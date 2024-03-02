package ru.inno.task4.util;

import java.lang.reflect.Proxy;

public class LogUtils {
    public static <T> T logTransform(T obj){
        ClassLoader classLoader = obj.getClass().getClassLoader();
        Class[] interfaces = obj.getClass().getInterfaces();
        return (T) Proxy.newProxyInstance(
                classLoader,
                interfaces,
                new LogHandler(obj)
        );
    }
}
