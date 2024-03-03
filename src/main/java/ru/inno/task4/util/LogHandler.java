package ru.inno.task4.util;

import lombok.AllArgsConstructor;
import ru.inno.task4.annotations.LogTransformation;
import ru.inno.task4.exception.WriteLogFileException;
import ru.inno.task4.service.Log;
import ru.inno.task4.service.Logable;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Arrays;

@AllArgsConstructor
public class LogHandler implements InvocationHandler {
    private final Object obj;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        var realMethod = obj.getClass().getMethod( method.getName(), (Class[]) method.getGenericParameterTypes());
        var argsBefore = Arrays.toString(args);
        var result = method.invoke(obj,args);
        if (realMethod.isAnnotationPresent(LogTransformation.class)) {
            try (Logable logFile = new Log(realMethod.getAnnotation(LogTransformation.class).logPath())){
                var stringBuilder = new StringBuilder()
                        .append(Instant.now().toString())
                        .append("\t")
                        .append(obj.getClass().getName())
                        .append("\t")
                        .append(argsBefore)
                        .append("\t")
                        .append(Arrays.toString(args))
                        .append("\n")
                        ;
                logFile.putLog(stringBuilder.toString());
            }
            catch (Exception e){
                throw new WriteLogFileException();
            }
        }
        return result;
    }
}
