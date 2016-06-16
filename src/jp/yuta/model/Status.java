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
    // 移動する際、単位距離あたりどれだけコストがかかるか
    private double moveCost;
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
//         色を決定
//        this.color = colorList.get(this.id % colorList.size());
    }

    private void initConsumer() {
        // 各Providerへの評価(能力に対する相対評価)
        for (int i = 0; i < N_Provider; i++) {
            this.scoreList.add(generateRandomGaussian(SCORE_MU, SCORE_SD));
            this.valueList.add(0.0);
        }
        // 移動コスト
        this.moveCost = generateRandomGaussian(MOVE_COST_MU, MOVE_COST_SD);
    }
}
