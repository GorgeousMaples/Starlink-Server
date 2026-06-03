package com.test.studentUnion;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.List;

@Data
public class Member {
    @ExcelProperty(value = "姓名")
    private String name;

    @ExcelProperty(value = "性别")
    private String gender;

    @ExcelProperty(value = "年级")
    private String grade;

    @ExcelProperty(value = "专业班级")
    private String majorClass;

    @ExcelProperty(value = "联系方式")
    private String phoneNumber;
}
