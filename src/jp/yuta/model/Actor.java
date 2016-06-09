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

    private boolean isProvider;
    // 能力
    private double operantResource;
    // 交換価格
    private int price = 0;
    private int currentPrice = 0;
    // 売上最大の価格
    private int bestPrice = 0;
    // 顧客数
    private int nConsumer = 0;
    private int bestPayoff = 0;

    // 各Providerに対する評価(能力に対する相対評価)
    private List<Double> scoreList = new ArrayList<>(N_Provider);
    // 各Providerに対する値段と距離を踏まえた価値(消費したコストに対してどれだけ価値を得られたか)
    private List<Double> valueList = new ArrayList<>(N_Provider);
    // 移動する際、単位距離あたりどれだけコストがかかるか
    private double moveCost;
    private int selectProviderId;

    // 色
    private Color color;

    public Actor(int[] pos, int id) {
        this.id = id;
        this.pos = pos;
        // 若いIDのActorが提供者
        this.isProvider = this.id < N_Provider;
        if (this.isProvider) {
            this.initProvider();
        } else {
            this.initConsumer();
        }
    }

    private void initProvider() {
        // 能力を乱数で設定
        this.operantResource = generateRandomDouble(0, 6000);
        // 交換価格を設定
        this.price = MIN_PRICE;
        this.currentPrice = this.price;
        this.bestPrice = this.price;
        // 色を決定
        this.color = colorList.get(this.id % colorList.size());

    }

    private void initConsumer() {
        // 各Providerへの評価(能力に対する相対評価)
        for (int i = 0; i < N_Provider; i++) {
            this.scoreList.add(generateRandomGaussian(0, 1000));
            this.valueList.add(0.0);
        }
        // 移動コスト
        this.moveCost = generateRandomGaussian(100, 50);
    }

    /**
     * Providerに対する価値を再計算
     * @param provider 再計算する対象のProvider
     */
    public void updateValue(Actor provider) {
        // 距離や評価から、得られる価値のリストを更新
        double operantResource, score, price, dist;

        operantResource = provider.getOperantResource();
        score = this.scoreList.get(provider.getId());
        price = provider.getPrice();
        dist = calcDist(this.pos, provider.getPos());

        this.valueList.set(provider.getId(), this.calcValue(operantResource, score, price, dist));
    }

    /**
     * 最も価値の得られるProviderを再選択
     */
    public void updateSelectProvider(){
        // 最も価値を得られるProviderと同じ色に設定
        this.selectProviderId = this.getMaxIndex(valueList);
        this.color = colorList.get(this.selectProviderId);
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
     * @return 売上
     */
    public int calcPayoff() {
        int payoff = this.nConsumer * this.price;
        if (this.bestPayoff < payoff) {
            this.bestPayoff = payoff;
            this.bestPrice = this.price;
        }
        return payoff;
    }

    /**
     * 引数のリストの中から最大値のインデックスを返す
     *
     * @param list リスト
     * @return 最大値のインデックス
     */
    private int getMaxIndex(List<Double> list) {
        double max = Double.NEGATIVE_INFINITY;
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
        return this.isProvider;
    }

    public double getOperantResource() {
        return this.operantResource;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return this.price;
    }

    public int getSelectProviderId() {
        return selectProviderId;
    }

    public void setnConsumer(int nConsumer) {
        this.nConsumer = nConsumer;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public int getCurrentPrice() {
        return this.currentPrice;
    }

    public int getBestPrice() {
        return this.bestPrice;
    }

    public int getBestPayoff() {
        return this.bestPayoff;
    }

}