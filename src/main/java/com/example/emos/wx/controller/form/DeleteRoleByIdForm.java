package com.example.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@ApiModel
public class DeleteRoleByIdForm {
    @NotNull
    @Min(value = 3,message = "禁止删除系统内置角色")
    private Integer id;
}

