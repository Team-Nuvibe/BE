package com.umc.nuvibe.domain.tribe.service.scrapedImage;

import com.umc.nuvibe.domain.tribe.dto.request.ScrapedImageToggleReq;
import com.umc.nuvibe.domain.tribe.dto.response.ScrapedImageToggleRes;

public interface ScrapedImageService {

    ScrapedImageToggleRes toggleScrapedImage(Long userId, ScrapedImageToggleReq req);
}
