//package com.delicloud.platform.cloud.storage.server.service;
//
//import com.delicloud.platform.cloud.storage.server.Application;
//import com.delicloud.platform.cloud.storage.server.bo.FileReq;
//import com.delicloud.platform.cloud.storage.server.entity.TFileInfo;
//import com.delicloud.platform.cloud.storage.server.repository.FileRepo;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.test.context.junit4.SpringRunner;
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Application.class)
//@Slf4j
//public class IndexServiceTest {
//
//    @Autowired
//    FileRepo fileRepo;
//
//    @Test
//    public void haveFile() {
//        FileReq fileReq = new FileReq();
//        fileReq.setAccount(15927860204L);
//        fileReq.setLimit(4);
//        fileReq.setOffset(0);
//        fileReq.setOrder("updateTime");
//        fileReq.setSort("desc");
//
//        Sort sort;
//        if (StringUtils.isNotBlank(fileReq.getOrder())
//                && StringUtils.isNotBlank(fileReq.getSort())) {
//            sort = new Sort(Sort.Direction.fromStringOrNull(fileReq.getSort()), fileReq.getOrder());
//        } else {
//            sort = new Sort(Sort.Direction.DESC, "updateTime");
//        }
//        Pageable pageable = new PageRequest(fileReq.getOffset(), fileReq.getLimit(), sort);
//        List<TFileInfo> page = fileRepo.findByUpdateBy(fileReq.getAccount(), pageable);
//
//        page.forEach(tFileInfo ->
//            System.out.println(tFileInfo.getFileName())
//        );
//
//
////        System.out.println(new FileReq().getFlag());
//    }
//}