package com.app.controller;

import com.app.domain.Card;
import com.app.service.ImageService;
import com.common.core.response.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/upload/image")
    public R<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam String type,
            @RequestParam String name
    ) {
        return imageService.uploadImage(file, type, name);
    }

    @GetMapping("download/cards")
    public R<List<Card>> getAllCards() {
        List<Card> list = imageService.getAllCards();
        return R.success(list);
    }
}
