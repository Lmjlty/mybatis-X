package com.lmj.base.wrapper;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntityWrapper<T> implements Wrapper<T> {
    
    private StringBuilder sql = new StringBuilder();
    
    /**
     * 为了防止sql注入
     */
    private int count = 0;
    
    private Map<String, Object> mappingsMap = new LinkedHashMap<>();
    
    /**
     * between模板
     */
    private static String betweenTemplate = " between #{?} and #{?} ";
    
    /**
     * 等式或者不等式模板
     */
    private static String relationTemplate = " ? #{?}";
    
    /**
     * in模板
     */
    private static String inTemplate = " ? (?)";
    /**
     * like模板
     */
    private static String likeTemplate = " like #{?}";
    
    @Override
    public Wrapper<T> eq(String column, Object value) {
        sql.append(" and ");
        String key = getKey(column, value);
        sql.append(column).append(relationTemplate.replaceFirst("\\?", "=").replaceFirst("\\?", key));
        return this;
    }
    
    @Override
    public Wrapper<T> gt(String column, Object value) {
        sql.append(" and ");
        String key = getKey(column, value);
        sql.append(column).append(relationTemplate.replaceFirst("\\?", ">").replaceFirst("\\?", key));
        return this;
    }
    
    @Override
    public Wrapper<T> lt(String column, Object value) {
        sql.append(" and ");
        String key = getKey(column, value);
        sql.append(column).append(relationTemplate.replaceFirst("\\?", "<").replaceFirst("\\?", key));
        return this;
    }
    
    @Override
    public Wrapper<T> in(String column, Collection<?> values) {
        sql.append(" and ");
        StringBuilder temp = new StringBuilder();
        values.forEach(value -> {
            String key = getKey(column, value);
            temp.append("#{").append(key).append("}").append(",");
        });
        temp.deleteCharAt(temp.length() - 1);
        sql.append(column).append(inTemplate.replaceFirst("\\?"," in ").replaceFirst("\\?", temp.toString()));
        return this;
    }
    
    @Override
    public Wrapper<T> between(String column, Object start, Object end) {
        sql.append(" and ");
        String startKey = getKey(column, start);
        String endKey = getKey(column, end);
        sql.append(column).append(" ");
        sql.append(betweenTemplate.replaceFirst("\\?", startKey).replaceFirst("\\?", endKey));
        return this;
    }
    
    @Override
    public Wrapper<T> or() {
        sql.append(" or ");
        return this;
    }
    
    @Override
    public Wrapper<T> and() {
        sql.append(" and ");
        return this;
    }
    
    @Override
    public String getSQL() {
        // 删除第一个and
        int indexOf = sql.indexOf(" and");
        if (0 == indexOf)
            sql.delete(0, 4);
        return sql.toString();
    }
    
    @Override
    public Wrapper<T> orderBy(String column, boolean isAsc) {
        sql.append(" order by ").append(column).append(" ").append(isAsc ? " asc " : " desc ");
        return this;
    }
    
    @Override
    public Wrapper<T> groupBy(String column) {
        sql.append(" group by ").append(column).append(" ");
        return this;
    }
    
    private String getKey(String column, Object value) {
        String key = new StringBuilder(column).append(count++).toString();
        mappingsMap.put(key, value);
        return key;
    }
    
    @Override
    public Map<String, Object> getMap() {
        return mappingsMap;
    }
    
    @Override
    public Wrapper<T> ne(String column, Object value) {
        sql.append(" and ");
        String key = getKey(column, value);
        sql.append(column).append(relationTemplate.replaceFirst("\\?", "<>").replaceFirst("\\?", key));
        return this;
    }
    
    @Override
    public Wrapper<T> notIn(String column, Collection<?> values) {
        sql.append(" and ");
        StringBuilder temp = new StringBuilder();
        values.forEach(value -> {
            String key = getKey(column, value);
            temp.append("#{").append(key).append("}").append(",");
        });
        temp.deleteCharAt(temp.length() - 1);
        sql.append(column).append(inTemplate.replaceFirst("\\?"," not in ").replaceFirst("\\?", temp.toString()));
        return this;
    }

    @Override
    public Wrapper<T> like(String column, Object value, boolean leftLike, boolean rightLike) {
        sql.append(" and ");
        sql.append(column);
        StringBuilder tempValue=new StringBuilder();
        if(leftLike)
            tempValue.append("%");
        tempValue.append(value);
        if(rightLike)
            tempValue.append("%");
        String key = getKey(column,tempValue.toString());
        sql.append(likeTemplate.replaceFirst("\\?", key));
        return this;
    }

    @Override
    public Wrapper<T> ge(String column, Object value) {
        sql.append(" and ");
        String key = getKey(column, value);
        sql.append(column).append(relationTemplate.replaceFirst("\\?", ">=").replaceFirst("\\?", key));
        return this;
    }

    @Override
    public Wrapper<T> le(String column, Object value) {
        sql.append(" and ");
        String key = getKey(column, value);
        sql.append(column).append(relationTemplate.replaceFirst("\\?", "<=").replaceFirst("\\?", key));
        return this;
    }

    @Override
    public Wrapper<T> isNull(String column) {
        sql.append(" and ").append(" ISNULL(").append(column).append(")");
        return this;
    }
    
    @Override
    public Wrapper<T> isNotNull(String column) {
        sql.append(" and ").append(column).append("IS NOT NULL");
        return this;
    }

    @Override
    public boolean isEmpty() {
        return sql.toString().trim().length() == 0;
    }

	
    
}
