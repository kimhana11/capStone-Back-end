package com.example.capd.contest.controller;

import com.example.capd.contest.service.CrawlingScriptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
public class CrawlingScriptController {

    private final CrawlingScriptService crawlingScriptService;

    @GetMapping("/execute-script")
    public String executeScript() {
        CompletableFuture<Void> pythonScriptTask = CompletableFuture.runAsync(() -> crawlingScriptService.executeCrawlingScript());
        return "크롤링 시작";
    }
}