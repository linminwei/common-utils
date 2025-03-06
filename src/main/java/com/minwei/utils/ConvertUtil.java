package com.minwei.utils;

import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lmw
 */
public class ConvertUtil {

    /**
     * 将任意一个数值型对象转换为Number
     *
     * @param object object
     * @return Number
     */
    public static Number asNumber(Object object) {
        if (object == null) {
            return null;
        }

        if (object instanceof Integer) {
            return NumberUtils.parseNumber(object.toString(), Integer.class);
        }
        if (object instanceof Long) {
            return NumberUtils.parseNumber(object.toString(), Long.class);
        }
        if (object instanceof Float) {
            return NumberUtils.parseNumber(object.toString(), Float.class);
        }
        if (object instanceof Double) {
            return NumberUtils.parseNumber(object.toString(), Double.class);
        }
        if (object instanceof Short) {
            return NumberUtils.parseNumber(object.toString(), Short.class);
        }
        if (object instanceof Byte) {
            return NumberUtils.parseNumber(object.toString(), Byte.class);
        }
        if (object instanceof BigDecimal) {
            return NumberUtils.parseNumber(object.toString(), BigDecimal.class);
        }
        throw new RuntimeException("提供的对象非数值型");
    }

    /**
     * 将源对象转为集合
     *
     * @param object object
     * @param clazz  指定泛型
     * @param <T>    泛型
     * @return List<T>
     */
    public static <T> List<T> asList(Object object, Class<T> clazz) {
        if (object == null) {
            return Collections.emptyList();
        }
        if (!(object instanceof List)) {
            throw new RuntimeException("提供的对象非集合");
        }

        @SuppressWarnings("unchecked")
        List<T> list = (List<T>) object;

        return list.stream()
                .filter(Objects::nonNull)
                .filter(clazz::isInstance)
                .collect(Collectors.toList());
    }

    /**
     * 将源对象转为Map
     *
     * @param object object
     * @param clazz  clazz
     * @param <T>    泛型
     * @return Map<String, T>
     */
    public static <T> Map<String, T> asMap(Object object, Class<T> clazz) {
        if (object == null) {
            return Collections.emptyMap();
        }
        if (!(object instanceof Map)) {
            throw new RuntimeException("提供的对象非Map");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> sourceMap = (Map<String, Object>) object;

        return sourceMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null && clazz.isInstance(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entrt -> clazz.cast(entrt.getValue())));
    }
}