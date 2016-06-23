package jp.yuta.model;

import java.awt.*;
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

    private Status status;

    // 移動する際、単位距離あたりどれだけコストがかかるか
    private double moveCost;
    // 色
    private Color color;

    public Actor(int[] pos, int id) {
        this.id = id;
        this.pos = pos;
        // 若いIDのActorが提供者
        boolean isProvider = this.id < N_Provider;
        this.status = new Status(isProvider);
        // 移動コスト
        this.moveCost = generateRandomGaussian(MOVE_COST_MU, MOVE_COST_SD);
        // 色を決定
        this.color = colorList.get(this.id % colorList.size());
    }

    /**
     * Providerに対する価値を再計算
     *
     * @param provider 再計算する対象のProvider
     */
    public void updateValue(Actor provider) {
        // 距離や評価から、得られる価値のリストを更新
        double operantResource, score, price, dist;

        operantResource = provider.getOperantResource();
        score = this.status.getScoreList().get(provider.getId());
        price = provider.getPrice();
        dist = calcDist(this.pos, provider.getPos());

        this.status.getValueList().set(provider.getId(), this.calcValue(operantResource, score, price, dist));
    }

    /**
     * 最も価値の得られるProviderを再選択
     */
    public void updateSelectProvider() {
        // 最も価値を得られるProviderと同じ色に設定
        this.status.setSelectProviderId(getMaxValueIndex(this.status.getValueList()));
        if (this.status.getSelectProviderId() == -1) {
            this.color = Color.lightGray;
        } else {
            this.color = colorList.get(this.status.getSelectProviderId());
        }
    }

    /**
     * 価値を計算
     *
     * @param operantResource Providerの能力
     * @param score           Providerへの評価
     * @param price           サービスの価格
     * @param dist            距離
     * @return 価値
     */
    private double calcValue(double operantResource, double score, double price, double dist) {
        return operantResource + score - price - dist * this.moveCost;
    }

    /**
     * 売上を計算、最高売上であれば価格を保存
     *
     * @return 売上
     */
    public int calcPayoff() {
        return this.status.calcPayoff();
    }

    public void resetPrice() {
        this.status.resetPrice();
    }

    /**
     * 引数のリストの中から最大値のインデックスを返す
     *
     * @param list リスト
     * @return 最大値のインデックス
     */
    private static int getMaxValueIndex(List<Double> list) {
        double max = 0;
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

    // test
    public double getMaxValue() {
        int id = getMaxValueIndex(this.status.getValueList());
        if (id == -1) {
            return 0;
        } else {
            return this.status.getValueList().get(id);
        }
    }

    public int getId() {
        return this.id;
    }

    public Color getColor() {
        return this.color;
    }

    public int[] getPos() {
        return this.pos;
    }

    public boolean isProvider() {
        return this.status.isProvider();
    }

    public double getOperantResource() {
        return this.status.getOperantResource();
    }

    public void setPrice(int price) {
        this.status.setPrice(price);
    }

    public int getPrice() {
        return this.status.getPrice();
    }

    public int getSelectProviderId() {
        return this.status.getSelectProviderId();
    }

    public void setnConsumer(int nConsumer) {
        this.status.setnConsumer(nConsumer);
    }

    public int getBestPrice() {
        return this.status.getBestPrice();
    }

    public int getBestPayoff() {
        return this.status.getBestPayoff();
    }

    public int getBestNConsumer() {
        return this.status.getBestNConsumer();
    }

    public void setChangePrice(boolean changePrice) {
        this.status.setChangePrice(changePrice);
    }

    public boolean isChangePrice() {
        return this.status.isChangePrice();
    }
}
