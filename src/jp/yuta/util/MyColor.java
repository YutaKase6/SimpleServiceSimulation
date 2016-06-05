package jp.yuta.util;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yutakase on 2016/06/03.
 */
public class MyColor {
    public static final List<Color> colorList = Arrays.asList(
            new Color(255, 0, 0),
            new Color(255, 128, 0),
            new Color(255, 255, 0),
            new Color(128, 255, 0),
            new Color(0, 255, 0),
            new Color(0, 255, 128),
            new Color(0, 255, 255),
            new Color(0, 128, 255),
            new Color(128, 0, 255),
            new Color(255, 0, 255)
    );

    private MyColor() {
    }
}
