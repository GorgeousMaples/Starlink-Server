import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.common.core.utils.WrapperUtils;
import com.common.mybatis.wrapper.WhereItem;

public class WrapperTest {
    public static void main(String[] args) {
        QueryWrapper<?> wrapper = new QueryWrapper<>();
//        wrapper.eq("A", 1).and(w -> w.eq("B", 2).or(w1 -> w1.eq("C", 3)));

        WhereItem[] items = new WhereItem[]{
                WhereItem.builder().group(new WhereItem[]{
                        WhereItem.builder().column("A").operator("eq").value("1").build(),
                        WhereItem.builder().joinWay("or").group(new WhereItem[]{
                                WhereItem.builder().column("B").operator("eq").value("2").build(),
                                WhereItem.builder().joinWay("or").column("C").operator("eq").value("3").build(),
                        }).build()
                }).build(),
                WhereItem.builder().joinWay("and").column("D").operator("eq").value("4").build()
        };
        System.out.println(WrapperUtils.getWhereSentence(WhereItem.buildQueryWrapper(wrapper, items)));
    }
}
