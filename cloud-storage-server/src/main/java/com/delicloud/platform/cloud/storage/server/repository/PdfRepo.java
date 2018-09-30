package com.delicloud.platform.cloud.storage.server.repository;

import com.delicloud.platform.cloud.storage.server.entity.TPdfInfo;
import com.delicloud.platform.common.data.repository.MyRepository;

import java.util.List;

public interface PdfRepo extends MyRepository<TPdfInfo, Long> {
    List<TPdfInfo> findAllByPdfMd5OrderByPdfIndex(String pdfMd5);
}
