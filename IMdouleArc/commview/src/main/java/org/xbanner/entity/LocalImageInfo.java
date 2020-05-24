package org.xbanner.entity;


import androidx.annotation.DrawableRes;

/**
 *
 * describe: 本地资源图片
 */
public class LocalImageInfo extends BannerData {

    @DrawableRes
    private int bannerRes;

    public LocalImageInfo(int bannerRes) {
        this.bannerRes = bannerRes;
    }

    @Override
    public Integer getXBannerUrl() {
        return bannerRes;
    }

    @Override
    public String getContentUrl() {
        return null;
    }
}
