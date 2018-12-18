package com.lmj.base.wrapper;

import java.util.Collection;
import java.util.Map;

/**
 * 附加条件
 * @author lmj
 *
 */
public interface Wrapper<T> {
    /**
     * 前面会默认加一个 and 
     * 等于
     * @param column
     * @param value
     * @return
     */
    Wrapper<T> eq(String column,Object value);
    /**
     * 前面会默认加一个 and 
     * 大于
     * @param column
     * @param value
     * @return
     */
    Wrapper<T> gt(String column,Object value);
    /**
     * 前面会默认加一个 and 
     * 大于等于
     * @param column
     * @param value
     * @return
     */
    Wrapper<T> ge(String column,Object value);
    /**
     * 前面会默认加一个 and 
     * 不等于 <>
     * @param column
     * @param value
     * @return
     */
    Wrapper<T> ne(String column,Object value);
    /**
     * 前面会默认加一个 and 
     * 小于
     * @param column
     * @param value
     * @return
     */
    Wrapper<T> lt(String column,Object value);
    /**
     * 前面会默认加一个 and 
     * 小于等于
     * @param column
     * @param value
     * @return
     */
    Wrapper<T> le(String column,Object value);
    /**
     * 前面会默认加一个 and 
     * in
     * @param column
     * @param value
     * @return
     */
    Wrapper<T> in(String column,Collection<?> values);
    /**
     * 前面会默认加一个 and 
     * not in
     * @param column
     * @param value
     * @return
     */
    Wrapper<T> notIn(String column,Collection<?> values);
    /**
     * 前面会默认加一个 and 
     * between
     * @param column
     * @param value
     * @return
     */
    Wrapper<T> between(String column,Object start,Object end);
    /**
     * 前面会默认加一个 and 
     * isNull
     * @param column
     * @return
     */
    Wrapper<T> isNull(String column);
    /**
     * 前面会默认加一个 and 
     * column is not null
     * @param column
     * @return
     */
    Wrapper<T> isNotNull(String column);
    /**
     * sql 加一个 or
     * @return
     */
    Wrapper<T> or();
    /**
     * 加一个and
     * @return
     */
    Wrapper<T> and();
    /**
     * 前面会默认加一个 and 
     * like 
     * @param column
     * @param value
     * @param leftLike 当true的时候 %value
     * @param rightLike 当true的时候 value%
     * @return
     */
    Wrapper<T> like(String column,Object value,boolean leftLike,boolean rightLike);
    /**
     * 排序
     * @param column
     * @param isAsc true 升序
     * @return
     */
    Wrapper<T> orderBy(String column,boolean isAsc);
    /**
     * 分组
     * @param column
     * @return
     */
    Wrapper<T> groupBy(String column);
    /**
     * 获取sql
     * @return
     */
    String getSQL();
    /**
     * 为了防止sql注入而使用的map
     * @return
     */
    Map<String, Object> getMap();
    /**
     * 判断wrapper是否是空
     * @return
     */
    boolean isEmpty();
    
} 