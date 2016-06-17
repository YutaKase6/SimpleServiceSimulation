package jp.yuta.model;

import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.CalcUtil.*;
import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/16.
 */
public class Status {

    private boolean isProvider;

    // 能力
    private double operantResource;
    // 交換価格
    private int price = 0;
    // 売上最大の価格
    private int bestPrice = 0;
    // 顧客数
    private int nConsumer = 0;
    private int bestNConsumer = 0;
    // 最大売上
    private int bestPayoff = 0;
    private boolean isChangePrice = true;

    // 各Providerに対する評価(能力に対する相対評価)
    private List<Double> scoreList = new ArrayList<>(N_Provider);
    // 各Providerに対する値段と距離を踏まえた価値(消費したコストに対してどれだけ価値を得られたか)
    private List<Double> valueList = new ArrayList<>(N_Provider);
    // 価値最大となるProviderのID
    private int selectProviderId;


    public Status(boolean isProvider) {
        this.isProvider = isProvider;
        if(this.isProvider){
           this.initProvider();
        }else {
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
        // 各Providerへの評価(能力に対する相対評価)
        for (int i = 0; i < N_Provider; i++) {
            this.scoreList.add(generateRandomGaussian(SCORE_MU, SCORE_SD));
            this.valueList.add(0.0);
        }
    }

    /**
     * 売上を計算、最高売上であれば価格を保存
     *
     * @return 売上
     */
    public int calcPayoff() {
        int payoff = this.nConsumer * this.price;
        if (this.bestPayoff < payoff) {
            this.bestPayoff = payoff;
            this.bestPrice = this.price;
            this.bestNConsumer = this.nConsumer;
        }
        return payoff;
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

    public void setSelectProviderId(int selectProviderId) {
        this.selectProviderId = selectProviderId;
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

    public void setChangePrice(boolean changePrice) {
        this.isChangePrice = changePrice;
    }

    public boolean isChangePrice() {
        return isChangePrice;
    }

    public List<Double> getScoreList() {
        return this.scoreList;
    }

    public List<Double> getValueList() {
        return this.valueList;
    }
}
