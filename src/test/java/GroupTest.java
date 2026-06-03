import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class GroupTest {
    @Data
    @Builder
    @AllArgsConstructor
    public static class Person {
        private String name;
        private String gender;
        private String province;
    }

    public static final Map<String, Function<Person, Serializable>> PERSON_MAP = new LinkedHashMap<>(){{
        put("gender", Person::getGender);
        put("province", Person::getProvince);
        put("name", Person::getName);
    }};

    public static Function<Person, List<Serializable>> dynamicKey(String[] groups) {
        return p ->
                Arrays.stream(groups)
                        .map(group -> PERSON_MAP.get(group).apply(p))
                        .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        List<Person> list = new ArrayList<Person>() {{
            add(new Person("张三", "男", "北京市"));
            add(new Person("李四", "女", "湖北省"));
            add(new Person("王五", "男", "广东省"));
            add(new Person("赵六", "女", "湖南省"));
            add(new Person("钱七", "男", "广东省"));
            add(new Person("孙八", null, "湖南省"));
        }};

        String[] groups = {"gender", "province"};

//        List<Map<String, Serializable>> result = dynamicGroup(list, groups);
//        result.forEach(System.out::println);

        Map<List<Serializable>, List<Person>> result = list.stream().collect(Collectors.groupingBy(dynamicKey(groups)));
        result.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    public static List<Map<String, Serializable>> dynamicGroup(List<Person> persons, String[] groups) {
        List<String> fieldList = Arrays.asList(groups);
        // 动态生成分组收集器
        Collector<Person, ?, ?> collector = createDynamicCollector(fieldList);
        // 执行分组并获取嵌套 Map
        Map<Serializable, ?> groupedMap = persons
                .stream()
                .collect((Collector<? super Person, ?, ? extends Map<Serializable,?>>) collector);
        // 转换嵌套 Map 为 List<Map<String, String>>
        List<Map<String, Serializable>> resultList = new ArrayList<>();
        convertNestedMap(groupedMap, fieldList, 0, new HashMap<>(), resultList);
        return resultList;
    }

    // 递归创建动态分组收集器
    private static Collector<Person, ?, ?> createDynamicCollector(List<String> fields) {
        String field = fields.get(0);
        Function<Person, Serializable> classifier = PERSON_MAP.get(field);
        // 递归终止条件：最后一个字段时不设计下游处理器
        if (fields.size() == 1) {
            return Collectors.groupingBy(classifier);
        }
        // 递归构建下游收集器
        Collector<Person, ?, ?>  downstream = createDynamicCollector(fields.subList(1, fields.size()));
        return Collectors.groupingBy(classifier, downstream);
    }

    // 递归转换嵌套 Map 结构为平铺的 List
    private static void convertNestedMap(
            Map<?, ?> map,
            List<String> fields,
            int currentIndex,
            Map<String, Serializable> currentEntry,
            List<Map<String, Serializable>> resultList
    ) {
        String currentField = fields.get(currentIndex);
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Map<String, Serializable> newEntry = new HashMap<>(currentEntry);
            newEntry.put(currentField, entry.getKey().toString());
            // 如果是最后一个字段，则添加计数
            if (currentIndex == fields.size() - 1) {
                newEntry.put("list", entry.getValue().toString());
                resultList.add(newEntry);
            }
            // 否则递归处理下一层
            else {
                convertNestedMap(
                        (Map<?, ?>) entry.getValue(),
                        fields,
                        currentIndex + 1,
                        newEntry,
                        resultList
                );
            }
        }
    }
}
