package com.upsic.kkc.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class TopTreeLevelsCacheItem extends BaseEntity<Long> {

    @Column(nullable = false)
    private UUID uid;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String objectType;

    @Column(nullable = false)
    private UUID model;

    private UUID parent;

    @ManyToOne
    private TopTreeLevelsCache cache;

    @Type(type = "com.upsic.smktservice.utils.hibernate.CustomStringArrayType")
    @Column(columnDefinition = "text[]", nullable = false)
    private String[] typeFilter;

    @Type(type = "com.upsic.smktservice.utils.hibernate.CustomUuidArrayType")
    @Column(columnDefinition = "uuid[]", nullable = false)
    private UUID[] categoryUidFilter;

}
