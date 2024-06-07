package com.zytoune.dailygame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {
    COVER_SMALL("cover_small","90 x 128", "Fit"),
    SCREENSHOT_MED("screenshot_med","569 x 320", "Lfill, Center gravity"),
    COVER_BIG("cover_big","264 x 374", "Fit"),
    LOGO_MED("logo_med","284 x 160", "Fit"),
    SCREENSHOT_BIG("screenshot_big","889 x 500", "Lfill, Center gravity"),
    SCREENSHOT_HUGE("screenshot_huge","1280 x 720", "Lfill, Center gravity"),
    THUMB("thumb","90 x 90", "Thumb, Center gravity"),
    MICRO("micro","35 x 35", "Thumb, Center gravity"),
    P720("720p","1280 x 720", "Fit, Center gravity"),
    P1080("1080p","1920 x 1080", "Fit, Center gravity");

    private final String name;
    private final String size;
    private final String extra;
}

