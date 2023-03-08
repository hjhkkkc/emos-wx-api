package com.example.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@ApiModel
public class InsertDeptForm {
    @NotBlank
    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{3,15}$")
    private String deptName;
}