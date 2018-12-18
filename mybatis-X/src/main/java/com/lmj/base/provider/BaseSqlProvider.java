package com.lmj.base.provider;

import java.util.Map;
import java.util.Set;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.Assert;

import com.lmj.base.entity.Page;
import com.lmj.base.threadLocal.ChangeReturnClassThreadLocal;
import com.lmj.base.util.TableMataData;
import com.lmj.base.wrapper.Wrapper;

public class BaseSqlProvider {
	/**
	 * 需要自己注入
	 */
	private static TableMataData tableMataData;
	
	public void setTableMataData(TableMataData tableMataData) {
		BaseSqlProvider.tableMataData = tableMataData;
	}
    
    public <Entity> String insert(Entity entity) throws IllegalArgumentException, IllegalAccessException {
        Assert.notNull(entity, "entity must not null");
        Class<? extends Object> entityClass = entity.getClass();
        SQL sql = new SQL();
        // 得到前缀
        String tableName = tableMataData.getTableName(entityClass);
        sql.INSERT_INTO(tableName);
        // 根据field域 已经entity去转换成对应的sql语句 带参数
        Map<String, String> columnAndValue = tableMataData.getColumnAndValue(entity);
        // 处理id
        Map<String, String> idColumnAndValue = tableMataData.getIdColumnAndValue(entityClass);
        columnAndValue.putAll(idColumnAndValue);
        columnAndValue.forEach((key, value) -> {
            sql.VALUES(key, value);
        });
        return sql.toString();
    }
    
    public <Entity> String updateById(Entity entity) throws Exception {
        Assert.notNull(entity, "entity must not null");
        tableMataData.checkIdHaveValue(entity);
        Class<? extends Object> entityClass = entity.getClass();
        SQL sql = new SQL();
        // 得到前缀
        String tableName = tableMataData.getTableName(entityClass);
        sql.UPDATE(tableName);
        // 根据field域 已经entity去转换成对应的sql语句 带参数
        Map<String, String> columnAndValue = tableMataData.getColumnAndValue(entity);
        Assert.notEmpty(columnAndValue, "you must have some field value");
        columnAndValue.forEach((key, value) -> {
            sql.SET(new StringBuffer(key).append("=").append(value.toString()).toString());
        });
        tableMataData.makeSQLAppendId(entityClass, sql);
        return sql.toString();
    }
    
    public <Entity> String deleteById(ProviderContext context) {
        Class<?> entityClass = tableMataData.getClassFromContext(context);
        SQL sql = new SQL();
        // 得到表名
        String tableName = tableMataData.getTableName(entityClass);
        sql.DELETE_FROM(tableName);
        tableMataData.makeSQLAppendId(entityClass, sql);
        return sql.toString();
    }
    
    public <Entity> String selectById(ProviderContext context) throws ReflectiveOperationException {
        Class<?> entityClass = tableMataData.getClassFromContext(context);
        SQL sql = new SQL();
        // 得到表名
        String tableName = tableMataData.getTableName(entityClass);
        Set<String> columnAndValue = tableMataData.getColumnByClass(entityClass);
        Assert.notEmpty(columnAndValue, "you must have some field value");
        columnAndValue.forEach(column -> {
            sql.SELECT(column);
        });
        sql.FROM(tableName);
        tableMataData.makeSQLAppendId(entityClass, sql);
        return sql.toString();
    }
    
