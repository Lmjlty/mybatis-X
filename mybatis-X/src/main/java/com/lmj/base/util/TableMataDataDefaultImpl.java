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
public class TableMataDataDefaultImpl implements TableMataData{

	/**
	 * 表名缓存
	 */
	protected static Map<Class<?>, String> tableNamesMap = new ConcurrentHashMap<>(128);

	/**
	 * 域 缓存
	 */
	protected static Map<Class<?>, List<Field>> fieldsMap = new ConcurrentHashMap<>(128);

	/**
	 * column 和 value的 缓存
	 */
	protected static Map<Field, Map<String, String>> columnAndValuesMap = new ConcurrentHashMap<>(128);

	/**
	 * 根据id操作 获取 class信息的缓存
	 */
	protected static Map<String, Class<?>> getClassByContextMap = new ConcurrentHashMap<>(256);

	/**
	 * select的列
	 */
	protected static Map<Class<?>, Set<String>> columnMap = new ConcurrentHashMap<>(128);
	/**
	 * 根据field得到column
	 */
	protected static Map<Field, String> columnFromFeildMap = new ConcurrentHashMap<>(256);
	/**
	 * #{field.getName()}
	 */
	protected static Map<Field, String> prepareValueMap = new ConcurrentHashMap<>(256);
	/**
	 * id field
	 */
	protected static Map<Class<?>, Field> idFieldMap = new ConcurrentHashMap<>(128);
	/**
	 * id = #{id} 的模板
	 */
	protected static Map<Class<?>, String> sqlAppendIDMap = new ConcurrentHashMap<>(128);

	/**
	 * uuid模板
	 */
	private static final String UUID_TEMPLATE = "'?'";
	
	private static final String LIMIT_TEMPLATE = " limit ?,?";

	@Override
	public String getTableName(Class<?> clazz) {
		String tableName = tableNamesMap.get(clazz);
		if (tableName != null)
			return tableName;
		TableName tableNameAnno = clazz.getAnnotation(TableName.class);
		if( null == tableNameAnno) 
		    throw new TableException("your " + clazz + " must have the annotation of TableName");
		tableName = tableNameAnno.value();
		tableNamesMap.put(clazz, tableName);
		return tableName;
	}

	@Override
	public List<Field> getAllField(Class<?> entityClass) {
		List<Field> result = fieldsMap.get(entityClass);
		if (result != null)
			return result;
		result = new ArrayList<>();
		Field[] fields = entityClass.getDeclaredFields();
		for (Field field : fields) {
			if (!shouldBeFilter(field))
				result.add(field);
		}
		fieldsMap.put(entityClass, result);
		return result;
	}

	@Override
	public <T> Map<String, String> getColumnAndValue(T entity) throws IllegalArgumentException, IllegalAccessException {
		Map<String, String> result = new LinkedHashMap<>();
		// 处理主键
		boolean isDealed = false;
		List<Field> fields = getAllField(entity.getClass());
		for (Field field : fields) {
			@SuppressWarnings("unused")
			Object obj;
			// 正常处理
			if (isDealed || null == field.getAnnotation(Id.class)) {
				field.setAccessible(true);
				if(null != field.get(entity))
				    getPrepareValue(field, result);
				field.setAccessible(false);
			// id不处理
			}else
				isDealed = true;
		}
		return result;
	}

	/**
	 * id处理 这个地方比较耦合,能够想到的是结合spring 根据type()去spring中寻找相应的处理
	 * @param field
	 * @param result
	 */
	protected void dealIdField(Field field, Map<String, String> result) {
		String column = getColumnName(field);
		// 自增不处理
		if (field.getAnnotation(Id.class).type().equals(IdEnum.UUID)) {
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			result.put(column, UUID_TEMPLATE.replaceAll("\\?", uuid));
			// 自己处理 result.put(id,#{id})
		} else if (field.getAnnotation(Id.class).type().equals(IdEnum.OWN)) {
			result.put(column, prepareValue(field));
		}
	}

	/**
	 * result.put(column,#{field.getName()})
	 * 
	 * @param field
	 * @param result
	 *            你的column-value的map
	 * @return String 返回column
	 */
	protected void getPrepareValue(Field field, Map<String, String> result) {
		Map<String, String> temp = columnAndValuesMap.get(field);
		if (null != temp) {
			result.putAll(temp);
		}
		temp = new LinkedHashMap<>();

		String column = getColumnName(field);
		String value = prepareValue(field);
		result.put(column, value);
		temp.put(column, value);
		columnAndValuesMap.put(field, temp);
	}

	/**
	 * 得到#{field.getName()}
	 * 
	 * @param field
	 * @return
	 */
	protected String prepareValue(Field field) {
		String result = prepareValueMap.get(field);
		if (null != result)
			return result;
		StringBuffer sb = new StringBuffer("#{");
		sb.append(field.getName()).append("}");
		result = sb.toString();
		prepareValueMap.put(field, result);
		return result;
	}

