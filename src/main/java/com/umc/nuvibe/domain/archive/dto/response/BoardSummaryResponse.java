package com.umc.nuvibe.domain.archive.dto.response;

import com.umc.nuvibe.domain.image.vo.ImageTag;
import java.util.List;

//바이브 톤 입구 dto
public record BoardSummaryResponse(
    String nickname,
    List<ImageTag> topTags
) {
    public static BoardSummaryResponse of(String nickname, List<ImageTag> topTags) {
        return new BoardSummaryResponse(nickname, topTags);
    }
}
