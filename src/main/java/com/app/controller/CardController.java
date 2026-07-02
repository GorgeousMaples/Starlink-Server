package com.app.controller;

import com.app.domain.Card;
import com.app.service.CardService;
import com.common.core.response.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
@CrossOrigin
public class CardController {
    private final CardService cardService;

    /**
     * 获取单张卡片信息
     */
    @GetMapping("/{id}")
    public R<Card> getCard(@PathVariable String id) {
        return cardService.getCard(id);
    }

    /**
     * 获取所有卡片信息
     */
    @GetMapping("/getAll")
    public R<List<Card>> getAllCards() {
        List<Card> list = cardService.getAllCards();
        return R.success(list);
    }

    /**
     * 更新单张卡片
     */
    @PostMapping("/update")
    public R<String> updateCard(@RequestBody Card card) {
        return cardService.updateCard(card);
    }

    /**
     * 上传单张卡片
     */
    @PostMapping("/upload")
    public R<String> uploadCard(
            @RequestPart MultipartFile file,
            @RequestPart Card card
    ) {
        return cardService.uploadCard(file, card);
    }

    /**
     * 批量上传卡片
     */
    @PostMapping("/batchUpload")
    public R<String> batchUploadCards(
            @RequestPart MultipartFile[] files,
            @RequestPart List<Card> cards
    ) {
        return cardService.batchUploadCards(files, cards);
    }

    /**
     * 删除单张卡片
     */
    @DeleteMapping("/{id}")
    public R<String> deleteCard(@PathVariable String id) {
        return cardService.deleteCard(id);
    }

    /**
     * 批量删除卡片
     */
    @DeleteMapping("/batchDelete")
    public R<String> batchDeleteCards(@RequestBody List<String> ids) {
        return cardService.batchDeleteCard(ids);
    }
}
