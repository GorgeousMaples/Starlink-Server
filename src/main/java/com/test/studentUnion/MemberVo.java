package com.test.studentUnion;

import lombok.Data;

import java.util.List;

@Data
public class MemberVo {
    private Member info;
    private List<DepartmentMember> departments;

    public MemberVo(Member member, List<DepartmentMember> departments) {
        this.info = member;
        this.departments = departments;
    }

    public void print() {
        System.out.println("姓名：" + info.getName()
                + "，性别：" + info.getGender()
                + "，年级：" + info.getGrade()
                + "，专业班级：" + info.getMajorClass()
                + "，联系方式：" + info.getPhoneNumber());
        for (DepartmentMember department : departments) {
            System.out.println("部门：" + department.getDepartment()
                    + "，职位：" + department.getPosition()
                    + "，级别：" + department.getLevel());
        }
    }
}
