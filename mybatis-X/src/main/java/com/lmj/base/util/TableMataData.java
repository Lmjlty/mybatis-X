package com.lmj.base.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.core.ResolvableType;

import com.lmj.base.anno.Id;
import com.lmj.base.anno.IdButNotExist;
import com.lmj.base.anno.TableField;
import com.lmj.base.anno.TableFieldNotExist;
import com.lmj.base.anno.TableName;
import com.lmj.base.entity.Page;
import com.lmj.base.enums.IdEnum;
import com.lmj.base.exception.TableException;


/**
 * 根据class信息得到表的属性值
 * 
 * @author lmj
 */
public interface TableMataData {
	/**
	 * 获取表名
	 * @param clazz
	 * @return
	 */
	public String getTableName(Class<?> clazz);

	/**
	 * 获取所有的列 除开static和TableFieldNotExist修饰的变量
	 * @param entityClass
	 * @return
	 */
	public List<Field> getAllField(Class<?> entityClass);

	/**
	 * 根据对象获取 column和column对应的值(value)
	 * 当value上面有{@link com.base.anno.Id}且type为{@link com.base.enums.IdEnum}.AUTO_INCREMENT的时候不会加入column
	 * 当value为null的时候不会加入column
	 * @param entity 对象
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public <Entity> Map<String, String> getColumnAndValue(Entity entity) throws IllegalArgumentException, IllegalAccessException;


	/**
	 * 检查entity这个类是否有{@link com.base.anno.Id} 并且id修饰 有没有值
	 * 
	 * @param entity
	 * @return
	 */
	public <Entity> void checkIdHaveValue(Entity entity);

	/**
	 * 根据ProviderContext 获取继承baseMapper的返回类型
	 * @param context
	 * @return
	 */
	public Class<?> getClassFromContext(ProviderContext context);

	/**
	 * 获取id 对应的column和 id.field.getName();
	 * @param entityClass
	 * @param result
	 * @param result
	 */
	public Map<String, String> getIdColumnAndValue(Class<?> entityClass);

	public Field getIdField(Class<?> entityClass);

	/**
	 * 给sql 在where后面添加 id=#{id}
	 * 
	 * @param entityClass
	 * @param sql
	 */
	public void makeSQLAppendId(Class<?> entityClass, SQL sql);
	/**
	 * 返回要查找的列
	 * @param entityClass
	 * @return
	 */
	public Set<String> getColumnByClass(Class<?> entityClass);
	/**
	 * 得到id对应的column名
	 * @param entityClass
	 * @return
	 */
	public String getIdColumn(Class<?> entityClass);
	/**
	 * sql.toString()+limit pageNo,pageSize
	 * @param page
	 * @param sql
	 */
    public String getLimit(Page page, SQL sql);

}
