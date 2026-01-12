package com.umc.nuvibe.domain.image.controller;

import com.umc.nuvibe.domain.image.service.ImageTagService;
import com.umc.nuvibe.domain.image.vo.ImageTag;
import com.umc.nuvibe.domain.image.vo.ImageTagCategory;
import com.umc.nuvibe.global.apiPayLoad.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@AllArgsConstructor
@Tag(name="Tag", description = "태그 검색 API")
public class ImageTagController {

    private ImageTagService imageTagService;

    @GetMapping("/find")
    @Operation(summary = "태그 찾기", description = "카테고리를 선택하여 태그 목록을 불러옵니다.")
    public Response<List<ImageTag>> findTag(
            @RequestParam ImageTagCategory category
    ) {
        return Response.ok(imageTagService.findByCategory(category));
    }

    @GetMapping("/search")
    @Operation(summary = "태그 검색", description = "검색을 통해 태그를 찾습니다.")
    public Response<List<ImageTag>> searchTag(
            @RequestParam String search
    ){
        return Response.ok(
                imageTagService.findByNameAndTagKoAndSynonyms(search)
        );
    }
}
