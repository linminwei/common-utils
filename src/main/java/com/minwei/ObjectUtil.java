package com.minwei;

import org.springframework.beans.BeanUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lmw
 */
public class ObjectUtil {

    /**
     * 拷贝字段
     * @param sourceObject 源对象
     * @param targetObject 目标对象类对象
     * @return
     * @param <S>
     * @param <T>
     */
    public static <S, T> T copyField(S sourceObject, Class<T> targetObject) {
        try {
            // 创建目标对象实例
            T target = targetObject.newInstance();
            BeanUtils.copyProperties(sourceObject, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("拷贝字段异常:" + e.getMessage(), e);
        }
    }

    public static <S, T> List<T> copyField(List<S> sourceObjects, Class<T> targetObject) {
        return sourceObjects.stream().map(source -> copyField(source, targetObject)).collect(Collectors.toList());
    }


    /**
     * 从Lambda表达式传入的函数式接口获取对应字段
     *
     * @param function function
     * @param <T>      泛型
     * @return Field
     */
    public static <T> Field getField(Function<T, ?> function) {
        // 从Function取出序列化方法
        Method writeReplaceMethod;
        try {
            writeReplaceMethod = function.getClass().getDeclaredMethod("writeReplace");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        // 从序列化方法取出序列化的Lambda信息
        boolean isAccessible = writeReplaceMethod.isAccessible();
        writeReplaceMethod.setAccessible(true);
        SerializedLambda serializedLambda;
        try {
            serializedLambda = (SerializedLambda) writeReplaceMethod.invoke(function);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        writeReplaceMethod.setAccessible(isAccessible);

        // 从Lambda信息中取出Method、Field、Class等
        String fieldName = serializedLambda.getImplMethodName().substring("get".length());
        fieldName = fieldName.replaceFirst(fieldName.charAt(0) + "", (fieldName.charAt(0) + "").toLowerCase());
        try {
            return Class.forName(serializedLambda.getImplClass().replace("/", ".")).getDeclaredField(fieldName);
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建泛型实例
     *
     * @param clazz 类对象
     * @param <T>   泛型
     * @return 泛型实例
     */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("创建泛型实例异常:" + e.getMessage(), e);
        }
    }

    /**
     * 判断某个字段是否是自定义类
     *
     * @param clazz 字段
     */
    public static boolean isCustomObject(Class<?> clazz) {
        // 排除基本数据类型
        if (clazz.isPrimitive()) {
            return false;
        }
        // 排除JDK内置的类(通过包名判断)
        if (clazz.getName().startsWith("java.") || clazz.getName().startsWith("javax.")) {
            return false;
        }
        // 排除数组类型
        if (clazz.isArray()) {
            return isCustomObject(clazz.getComponentType());
        }
        return true;
    }

    /**
     * 获取泛型参数中的类对象(针对于List<Object>,获取Object的类对象)
     *
     * @param field 字段
     * @return 类对象
     */
    public static Class<?> getGenericParameterClass(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof Class) {
                return (Class<?>) actualTypeArguments[0];
            }
        }
        return null;
    }
}
