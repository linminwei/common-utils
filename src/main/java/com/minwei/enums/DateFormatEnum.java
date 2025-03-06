package com.minwei.enums;

import lombok.Getter;

/**
 * @author lmw
 */

@Getter
public enum DateFormatEnum {

    YYYY_CN("yyyy年"),
    YYYY_MM("yyyy-MM"),
    YYYY_MM_DD_CN("yyyy年MM月dd日"),
    YYYY_MM_DD_LINE("yyyy-MM-dd"),
    YYYY_MM_DD_ITALIC("yyyy/MM/dd");

    private final String value;


    DateFormatEnum(String value) {
        this.value = value;
    }
}
