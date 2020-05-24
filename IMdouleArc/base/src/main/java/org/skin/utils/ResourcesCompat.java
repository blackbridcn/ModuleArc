package org.skin.utils;

import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * File: ResourcesCompat.java
 * Author: yuzhuzhang
 * Create: 2020/3/21 11:46 PM
 * Description: TODO
 * -----------------------------------------------------------------
 * 2020/3/21 : Create ResourcesCompat.java (yuzhuzhang);
 * -----------------------------------------------------------------
 */
public class ResourcesCompat {

    public static Resources getResources(AssetManager assetManager,
                                         DisplayMetrics displayMetrics,
                                         Configuration configuration) {
        Resources resources= null;
        try {
            resources = new Resources(assetManager, displayMetrics, configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resources;
    }
}
