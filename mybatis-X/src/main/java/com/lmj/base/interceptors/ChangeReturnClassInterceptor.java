package com.lmj.base.interceptors;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.lmj.base.exception.StartException;
import com.lmj.base.log.LogUtil;
import com.lmj.base.threadLocal.ChangeReturnClassThreadLocal;


/**
 * 修改返回类型拦截器
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
		RowBounds.class, ResultHandler.class }) })
public class ChangeReturnClassInterceptor implements Interceptor {

	public static final LogUtil logger = LogUtil.getLog(ChangeReturnClassInterceptor.class);
	/**
	 * 方法缓存
	 */
	private static Map<String, Boolean> enterMap=new ConcurrentHashMap<>();
	/**
	 * 首次加载,避免线程不安全
	 */
	private static Field field = null;
	static {
		try {
			field = ResultMap.class.getDeclaredField("type");
		} catch (Exception e) {
			throw new StartException("can't find the type field from the org.apache.ibatis.mapping.ResultMap");
		}
		field.setAccessible(true);
	}

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		// 如果名字刚好符合
		String id = mappedStatement.getId();
		// 说明刚好是使用的这个,这个时候得到sql 是为了调用一次threadLocal
		if (shouldEnter(id)) {
			// 判断是否是框架自己的
			mappedStatement.getBoundSql(invocation.getArgs()[1]);
			if (ChangeReturnClassThreadLocal.haveReturnClass()) {
				List<ResultMap> resultMaps = mappedStatement.getResultMaps();
				ResultMap resultMap = resultMaps.get(0);
				field.set(resultMap, ChangeReturnClassThreadLocal.getReturnClass());
				// 这里不还原
			}
		}
		Object returnValue = invocation.proceed();
		return returnValue;
	}

	private boolean shouldEnter(String id) {
	    Boolean result = enterMap.get(id);
	    if(null != result)
	        return result;
	    result = (id != null && -1 != id.indexOf("SureFieldByClass"));
	    enterMap.put(id, result);
	    return result;
	}

    @Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {

	}
}