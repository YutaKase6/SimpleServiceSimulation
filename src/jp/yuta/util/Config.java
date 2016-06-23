package jp.yuta.util;


/**
 * Created by yutakase on 2016/06/02.
 */
public final class Config {
    private Config(){}

    public static final int RANDOM_SEED = 19920625;

    public static final int N_SERVICE = 1;
    public static final int DIM = 2;
    public static final int N_ACTOR = 2010;
    public static final int N_Provider = 10;

    public static final int MIN_OPERANT_RESOURCE = 1000;
    public static final int MAX_OPERANT_RESOURCE = 6000;
    public static final int SCORE_MU = 0;
    public static final int SCORE_SD = 1000;
    public static final int MOVE_COST_MU = 100;
    public static final int MOVE_COST_SD = 50;
    public static final int MIN_PRICE = 0;
    public static final int MAX_PRICE = 10000;
    public static final int DELTA_PRICE = 100;
    public static final int PRICE_THRESHOLD = 100;
    public static final int MAX_CONSUMERS = 200;

    public static final int STEP_SLEEP_TIME = 0;

    public static final int WINDOW_WIDTH = 1300;
    public static final int WINDOW_HEIGHT = 660;

    public static final int FIELD_SIZE = 100;
    public static final int CANVAS_SIZE = 600;
    public static final int CANVAS_RATE = CANVAS_SIZE / FIELD_SIZE;

    public static final int FRAME_PADDING = 30;

}
