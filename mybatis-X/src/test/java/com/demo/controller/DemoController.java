package com.demo.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.entity.User2;
import com.demo.mapper.UserMapper;


@RestController
public class DemoController {
    
    @Autowired
    private UserMapper userMapper;
    
    @RequestMapping("test")
    @Transactional
    public Object test(String value) {
		//demo介绍
        //wrapper用法和mybatis-plus差不多,大家可以自己去百度一下,这里就不介绍这个了
        //selectById,deleteById...等等都差不多,也不介绍了
        /**
         * (1)着重介绍下selectByIdSureFieldByClass,User2相当于一个DTO,你只要在returnClass的地方输入即可完成
         * 部分列的查询,在一定程度上优化了sql查询,也节约了重复编码的操作.
         * 唯一一点不足,是通过反射来完成的,具体的性能没去测试过,但是我走了缓存,一般来说不会存在问题(下面,在我本机上的测试结果,通过反射的方式,居然还要快一点~~)
         * 其余的list page等等,都是该种方式的拓展,本质是差不多的.
         * (2)再介绍下updateAppend(T t);
         * 比如一笔存钱业务,我这人特别懒,以前都是通过mybatis-plus的select方法先找出t对象,
         * 来一段硬编码:
         * int id=t.getId();
         * int oldMoney = t.getMoney;
         * t = new T();
         * t.setId(id);
         * t.setMoney=(oldMoney+100);
         * tMapper.updateById(t);
         * 这样就不用去硬编码了,但是这个方式存在极大的并发问题,所以推荐写成
         * update t set money = money + 100 where id=#{id};
         * 这种操作其实也是一种累赘,所以我也不想多写这种代码.
         * 因此updateAppend(t)可以实现如下效果,你主要写
         * int id=t.getId();
         * t = new T();
         * t.setId(id);
         * t.setMoney=(100);
         * tMapper.updateById(t);
         * 即可达到 update t set money = money + 100 where id=#{id};这样的效果.
         * 并且不需要写任何的sql语句
         * User u=new User();
         * u.setId(1);
         * u.setPwd("2");
         * userMapper.updateAppendById(u);
         */
//    	long start=System.currentTimeMillis();
//    	for(int i=0;i<10000;i++) {
//    		List<User2> user2 = userMapper.selectListSureFieldByClass(null, User2.class);
//    	}
//    	System.out.println(System.currentTimeMillis() - start);
//    	start=System.currentTimeMillis();
//    	for(int i=0;i<10000;i++) {
//    		List<User2> user2 = userMapper.selectAll();
//    	}
//    	System.out.println(System.currentTimeMillis() - start);
    	return null;
    }
}
