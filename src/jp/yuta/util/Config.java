package jp.yuta.util;


/**
 * Created by yutakase on 2016/06/02.
 */
public final class Config {
    private Config(){}

    public static final int DIM = 2;
    public static final int N_ACTOR = 10000;
    public static final int N_Provider = 10;

    public static final double MAX_SCORE = 1000;
    public static final double MAX_MOVE_COST = 1;

    public static final int RANDOM_SEED = 625;

    public static final int WINDOW_WIDTH = 550;
    public static final int WINDOW_HEIGHT = 550;

    public static final int FIELD_SIZE = 100;
    public static final int CANVAS_SIZE = 500;
    public static final int CANVAS_RATE = CANVAS_SIZE / FIELD_SIZE;

}
