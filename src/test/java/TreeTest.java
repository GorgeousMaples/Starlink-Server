import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;

import java.util.ArrayList;
import java.util.List;

public class TreeTest {
    public static void main(String[] args) {
        // 1. 模拟数据库查询的扁平列表
        List<TreeNode<String>> nodeList = new ArrayList<>();
        nodeList.add(new TreeNode<>("1", "0", "根节点1", 1));
        nodeList.add(new TreeNode<>("2", "1", "子节点1", 2));
        nodeList.add(new TreeNode<>("3", "1", "子节点2", 1));
        nodeList.add(new TreeNode<>("4", "2", "孙子节点", 1));
        nodeList.add(new TreeNode<>("5", "0", "根节点2", 2));

        // 2. 构建树
        List<Tree<String>> treeList = TreeUtil.build(nodeList, "0",
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId());
                    tree.setParentId(treeNode.getParentId());
                    tree.setName(treeNode.getName());
                    tree.setWeight(treeNode.getWeight());
                    tree.putExtra("remark", "自定义备注");
                }
        );

        // 3. 获取根节点并打印
        Tree<String> root = treeList.get(0);
        System.out.println(root);
    }
}
