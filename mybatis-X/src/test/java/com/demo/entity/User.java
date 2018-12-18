package com.demo.entity;

import java.io.Serializable;

import com.lmj.base.anno.Id;
import com.lmj.base.anno.TableField;
import com.lmj.base.anno.TableName;

import lombok.Data;

@Data
@TableName("user")
public class User implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = -8917241480540199531L;
    @Id
    private Integer id;
    @TableField("user_name")
    private String userName;
    
    private String pwd;
    
    
}