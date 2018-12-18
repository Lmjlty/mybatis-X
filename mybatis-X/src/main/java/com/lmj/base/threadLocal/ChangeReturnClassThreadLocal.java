package com.lmj.base.threadLocal;


public class ChangeReturnClassThreadLocal {
    
    private static ThreadLocal<Class<?>> returnClasses = new ThreadLocal<>();
    
    
    public static Boolean haveReturnClass() {
        return returnClasses.get() == null ? false : true;
    }
    
    public static Class<?> getReturnClass(){
        Class<?> result = returnClasses.get();
        returnClasses.remove();
        return result;
    }
    
    public static void setReturnClass(Class<?> clazz) {
        returnClasses.set(clazz);
    }
}
