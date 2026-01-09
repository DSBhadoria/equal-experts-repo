package com.equalexperts.githubgistsapi.controller;

import com.equalexperts.githubgistsapi.service.GistService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class GistController {
    private final GistService gistService;

    public GistController(GistService gistService) {
        this.gistService = gistService;
    }

    @GetMapping
    public String getUserGists(@Parameter Integer page,
                               @Parameter Integer per_page) {
        return gistService.getUserGists(page, per_page);
    }
}
