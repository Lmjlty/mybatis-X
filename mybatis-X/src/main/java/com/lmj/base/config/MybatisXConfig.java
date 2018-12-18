package com.lmj.base.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lmj.base.interceptors.ChangeReturnClassInterceptor;
import com.lmj.base.provider.BaseSqlProvider;
import com.lmj.base.util.TableMataData;
import com.lmj.base.util.TableMataDataDefaultImpl;

/**
 * 相关配置
 * @author lmj
 *
 */
@Configuration
public class MybatisXConfig {
	/**
	 * 当你不注入用于自己的实现 TableMataData的实现的时候
	 * 会注入一个默认的实现 TableMataDataDefaultImpl
	 * @return
	 */
	@Bean
	@ConditionalOnMissingBean(name={"tableMataData"})
	public TableMataData tableMataData() {
		return new TableMataDataDefaultImpl();
	}
	
	@Bean
	@ConditionalOnMissingBean(name={"changeReturnClassInterceptor"})
	public ChangeReturnClassInterceptor changeReturnClassInterceptor() {
		return new ChangeReturnClassInterceptor();
	}
	/**
	 * 这个注入主要是为了实现自己的
	 * @return
	 */
	@Bean
	public BaseSqlProvider baseSqlProvider(ApplicationContext context) {
		BaseSqlProvider provider = new BaseSqlProvider();
		provider.setTableMataData((TableMataData) context.getBean("tableMataData"));
		return provider;
	}
}
