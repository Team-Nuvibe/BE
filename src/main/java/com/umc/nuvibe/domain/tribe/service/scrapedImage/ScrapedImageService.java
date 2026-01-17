package com.umc.nuvibe.domain.tribe.service.scrapedImage;

import com.umc.nuvibe.domain.tribe.dto.request.ScrapedImageSliceReq;
import com.umc.nuvibe.domain.tribe.dto.request.ScrapedImageToggleReq;
import com.umc.nuvibe.domain.tribe.dto.response.ScrapedImageToggleRes;
import com.umc.nuvibe.domain.tribe.dto.response.ScrapedImageTotalRes;

public interface ScrapedImageService {

    ScrapedImageToggleRes toggleScrapedImage(Long userId, ScrapedImageToggleReq req);

    ScrapedImageTotalRes getTotalScrapedImage(Long userId, ScrapedImageSliceReq req);
}
