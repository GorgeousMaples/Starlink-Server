package com.test.studentUnion;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class DepartmentMember {
    @ExcelProperty(value = "部门")
    private String department;

    @ExcelProperty(value = "职务")
    private String position;

    @ExcelProperty(value = "级别")
    private Integer level;

    @ExcelProperty(value = "姓名")
    private String name;
}
