package com.app.service;

import com.app.config.FileProperties;
import com.app.domain.Card;
import com.app.mapper.CardMapper;
import com.common.core.response.R;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final FileProperties fileProperties;
    private final CardMapper cardMapper;

    /**
     * 上传文件
     * @param file 原始文件
     * @param type 子目录
     */
    public R<String> uploadImage(MultipartFile file, String type, String name) {
        // 去掉危险字符
//        subDir = subDir.replaceAll("[\\\\/:*?\"<>|]", "");
//
//        if (!subDir.isEmpty() && !subDir.endsWith("/")) {
//            subDir += "/";
//        }
//

        if (!type.equals("card") && !type.equals("emoji")) {
            // 类型错误
            return R.error("错误的图片类型");
        }

        String uploadDir = fileProperties.getImagePath();
        File dir = new File(uploadDir + type + "/"); // 拼接路径

        // 创建目录
        if (!dir.exists()) {
            dir.mkdirs();
        }

//        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        String fileName = file.getOriginalFilename();
        File dest = new File(dir, fileName);

        try {
            // 将图片存到磁盘中
            file.transferTo(dest);
            // 将路径信息存到数据库中
            if (type.equals("card")) {
                String path = "images/" + type + "/" + fileName;
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

    /**
     * 返回所有的图片
     */
    public List<Card> getAllCards() {
        return cardMapper.selectList(null);
    }
}
