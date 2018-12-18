package com.lmj.base.mapper;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.lmj.base.entity.Page;
import com.lmj.base.provider.BaseSqlProvider;
import com.lmj.base.wrapper.Wrapper;

/**
 * baseMapper
 * @author lmj
 *
 */
public interface BaseMapper<T extends Serializable> {
	
    /**
     * 根据id更新
     * @return
     */
    @UpdateProvider(type = BaseSqlProvider.class, method = "updateById")
    int updateById(T t);
    /**
     * 找到所有
     * @return
     */
    @SelectProvider(type = BaseSqlProvider.class, method = "selectList")
    List<T> selectList(@Param("wrapper")Wrapper<T> wrapper);
    /**
     * 找到所有
     * @return
     */
    @SelectProvider(type = BaseSqlProvider.class, method = "selectListSureFieldByClass")
    <R>List<R> selectListSureFieldByClass(@Param("wrapper")Wrapper<T> wrapper,@Param("returnClass")Class<?> returnClass);
    /**
     * 根据id删除
     * @return
     */
    @DeleteProvider(type = BaseSqlProvider.class, method = "deleteById")
    int deleteById(Serializable id);
    /**
     * id
     * @param id
     * @return
     */
    @SelectProvider(type = BaseSqlProvider.class, method = "selectById")
    T selectById(Serializable id);
    /**
     * 返回固定class
     * @param <R>
     */
    @SelectProvider(type = BaseSqlProvider.class, method = "selectByIdSureFieldByClass")
    <R>R selectByIdSureFieldByClass(@Param("id")Serializable id,@Param("returnClass")Class<?> returnClass);
    /**
     * 添加
     */
    @InsertProvider(type = BaseSqlProvider.class, method = "insert")
    int insert(T t);
    /**
     * 追加 
     * update user set money=money+#{money}
     */
    @UpdateProvider(type = BaseSqlProvider.class, method = "updateAppendById")
    int updateAppendById(T t);
    
    /**
     * 根据条件找到条数  count(*)
     * @param wrapper
     * @return
     */
    @SelectProvider(type = BaseSqlProvider.class, method = "selectCount")
    Long selectCount(@Param("wrapper")Wrapper<T> wrapper);
    /**
     * 分页,只找到list
     * @param page
     * @return
     */
    @SelectProvider(type = BaseSqlProvider.class, method = "selectPage")
    List<T> selectPage(@Param("page")Page page,@Param("wrapper")Wrapper<T> wrapper);
    /**
     * 分页,只找到list
     * @param page
     * @return
     */
    @SelectProvider(type = BaseSqlProvider.class, method = "selectPageSureFieldByClass")
    <R>List<R> selectPageSureFieldByClass(@Param("page")Page page,@Param("wrapper")Wrapper<T> wrapper,@Param("returnClass")Class<?> returnClass);
}
