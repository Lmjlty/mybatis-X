package com.lmj.base.enums;


/**
 * id生成策略
 * @author lmj
 *
 */
public enum IdEnum {
    //自增
    AUTO_INCREMENT,
    //uuid 32位 UUID.randomUUID().toString().replaceAll("-", "");
    UUID,
    //需要自己实现
    OWN;
}
