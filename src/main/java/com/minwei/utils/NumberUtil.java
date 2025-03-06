package com.minwei.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author lmw
 */
@Slf4j
public class NumberUtil {

    /**
     * 判断Number对象是否是某个数值型
     *
     * @param number number
     * @param clazz  clazz
     * @param <T>    泛型
     * @return boolean
     */
    public static <T extends Number> boolean checkType(Number number, Class<T> clazz) {
        return clazz.isInstance(number);
    }
}