    /**
     * 根据id 和class 返回固定格式
     * 
     * @param providerContext
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public String selectByIdSureFieldByClass(MapperMethod.ParamMap<?> paramMap, ProviderContext context)
            throws InstantiationException, IllegalAccessException {
        // 获取到class信息
        Class<?> returnClass = (Class<?>) paramMap.get("returnClass");
        SQL sql = new SQL();
        // 得到表名
        String tableName = tableMataData.getTableName(returnClass);
        Set<String> columnAndValue = tableMataData.getColumnByClass(returnClass);
        Assert.notEmpty(columnAndValue, "your " + returnClass + " must have some field value");
        // 设置returnClass
        ChangeReturnClassThreadLocal.setReturnClass(returnClass);
        columnAndValue.forEach(column -> {
            sql.SELECT(column);
        });
        sql.FROM(tableName);
        // 处理id属性
        tableMataData.makeSQLAppendId(returnClass, sql);
        return sql.toString();
    }
    
    /**
     * selectList
     */
    public <Entity> String selectList(ProviderContext context, MapperMethod.ParamMap<Object> paramMap) {
        Wrapper<?> wrapper = (Wrapper<?>) paramMap.get("wrapper");
        Class<?> entityClass = tableMataData.getClassFromContext(context);
        Assert.notNull(entityClass, "your returnClass can't be null");
        String tableName = tableMataData.getTableName(entityClass);
        // 得到表名
        SQL sql = new SQL();
        Set<String> columnAndValue = tableMataData.getColumnByClass(entityClass);
        Assert.notEmpty(columnAndValue, "you must have some field value");
        columnAndValue.forEach(column -> {
            sql.SELECT(column);
        });
        sql.FROM(tableName);
        if (null != wrapper && !wrapper.isEmpty()) {
            // 切入 sql
            sql.WHERE(wrapper.getSQL());
            // 为了防止sql注入,放进参数里面
            Map<String, Object> map = wrapper.getMap();
            map.forEach((key, value) -> {
                paramMap.put(key, value);
            });
        }
        return sql.toString();
    }
    
    /**
     * 只找到确定的列
     */
    public String selectListSureFieldByClass(MapperMethod.ParamMap<Object> paramMap, ProviderContext context) {
        Wrapper<?> wrapper = (Wrapper<?>) paramMap.get("wrapper");
        // 获取到class信息
        Class<?> returnClass = (Class<?>) paramMap.get("returnClass");
        SQL sql = new SQL();
        // 得到表名
        String tableName = tableMataData.getTableName(returnClass);
        Set<String> columnAndValue = tableMataData.getColumnByClass(returnClass);
        Assert.notEmpty(columnAndValue, "your " + returnClass + " must have some field value");
        // 设置returnClass
        ChangeReturnClassThreadLocal.setReturnClass(returnClass);
        columnAndValue.forEach(column -> {
            sql.SELECT(column);
        });
        sql.FROM(tableName);
        if (null != wrapper && !wrapper.isEmpty()) {
            // 切入 sql
            sql.WHERE(wrapper.getSQL());
            // 为了防止sql注入,放进参数里面
            Map<String, Object> map = wrapper.getMap();
            map.forEach((key, value) -> {
                paramMap.put(key, value);
            });
        }
        return sql.toString();
    }
    
