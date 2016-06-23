package jp.yuta.util;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yutakase on 2016/06/03.
 */
public class MyColor {

    private static final int TRANSPARENT = 255;

    public static final List<Color> colorList = Arrays.asList(
//            new Color(244, 67, 54, TRANSPARENT),         /*red*/
            new Color(233, 30, 99, TRANSPARENT),         /*pink*/
//            new Color(156, 39, 176, TRANSPARENT),        /*purple*/
            new Color(103, 58, 183, TRANSPARENT),        /*deep purple*/
//            new Color(63, 81, 181, TRANSPARENT),         /*indigo*/
            new Color(33, 150, 243, TRANSPARENT),        /*blue*/
//            new Color(3, 169, 244, TRANSPARENT),         /*light blue*/
            new Color(0, 188, 212, TRANSPARENT),         /*cyan*/
//            new Color(0, 150, 136, TRANSPARENT),         /*teal*/
            new Color(76, 175, 80, TRANSPARENT),         /*green*/
//            new Color(139, 195, 74, TRANSPARENT),        /*light green*/
            new Color(205, 220, 57, TRANSPARENT),        /*lime*/
//            new Color(255, 235, 59, TRANSPARENT),        /*yellow*/
            new Color(255, 193, 7, TRANSPARENT),         /*amber*/
//            new Color(255, 152, 0, TRANSPARENT),         /*orange*/
            new Color(255, 87, 34, TRANSPARENT),         /*deep orange*/
            new Color(121, 85, 72, TRANSPARENT),         /*brown*/
//            new Color(158, 158, 158, TRANSPARENT),       /*grey*/
//            new Color(96, 125, 139, TRANSPARENT),        /*blue grey*/
            new Color(0, 0, 0, TRANSPARENT)              /*black*/
    );

    private MyColor() {
    }
}
