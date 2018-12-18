package com.lmj.base.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表名
 * @author lmj
 *
 */
@Target(value= {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
/**
 * 该注解表示 @TableName可以被继承
 */
@Inherited
@Documented
public @interface TableName {
    
    String value();
}
