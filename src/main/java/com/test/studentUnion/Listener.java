package com.test.studentUnion;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Listener {
    private static final String ROOT_PATH = "D:\\2022-2023学生会办公室文件\\8-团委学生会名单\\StudentSearcher\\";
    private static final String MEMBER_FILE_NAME = ROOT_PATH + "infoList.xls";
    private static final String DEPARTMENT_MEMBER_FILE_NAME = ROOT_PATH + "department.xls";

    private static final List<Member> memberList = new ArrayList<>();
    private static final List<DepartmentMember> departmentMemberList = new ArrayList<>();
    private static final Map<String, Member> memberMap;
    private static final Map<String, List<DepartmentMember>> departmentMemberMap;

    static {
        EasyExcel.read(MEMBER_FILE_NAME, Member.class, new MemberListener()).sheet().doRead();
        EasyExcel.read(DEPARTMENT_MEMBER_FILE_NAME, DepartmentMember.class, new DepartmentMemberListener()).sheet().doRead();
        memberMap = memberList.stream().collect(Collectors.toMap(Member::getName, Function.identity()));
        departmentMemberMap = departmentMemberList.stream().collect(Collectors.groupingBy(DepartmentMember::getName));
    }

    private static class MemberListener extends AnalysisEventListener<Member> {
        @Override
        public void invoke(Member member, AnalysisContext analysisContext) {
            memberList.add(member);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }
    }

    private static class DepartmentMemberListener extends AnalysisEventListener<DepartmentMember> {
        @Override
        public void invoke(DepartmentMember departmentMember, AnalysisContext analysisContext) {
            departmentMemberList.add(departmentMember);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }
    }

    public static List<MemberVo> searchByName(String name) {
        return memberList.stream()
                .filter(member -> member.getName().contains(name))
                .map(member -> new MemberVo(member, departmentMemberMap.get(member.getName())))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<MemberVo> members = searchByName("黄");
        for (MemberVo memberVo : members) {
            memberVo.print();
            System.out.println();
        }
    }
}
