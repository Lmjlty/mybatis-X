package com.lmj.base.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表不存在这个字段
 * @author lmj
 *
 */
@Target(value= {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME) 
@Documented
public @interface TableFieldNotExist {
    
}
