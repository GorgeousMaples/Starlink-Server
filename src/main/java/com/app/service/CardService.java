package com.app.service;

import com.app.config.FileProperties;
import com.app.domain.Card;
import com.app.mapper.CardMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.common.core.response.R;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.BatchResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final FileProperties fileProperties;
    private final CardMapper cardMapper;

    /**
     * 保存文件到磁盘中
     * @param file 文件
     * @param type 文件类型（子目录）
     * @param name 保存的文件名
     * @throws IOException
     */
    private String saveFile(MultipartFile file, String type, String name) throws IOException {
        String uploadDir = fileProperties.getImagePath();
        File dir = new File(uploadDir + type + "/"); // 拼接路径

        // 创建目录
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String extension;

        // 获取文件扩展名
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        } else {
            extension = ".png";
        }
        String fileName = name + extension;
        File dest = new File(dir, fileName);
//        file.transferTo(dest); // 将图片存到磁盘中
        // 覆盖模式，如果存在图片，则覆盖
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        String path = "images/" + type + "/" + fileName; // 保存路径

        return path;
    }

    /**
     * 获取单张卡片
     */
    public R<Card> getCard(String id) {
        Card card = cardMapper.selectById(id);
        if (card == null) {
            return R.error("卡片不存在");
        }
        return R.success(card);
    }

    /**
     * 返回所有的卡片
     */
    public List<Card> getAllCards() {
        LambdaQueryWrapper<Card> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(Card::getId); // 根据 ID 排序
        return cardMapper.selectList(lqw);
    }

    /**
     * 删除卡片
     */
    public R<String> deleteCard(String id) {
        int result = cardMapper.deleteById(id);
        if (result > 0) {
            return R.success("删除成功");
        } else {
            return R.error("删除失败，卡片不存在");
        }
    }

    /**
     * 批量删除卡片
     */
    public R<String> batchDeleteCard(List<String> ids) {
        int result = cardMapper.deleteByIds(ids);
        if (result > 0) {
            return R.success("成功删除" + result + "张卡片");
        } else {
            return R.error("卡片批量删除失败！");
        }
    }

    /**
     * 上传单张卡片
     */
    public R<String> uploadCard(MultipartFile file, Card card) {
        try {
            String path = saveFile(file, "card", card.getId());
            card.setPath(path); // 将地址改为本地磁盘中的地址
            cardMapper.insertOrUpdate(card);
            return R.success(card.getId() + "卡片上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 更新单张卡片
     */
    public R<String> updateCard(Card card) {
        try {
            card.setPath(null);
            cardMapper.insertOrUpdate(card);
            return R.success(card.getId() + "卡片更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 批量卡片
     */
    public R<String> batchUploadCards(MultipartFile[] files, List<Card> cards) {
        if (cards.size() != files.length) {
            return R.error("上传的卡片信息与图片数量不同！");
        }
        try {
            int count = files.length;
            for (int i = 0; i < count; i++) {
                MultipartFile file = files[i];
                Card card = cards.get(i);
                String path = saveFile(file, "card", card.getId());
                card.setPath(path);
            }
            // 批量存入数据库
            List<BatchResult> results = cardMapper.insertOrUpdate(cards);

            return R.success(String.format("成功上传 %d 张卡片", results.size()));
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 上传文件
     * @param file 原始文件
     * @param type 子目录
     */
    public R<String> uploadImage(MultipartFile file, String type, String name) {
        if (!type.equals("card") && !type.equals("emoji")) {
            // 类型错误
            return R.error("错误的图片类型");
        }
        try {
            // 将路径信息存到数据库中
            if (type.equals("card")) {
                String path = saveFile(file, type, name);
                Card card = Card.builder()
                        .name(name)
                        .path(path)
                        .build();
                cardMapper.insertOrUpdate(card);
            } else {
                return R.error("错误的图片类型");
            }
            return R.success("图片上传成功");

        } catch (IOException e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }
}
