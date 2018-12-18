package com.lmj.base.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 用于dto,但是并不存在id
 * @author lmj
 *
 */
@Target(value= {ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IdButNotExist{
}
