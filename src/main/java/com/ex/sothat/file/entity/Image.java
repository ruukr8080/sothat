package com.ex.sothat.file.entity;

import com.ex.sothat.global.common.BaseTimeEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class Image extends BaseTimeEntity {
    protected String url;
}
