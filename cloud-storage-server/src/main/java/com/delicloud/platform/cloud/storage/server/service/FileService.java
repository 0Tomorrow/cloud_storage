package com.delicloud.platform.cloud.storage.server.service;

import com.delicloud.platform.cloud.storage.core.bo.FileInfoResp;
import com.delicloud.platform.cloud.storage.core.enums.FileState;
import com.delicloud.platform.cloud.storage.core.bo.FileInfo;
import com.delicloud.platform.cloud.storage.server.bo.PdfImgInfo;
import com.delicloud.platform.cloud.storage.server.bo.SliceInfo;
import com.delicloud.platform.cloud.storage.server.config.IconConfig;
import com.delicloud.platform.cloud.storage.server.config.PathConfig;
import com.delicloud.platform.cloud.storage.server.config.PreviewConfig;
import com.delicloud.platform.cloud.storage.server.entity.TFileInfo;
import com.delicloud.platform.cloud.storage.server.entity.TIndexInfo;
import com.delicloud.platform.cloud.storage.server.entity.TPdfInfo;
import com.delicloud.platform.cloud.storage.server.entity.TUploadInfo;
import com.delicloud.platform.cloud.storage.server.file.FileUtil;
import com.delicloud.platform.cloud.storage.server.file.TempFileUtil;
import com.delicloud.platform.cloud.storage.server.mq.MqFileService;
import com.delicloud.platform.cloud.storage.server.repository.FileRepo;
import com.delicloud.platform.cloud.storage.server.repository.IndexRepo;
import com.delicloud.platform.cloud.storage.server.repository.PdfRepo;
import com.delicloud.platform.cloud.storage.server.repository.UploadRepo;
import com.delicloud.platform.cloud.storage.server.repository.cache.FileSliceCache;
import com.delicloud.platform.common.lang.exception.PlatformException;
import com.delicloud.platform.common.lang.util.MyBeanUtils;
import com.delicloud.platform.common.lang.util.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FileService {
    @Autowired
    PathConfig pathConfig;

    @Autowired
    IconConfig iconConfig;

    @Autowired
    PreviewConfig previewConfig;

    @Autowired
    FileRepo fileRepo;

    @Autowired
    IndexRepo indexRepo;

    @Autowired
    UploadRepo uploadRepo;

    @Autowired
    MqFileService mqFileService;

    @Autowired
    PdfRepo pdfRepo;

    public List<FileInfoResp> findFile(Long account, String path) {
        if (account == null) {
            throw new PlatformException("用户未登录");
        }
        String relativePath = pathConfig.getRelativePath(path);
        List<FileInfoResp> list = new ArrayList<>();
        List<TFileInfo> tList = fileRepo.findAllByUpdateByAndPath(account, relativePath);
        for (TFileInfo tFileInfo : tList) {
            FileInfoResp fileInfoResp = MyBeanUtils.copyProperties2(tFileInfo, FileInfoResp.class);
            list.add(fileInfoResp);
        }
        return list;
    }

    public String handShake(FileInfo fileInfo) {
        String fileName = fileInfo.getFileName();
        Long account = fileInfo.getUpdateBy();
        String path = fileInfo.getPath();
        String tempPath = pathConfig.getTempPath(account, path, fileName);

        String type = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        fileInfo.setType(type);
        String icon = iconConfig.getIcon(type);
        fileInfo.setIcon(icon);
        try {
            log.info("create temp file : {}, size : {}", tempPath, fileInfo.getFileSize());
            TempFileUtil.createTempFile(tempPath, fileInfo.getFileSize());
        } catch (Exception e) {
            throw new PlatformException("temp文件创建失败:" + tempPath);
        }
        return saveFileInfoAndUpdateInfo(fileInfo);
    }

    private String saveFileInfoAndUpdateInfo(FileInfo fileInfo) {
        Long account = fileInfo.getUpdateBy();
        String path = fileInfo.getPath();
        String relativePath = pathConfig.getRelativePath(path);
        TFileInfo tFileInfo = MyBeanUtils.copyProperties2(fileInfo, TFileInfo.class);
        tFileInfo.setIndexInfo(indexRepo.findFirstByUpdateByAndPath(account, relativePath));

        try {
            tFileInfo = fileRepo.save(tFileInfo);
        } catch (Exception e) {
            throw new PlatformException("save tFileInfo时出错: " + tFileInfo.getFileName());
        }

        TUploadInfo tUploadInfo = new TUploadInfo();
        tUploadInfo.setTFileInfo(tFileInfo);
        tUploadInfo.setFileState(FileState.HANDSHAKE.getCode());
        tUploadInfo.setMergeCode(StringEncoder.encodeByMD5(account + ":" + System.currentTimeMillis()));
        tUploadInfo.setUploadCount(0);
        tUploadInfo.setSliceSize(fileInfo.getSliceSize());
        tUploadInfo.setSliceCount(fileInfo.getSliceCount());
        try {
            uploadRepo.save(tUploadInfo);
        } catch (Exception e) {
            throw new PlatformException("save tUploadInfo时出错: " + tFileInfo.getFileName());
        }

        return tUploadInfo.getMergeCode();
    }

    public void mqMerge(MultipartFile file, Integer index, String mergeCode) {
        TUploadInfo tUploadInfo = uploadRepo.findFirstByMergeCode(mergeCode);

        if (null == tUploadInfo) {
            throw new PlatformException("文件识别码不存在: " + mergeCode);
        }

        SliceInfo sliceInfo = new SliceInfo();
        sliceInfo.setIndex(index);
        sliceInfo.setMergeCode(mergeCode);
        try {
            byte[] bytes = FileUtil.fileToBytes(file);
            sliceInfo.setFile(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("send mq merge file : {}, index : {}", mergeCode, index);
        mqFileService.fileMergeOutput(sliceInfo);
    }

    public void merge(SliceInfo sliceInfo) {
        byte[] file = sliceInfo.getFile();
        int index = sliceInfo.getIndex();
        String mergeCode = sliceInfo.getMergeCode();
        TUploadInfo tUploadInfo = uploadRepo.findFirstByMergeCode(mergeCode);
        String path = tUploadInfo.getTFileInfo().getIndexInfo().getPath();
        Long account = tUploadInfo.getTFileInfo().getUpdateBy();
        String fileName = tUploadInfo.getTFileInfo().getFileName();

        String tempPath = pathConfig.getTempPath(account, path, fileName);
        Long sliceSize = tUploadInfo.getSliceSize();

        Long pos = index * sliceSize;
        log.info("start merge file : {}, index : {}", tempPath, index);
        FileUtil.merge(file, pos, tempPath);

        if (!isFinish(mergeCode)) {
            return;
        }
        String filePath = pathConfig.getAbsoluteFilePath(account, path, fileName);
        FileUtil.changeName(tempPath, filePath);
    }

    private synchronized boolean isFinish(String mergeCode) {
        TUploadInfo tUploadInfo = uploadRepo.findFirstByMergeCode(mergeCode);
        Integer uploadCount = tUploadInfo.getUploadCount();
        Integer sliceCount = tUploadInfo.getSliceCount();
        uploadCount++;
        if (uploadCount.equals(sliceCount)) {
            log.info("upload finished, file : {}", tUploadInfo);
            tUploadInfo.setUploadCount(uploadCount);
            tUploadInfo.setFileState(FileState.FINISH.getCode());
            uploadRepo.save(tUploadInfo);
            return true;
        }
        tUploadInfo.setUploadCount(uploadCount);
        tUploadInfo.setFileState(FileState.UPLOADING.getCode());
        uploadRepo.save(tUploadInfo);
        return false;
    }

//    @Scheduled(cron = "0/2 * * * * *")
    public void deleteFile(String id) {
        TFileInfo tFileInfo = fileRepo.getOne(Long.parseLong(id));
        if (tFileInfo == null) {
            throw new PlatformException("id不存在");
        }
        uploadRepo.deleteAllByTFileInfo(Long.parseLong(id));
        fileRepo.delete(Long.parseLong(id));
        String absoluteFilePath = pathConfig.getAbsoluteFilePath(tFileInfo);
        FileUtil.deleteFile(absoluteFilePath);
    }

    public List<PdfImgInfo> preview(String id) {
        TFileInfo tFileInfo = fileRepo.getOne(Long.parseLong(id));
        if (tFileInfo == null) {
            throw new PlatformException("id不存在");
        }
        String absoluteFilePath = pathConfig.getAbsoluteFilePath(tFileInfo);
        if (!tFileInfo.getType().equals("pdf")) {
            return null;
        }
        String fileMd5 = FileUtil.getFileMd5(absoluteFilePath);
        if (null == fileMd5) {
            throw new PlatformException("创建文件md5失败");
        }
        List<TPdfInfo> list = pdfRepo.findAllByPdfMd5OrderByPdfIndex(fileMd5);
        if (list.isEmpty()) {
            list = previewConfig.pdfPreview(absoluteFilePath, fileMd5);
            pdfRepo.save(list);
        }
        return MyBeanUtils.copyCollectionProperties(list, PdfImgInfo.class);
    }

    public String getDownloadPath(String id) {
        TFileInfo tFileInfo = fileRepo.getOne(Long.parseLong(id));
        if (tFileInfo == null) {
            throw new PlatformException("id不存在");
        }
        Long account = tFileInfo.getUpdateBy();
        String fileName = tFileInfo.getFileName();
        String path = tFileInfo.getIndexInfo().getPath();
        String relativePath = pathConfig.getRelativePath(path);
//        TFileInfo tFileInfo = fileRepo.findFirstByUpdateByAndPathAndFileName(account, relativePath, fileName);
        return "/cloud_storage/user_file/" + account + relativePath + fileName;
    }
}
