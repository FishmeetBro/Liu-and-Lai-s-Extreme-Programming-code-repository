package com.farmland.intel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 *
 * </p>
 *
 * @author
 * @since 2022-01-26
 */
@Getter
@Setter
@TableName("sys_user")
@ApiModel(value = "User对象", description = "")
@ToString
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("电话")
    private String phone;

    private String social;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("角色")
    private String role;

    private Boolean collectFlag;

    @TableField(exist = false)
    List<String> phoneList;

    @TableField(exist = false)
    List<String> emailList;

    @TableField(exist = false)
    List<String> socialList;

    @TableField(exist = false)
    List<String> addressList;

}
