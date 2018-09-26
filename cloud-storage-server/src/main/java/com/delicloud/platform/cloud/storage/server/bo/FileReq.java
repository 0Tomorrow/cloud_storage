package com.delicloud.platform.cloud.storage.server.bo;

import com.delicloud.platform.common.lang.bo.JsonBase;
import com.delicloud.platform.common.lang.bo.page.ReqPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class FileReq extends JsonBase {
    private Long account;
}
