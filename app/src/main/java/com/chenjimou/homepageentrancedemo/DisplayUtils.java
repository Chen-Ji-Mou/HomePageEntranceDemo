package com.chenjimou.homepageentrancedemo;

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DisplayUtils {

    public static DisplayMetrics getDisplaySize(Context content)
    {
        return content.getResources().getDisplayMetrics();
    }
}