	/**
	 * 根据field确认column的名字 当field被@link {com.lmj.base.anno.TableField}修饰的时候,
	 * 会将column设置为 TableField.value() as field.getName(); 否则为field.getName();
	 * @return
	 */
	protected String getColumnName(Field field) {
		String column = columnFromFeildMap.get(field);
		if (null != column)
			return column;
		column = field.getName();
		TableField tableFieldAnno;
		if (null != (tableFieldAnno = field.getAnnotation(TableField.class))) {
			column = new StringBuffer(tableFieldAnno.value()).append(" as ").append(field.getName()).toString();
		}
		columnFromFeildMap.put(field, column);
		return column;
	}
	@Override
	public <Entity> void checkIdHaveValue(Entity entity) {
		Field field = getIdField(entity.getClass());
		boolean isIdHaveValue = false;
		field.setAccessible(true);
		try {
			Object object = field.get(entity);
			isIdHaveValue = object == null ? false : true;
		} catch (IllegalArgumentException e) {
			// ignore
		} catch (IllegalAccessException e) {
			// ignore
		}
		field.setAccessible(false);
		if(!isIdHaveValue)
		    throw new TableException("your bean " + entity + " must have the value of id");
	}
	@Override
	public Class<?> getClassFromContext(ProviderContext context) {
		Class<?> mapperType = context.getMapperType();
		Method method = context.getMapperMethod();
		String key = new StringBuffer(mapperType.toString()).append(method.toString()).toString();
		Class<?> result = getClassByContextMap.get(key);
		if (null != result)
			return result;
		for (Type parent : mapperType.getGenericInterfaces()) {
		    //这里硬要和spring耦合,后面找机会抽离
			ResolvableType parentType = ResolvableType.forType(parent);
			result = parentType.getGeneric(0).getRawClass();
			break;
		}
		getClassByContextMap.put(key, result);
		return result;
	}

	@Override
	public Map<String, String> getIdColumnAndValue(Class<?> entityClass) {
		Field idField = getIdField(entityClass);
		Map<String, String> result = new LinkedHashMap<>();
		dealIdField(idField, result);
		return result;
	}

	@Override
	public Field getIdField(Class<?> entityClass) {
		Field field = idFieldMap.get(entityClass);
		if (null != field)
			return field;
		Field[] fields = entityClass.getDeclaredFields();
		for (Field f : fields) {
			if (null != f.getAnnotation(Id.class) || null != f.getAnnotation(IdButNotExist.class)) {
				field = f;
				idFieldMap.put(entityClass, field);
				break;
			}
		}
		return field;
	}

	@Override
	public void makeSQLAppendId(Class<?> entityClass, SQL sql) {
		String result = sqlAppendIDMap.get(entityClass);
		if (null != result) {
			sql.WHERE(result);
			return;
		}
		Field idField = getIdField(entityClass);
		String column = getColumnName(idField);
		String value = prepareValue(idField);
		result = new StringBuffer(column).append(" = ").append(value).toString();
		sqlAppendIDMap.put(entityClass, result);
		sql.WHERE(result);
	}

	@Override
	public Set<String> getColumnByClass(Class<?> entityClass) {
		Set<String> columnes = columnMap.get(entityClass);
		if (null != columnes)
			return columnes;
		columnes = new LinkedHashSet<>();
		List<Field> fields = getAllField(entityClass);
		for (Field field : fields) {
			// 这个地方不处理驼峰,是因为mybatis自己处理了,没必要所有的都自己做
			String columnName = getColumnName(field);
			columnes.add(columnName);
		}
		columnMap.put(entityClass, columnes);
		return columnes;
	}

	/**
	 * 该属性是否该被过滤 留给你们自己拓展
	 * @param field
	 */
	protected boolean shouldBeFilter(Field field) {
		// 不存在该列
		TableFieldNotExist annotation = field.getAnnotation(TableFieldNotExist.class);
		IdButNotExist idButNotExistAnno=field.getAnnotation(IdButNotExist.class);
		if (null != annotation || null != idButNotExistAnno)
			return true;
		// 静态属性不要,因为serialVersionUID 是静态的
		if (Modifier.isStatic(field.getModifiers()))
			return true;
		return false;
	}
	@Override
	public String getIdColumn(Class<?> entityClass) {
		Field idField = getIdField(entityClass);
		return getColumnName(idField);
	}
	@Override
    public String getLimit(Page page, SQL sql) {
    	StringBuffer temp = new StringBuffer(sql.toString());
        temp.append(LIMIT_TEMPLATE.replaceFirst("\\?",page.getPageNo().toString()).replaceFirst("\\?", page.getPageSize().toString()));
        return temp.toString();
    }

}
