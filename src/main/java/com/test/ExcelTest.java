package com.test;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExcelTest {
    @Data
    public static class WhutStudent {
        @ExcelProperty(value = "姓名")
        private String name;

        @ExcelProperty(value = "班级")
        private String className;

        @ExcelProperty(value = "综测成绩")
        private double grade;
    }

    @Data
    public static class HustStudent {
        @ExcelProperty(value = "姓名")
        private String name;

        @ExcelProperty(value = "专业")
        private String major;

        @ExcelProperty(value = "导师姓名")
        private String teacher;

        @ExcelProperty(value = "录取方式")
        private String type;
    }

    @Data
    public static class Student {
        private String name;

        private String className;

        private double grade;

        private String major;

        private String teacher;

        private String type;

        public Student(WhutStudent whutStudent, HustStudent hustStudent) {
            this.name = whutStudent.getName();
            this.grade = whutStudent.getGrade();
            this.className = whutStudent.getClassName();
            this.major = hustStudent.getMajor();
            this.teacher = hustStudent.getTeacher();
            this.type = hustStudent.getType();
        }
    }

    private static final List<WhutStudent> whutList = new ArrayList<>();
    private static final List<HustStudent> hustList = new ArrayList<>();

    public static class WhutListener extends AnalysisEventListener<WhutStudent> {
        @Override
        public void invoke(WhutStudent whutStudent, AnalysisContext analysisContext) {
            whutList.add(whutStudent);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//            for (int i = 0; i < whutList.size() && i < 10; i++) {
//                WhutStudent student = whutList.get(i);
//                System.out.println()
        }
    }

    public static class HustListener extends AnalysisEventListener<HustStudent> {
        @Override
        public void invoke(HustStudent hustStudent, AnalysisContext analysisContext) {
            hustList.add(hustStudent);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {
            Map<String, WhutStudent> whutMap = whutList.stream()
                    .collect(Collectors.toMap(WhutStudent::getName, Function.identity()));
//            Map<String, HustStudent> hustMap = hustList.stream()
//                    .collect(Collectors.toMap(HustStudent::getName, Function.identity()));
            for (HustStudent hustStudent : hustList) {
                String name = hustStudent.getName();
                if (whutMap.containsKey(name)) {
                    System.out.println(new Student(whutMap.get(name), hustStudent));
                }
            }
//            hustList.stream()
//                    .filter(hustStudent -> whutMap.containsKey(hustStudent.getName()))
//                    .forEach(hustStudent -> {
//                        System.out.println(new Student(whutMap.get(hustStudent.getName()), hustStudent));
//                    });
        }
    }

    public static void main(String[] args) {
        EasyExcel.read("D:\\2025研究生\\whut.xlsx", WhutStudent.class, new WhutListener()).sheet().doRead();
        EasyExcel.read("D:\\2025研究生\\hust.xlsx", HustStudent.class, new HustListener()).sheet().doRead();
    }
}
