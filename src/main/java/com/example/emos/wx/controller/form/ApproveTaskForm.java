package com.example.emos.wx.controller.form;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Data
@ApiModel("审批任务表单")
public class ApproveTaskForm {
    @NotBlank(message = "taskId不能为空")
    private String taskId;

    @NotBlank(message = "approval不能为空")
    @Pattern(regexp = "^同意$|^不同意$",message = "approval内容错误")
    private String approval;

}
