package jp.yuta.model;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/02.
 */
public abstract class AbstractActor {

    private int[] pos = new int[DIM];

    public AbstractActor(int[] pos) {
        this.pos = pos;
    }

    public int[] getPos(){
        return pos;
    }

}
