package jp.yuta.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.Config.*;
import static jp.yuta.util.CalcUtil.*;
import static jp.yuta.util.MyColor.colorList;

/**
 * Created by yutakase on 2016/06/02.
 */
public class Actor {

    private int id;
    // 座標
    private int[] pos;

    private Market market;

    private boolean isProvider;

    private List<Double> valueList = new ArrayList<>(N_Provider);

    private double moveCost = 0;

    // 色
    private Color color;

    public Actor(int[] pos, int id, Market market) {
        this.id = id;
        this.pos = pos;
        this.market = market;
        this.isProvider = this.id < N_Provider;
        // 色を決定
        this.color = colorList.get(this.id % colorList.size());
        if (!isProvider) {
            this.color = new Color(0, 0, 0);
            // 各Providerへの評価
            for (int i = 0; i < N_Provider; i++) {
//                valueList.add(generateRandomDouble(0, MAX_SCORE));
                valueList.add(1000.0);
            }
            // 移動コスト
            moveCost = generateRandomDouble(0, MAX_MOVE_COST);
            calcScore();
            this.color = colorList.get(getMaxIndex(valueList));
        }
    }

    private void calcScore() {
        double tmpScore;
        double dist;
        for (int i = 0; i < N_Provider; i++) {
            dist = calcDist(this.pos, this.market.getActors().get(i).getPos());
            tmpScore = this.valueList.get(i) - dist * this.moveCost;
            this.valueList.set(i, tmpScore);
        }
    }

    private int getMaxIndex(List<Double> list) {
        double max = Double.MIN_VALUE;
        int maxIndex = -1;
        for (int i = 0; i < list.size(); i++) {
            double n = list.get(i);
            if (max < n) {
                max = n;
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public Color getColor() {
        return this.color;
    }

    public int[] getPos() {
        return this.pos;
    }

    public boolean isProvider() {
        return this.isProvider;
    }

    public double getMoveCost() {
        return this.moveCost;
    }
}
