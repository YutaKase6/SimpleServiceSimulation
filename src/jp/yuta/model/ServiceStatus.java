package jp.yuta.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.CalcUtil.*;
import static jp.yuta.util.Config.*;
import static jp.yuta.util.MyColor.colorList;

/**
 * Created by yutakase on 2016/06/16.
 */
public class ServiceStatus {

    private int serviceId;
    // Providerかどうか
    private boolean isProvider;
    // 色
    private Color color;

    // 能力
    private double operantResource;
    // 交換価格
    private int price = 0;
    // 売上最大の価格
    private int bestPrice = 0;
    // 顧客数
    private int nConsumer = 0;
    // 売上最大の顧客数
    private int bestNConsumer = 0;
    // 最大売上
    private int bestPayoff = 0;

    // 各Providerに対する評価(能力に対する相対評価)
    private List<Double> scoreList = new ArrayList<>(N_PROVIDER);
    // 各Providerに対する値段と距離を踏まえた価値(消費したコストに対してどれだけ価値を得られたか)
    private List<Double> valueList = new ArrayList<>(N_PROVIDER);
    // 価値最大となるProviderのID
    private int selectProviderId;
    // 移動する際、単位距離あたりどれだけコストがかかるか
    private double moveCost;


    public ServiceStatus(int actorId, int serviceId, boolean isProvider) {
        this.serviceId = serviceId;
        this.isProvider = isProvider;
        this.color = colorList.get(actorId % colorList.size());
        if (this.isProvider) {
            this.initProvider();
        } else {
            this.initConsumer();
        }
    }

    private void initProvider() {
        // 能力を乱数で設定
        this.operantResource = generateRandomDouble(MIN_OPERANT_RESOURCE, MAX_OPERANT_RESOURCE);
        // 交換価格を設定
        this.price = MIN_PRICE;
        this.bestPrice = this.price;
    }

    private void initConsumer() {
        // 移動コストを乱数で決定
        this.moveCost = generateRandomGaussian(MOVE_COST_MU, MOVE_COST_SD);
        // 各Providerへの評価(能力に対する相対評価)
        for (int i = 0; i < N_PROVIDER; i++) {
            // 評価の初期値は0? 知らないもんは知らない。
//            this.scoreList.add(generateRandomGaussian(SCORE_MU, SCORE_SD));
            this.scoreList.add(0.0);
            this.valueList.add(0.0);
        }
    }

    /**
     * 売上を計算、最高売上であれば価格を保存
     *
     * @return 売上
     */
    public int calcPayoff() {
        // 顧客数計算(限界値:MAX_CONSUMER)
        int nConsumer = (this.nConsumer > MAX_CONSUMERS) ? MAX_CONSUMERS : this.nConsumer;
        // 売上計算
        int payoff = nConsumer * this.price;
        // 最大売上更新
        if (this.bestPayoff < payoff) {
            this.bestPayoff = payoff;
            this.bestPrice = this.price;
            this.bestNConsumer = this.nConsumer;
        }
        return payoff;
    }


    /**
     * Providerに対する価値を再計算
     *
     * @param provider 再計算する対象のProvider
     * @param dist     Providerとの距離
     */
    public void updateValue(Actor provider, double dist) {
        double value = this.calcValue(provider, dist);
        this.valueList.set(provider.getId() % N_PROVIDER, value);
    }

    /**
     * 価値を計算
     *
     * @param provider 価値計算対象Provider
     * @param dist     Providerとの距離
     * @return 価値
     */
    private double calcValue(Actor provider, double dist) {
        double operantResource = provider.getOperantResource(this.serviceId);
        double score = this.scoreList.get(provider.getId() % N_PROVIDER);
        double price = provider.getPrice(this.serviceId);

        return operantResource + score - price - (dist * this.moveCost);
    }

    /**
     * 最も価値の得られるProviderを再選択
     */
    public void updateSelectProvider() {
        this.selectProviderId = getMaxValueIndex(this.valueList);
        this.color = (this.selectProviderId == -1) ? Color.lightGray : colorList.get(this.selectProviderId);
    }

    /**
     * 最大売上、最大売上価格、最大売上顧客数をリセット
     */
    public void resetBest() {
        this.bestNConsumer = 0;
        this.bestPayoff = 0;
        this.bestPrice = 0;
    }

    /**
     * 能力上昇
     *
     * @param nConsumer 顧客数
     */
    public void increseOperantResouce(int nConsumer) {
        nConsumer = (nConsumer > MAX_CONSUMERS) ? MAX_CONSUMERS : nConsumer;
        this.operantResource += nConsumer;
        // 能力上限
        this.operantResource = (this.operantResource > MAX_OPERANT_RESOURCE) ? MAX_OPERANT_RESOURCE : this.operantResource;
    }

    /**
     * 価値再計算
     */
    public void updateScoreList() {
//        ランダムに再評価
//        for (int i = 0; i < this.scoreList.size(); i++) {
//            if (generateRandomDouble(0, 1) < RECALC_SCORE_PROBABILITY) {
//                this.scoreList.set(i, generateRandomGaussian(SCORE_MU, SCORE_SD));
//            }
//        }
        // 交換したProviderを評価
        if (this.selectProviderId != -1) {
            if (this.scoreList.get(this.selectProviderId) == 0) {
                // 初めて, 1000 or -1000 で評価
                double score = (generateRandomDouble(0, 1) > 0.5) ? 1000.0 : -1000.0;
                this.scoreList.set(this.selectProviderId, score);
            } else {
                // リピート、一定確率で評価を上下
                if (generateRandomDouble(0, 1) < RECALC_SCORE_PROBABILITY) {
                    double deltaScore = (generateRandomDouble(0, 1) > 0.5) ? 100.0 : -100.0;
                    double score = this.scoreList.get(this.selectProviderId);
                    this.scoreList.set(this.selectProviderId, score + deltaScore);
                }
            }
        }
    }

    public Color getColor() {
        return this.color;
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

    public int getBestPrice() {
        return this.bestPrice;
    }

    public int getBestPayoff() {
        return this.bestPayoff;
    }

    public int getBestNConsumer() {
        return bestNConsumer;
    }

}
