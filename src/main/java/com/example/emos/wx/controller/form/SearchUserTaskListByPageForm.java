package com.example.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
@Data
@ApiModel("查询审批任务分页列表")
public class SearchUserTaskListByPageForm {
    @NotBlank(message = "type不能为空")
    private String type;

    @NotNull(message = "page不能为空")
    @Min(value = 1, message = "page不能小于1")
    private Integer page;

    @NotNull(message = "length不能为空")
    @Range(min = 1, max = 60,message = "length范围要在1~60之间")
    private Integer length;

}
