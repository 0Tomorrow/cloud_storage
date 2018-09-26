package com.delicloud.platform.cloud.storage.server.entity;

import com.delicloud.platform.common.data.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = TIndexInfo.TABLE_NAME)
@Data
@EqualsAndHashCode(callSuper = false)
public class TIndexInfo extends BaseEntity {
    protected static final String TABLE_NAME = "t_index_info";

    private String indexName;

    @ManyToOne(targetEntity = TIndexInfo.class)
    private TIndexInfo prevIndex;

    private String path;
}