    public <Entity> String updateAppendById(Entity entity) throws Exception {
        Assert.notNull(entity, "entity must not null");
        tableMataData.checkIdHaveValue(entity);
        Class<? extends Object> entityClass = entity.getClass();
        SQL sql = new SQL();
        // 得到前缀
        String tableName = tableMataData.getTableName(entityClass);
        sql.UPDATE(tableName);
        // 根据field域 已经entity去转换成对应的sql语句 带参数
        Map<String, String> columnAndValue = tableMataData.getColumnAndValue(entity);
        Assert.notEmpty(columnAndValue, "you must have some field value");
        columnAndValue.forEach((key, value) -> {
            sql.SET(new StringBuffer(key).append("=").append(key).append("+").append(value.toString()).toString());
        });
        tableMataData.makeSQLAppendId(entityClass, sql);
        return sql.toString();
    }
    //待完成
//    public <Entity> String insertBatch(MapperMethod.ParamMap<?> paramMap) throws IllegalArgumentException, IllegalAccessException {
//        List<Entity> entities = (List<Entity>) paramMap.get("list");
//        paramMap.remove("list");
//        Assert.notEmpty(entities, "entities must not be empty");
//        Class<?> entityClass = entities.get(0).getClass();
//        SQL sql = new SQL();
////        // 得到前缀
//        String tableName = tableMataData.getTableName(entityClass);
//        sql.INSERT_INTO(tableName);
//        // 根据field域 已经entity去转换成对应的sql语句 带参数
//        // 处理id
//        Map<String, String> idColumnAndValue = tableMataData.getIdColumnAndValue(entityClass);
//        String idColumn = tableMataData.getIdColumn(entityClass);
////        tableMataData.getColumnAndValue(entity);
//        Set<String> columnes = tableMataData.getColumnByClass(entityClass);
//        if(!idColumnAndValue.isEmpty()) {
//        	idColumnAndValue.forEach((key,value) ->{
//        		columnes.add(key);
//        	});
//        }else {
//        	columnes.remove(idColumn);
//        }
//        StringBuilder temp = new StringBuilder(sql.toString()).append(" (");
//     
//        columnes.forEach(column ->{
//        	temp.append(column).append(",");
//        });
//        temp.deleteCharAt(temp.length() - 1).append(") values ");
//        //放入 (#{userName},#{password})
//        entities.forEach(entity ->{
//        });
//        return temp.toString();
//    }
    /**
     * 找到条数
     */
    public String selectCount(MapperMethod.ParamMap<Object> paramMap, ProviderContext context) {
    	Wrapper<?> wrapper = (Wrapper<?>) paramMap.get("wrapper");
        SQL sql = new SQL();
        //because count(*) is more is faster than count (id)
        sql.SELECT(" count(*) ");
        // 得到表名
        Class<?> entityClass = tableMataData.getClassFromContext(context);
        String tableName = tableMataData.getTableName(entityClass);
        sql.FROM(tableName);
        if (null != wrapper && !wrapper.isEmpty()) {
            // 切入 sql
            sql.WHERE(wrapper.getSQL());
            // 为了防止sql注入,放进参数里面
            Map<String, Object> map = wrapper.getMap();
            map.forEach((key, value) -> {
                paramMap.put(key, value);
            });
        }
        return sql.toString();
    }
    
    /**
     * selectList 分页
     */
    public <Entity> String selectPage(ProviderContext context, MapperMethod.ParamMap<Object> paramMap) {
        Wrapper<?> wrapper = (Wrapper<?>) paramMap.get("wrapper");
        Class<?> entityClass = tableMataData.getClassFromContext(context);
        Assert.notNull(entityClass, "your returnClass can't be null");
        String tableName = tableMataData.getTableName(entityClass);
        // 得到表名
        SQL sql = new SQL();
        Set<String> columnAndValue = tableMataData.getColumnByClass(entityClass);
        Assert.notEmpty(columnAndValue, "you must have some field value");
        columnAndValue.forEach(column -> {
            sql.SELECT(column);
        });
        sql.FROM(tableName);
        if (null != wrapper && !wrapper.isEmpty()) {
            // 切入 sql
            sql.WHERE(wrapper.getSQL());
            // 为了防止sql注入,放进参数里面
            Map<String, Object> map = wrapper.getMap();
            map.forEach((key, value) -> {
                paramMap.put(key, value);
            });
        }
        Page page = (Page) paramMap.get("page");
        return tableMataData.getLimit(page,sql);
    }
    
    /**
     * 分页只找到固定的列
     */
    public String selectPageSureFieldByClass(MapperMethod.ParamMap<Object> paramMap, ProviderContext context) {
        Wrapper<?> wrapper = (Wrapper<?>) paramMap.get("wrapper");
        // 获取到class信息
        Class<?> returnClass = (Class<?>) paramMap.get("returnClass");
        SQL sql = new SQL();
        // 得到表名
        String tableName = tableMataData.getTableName(returnClass);
        Set<String> columnAndValue = tableMataData.getColumnByClass(returnClass);
        Assert.notEmpty(columnAndValue, "your " + returnClass + " must have some field value");
        // 设置returnClass
        ChangeReturnClassThreadLocal.setReturnClass(returnClass);
        columnAndValue.forEach(column -> {
            sql.SELECT(column);
        });
        sql.FROM(tableName);
        if (null != wrapper && !wrapper.isEmpty()) {
            // 切入 sql
            sql.WHERE(wrapper.getSQL());
            // 为了防止sql注入,放进参数里面
            Map<String, Object> map = wrapper.getMap();
            map.forEach((key, value) -> {
                paramMap.put(key, value);
            });
        }
        Page page = (Page) paramMap.get("page");
        return tableMataData.getLimit(page,sql);
    }
 
}
