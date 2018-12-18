package com.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lmj.base.anno.IdButNotExist;
import com.lmj.base.anno.TableField;
import com.lmj.base.anno.TableName;

import lombok.Data;

@TableName("user")
@Data
public class User2{

    /**
     * 
     */
    private static final long serialVersionUID = -8917241480540199531L;
    @TableField("user_name")
    private String userName;
    @IdButNotExist
    @JsonIgnore
    private Integer id;
}
