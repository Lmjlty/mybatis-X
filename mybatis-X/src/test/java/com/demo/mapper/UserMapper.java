package com.demo.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.demo.entity.User;
import com.demo.entity.User2;
import com.lmj.base.mapper.BaseMapper;

@Mapper
public interface UserMapper extends BaseMapper<User>{
    
//    @Insert("insert into user (user_name) values (#{userName})")
//    int insertTwo(User user);
	
	  void insertBatch(@Param("list")List<User> users);
	  
	  @Select("select id,user_name from user")
	  List<User2> selectAll();
}
