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

//    @PostMapping("/upload")
//    public R<String> uploadImage(
//            @RequestParam MultipartFile file,
//            @RequestParam String type,
//            @RequestParam String name
//    ) {
//        return cardService.uploadImage(file, type, name);
//    }

    @PostMapping("/upload")
    public R<String> uploadCard(
            @RequestPart MultipartFile file,
            @RequestPart Card card
    ) {
        return cardService.uploadCard(file, card);
    }

    @PostMapping("/update")
    public R<String> updateCard(@RequestBody Card card) {
        return cardService.updateCard(card);
    }

    @PostMapping("/batchUpload")
    public R<String> batchUploadCards(
            @RequestPart MultipartFile[] files,
            @RequestPart List<Card> cards
    ) {
        return cardService.batchUploadCards(files, cards);
    }

    @GetMapping("/getAll")
    public R<List<Card>> getAllCards() {
        List<Card> list = cardService.getAllCards();
        return R.success(list);
    }
}
