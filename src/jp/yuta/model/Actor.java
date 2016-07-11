package jp.yuta.model;

import java.awt.*;

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
    // 評価や能力といったサービスに関する情報
    private ServiceStatus serviceStatus;
    // 移動する際、単位距離あたりどれだけコストがかかるか
    private double moveCost;
    // 色
    private Color color;


    public Actor(int[] pos, int id) {
        this.id = id;
        this.pos = pos;
        // 若いIDのActorが提供者
        boolean isProvider = this.id < N_PROVIDER;
        // ProviderかConsumerか判定し、関する情報を生成
        this.serviceStatus = new ServiceStatus(isProvider);
        // 移動コストを乱数で決定
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
        double dist = calcDist(this.pos, provider.getPos());
        double moveCost = this.moveCost;
        this.serviceStatus.updateValue(provider, dist, moveCost);
    }

    /**
     * 最も価値の得られるProviderを再選択
     */
    public void updateSelectProvider() {
        // Providerを選択
        this.serviceStatus.updateSelectProvider();
        // 最も価値を得られるProviderと同じ色に設定(価値がすべてマイナスの場合はグレーに)
        int selectedId = this.serviceStatus.getSelectProviderId();
        this.color = (selectedId == -1) ? Color.lightGray : colorList.get(selectedId);
    }

    /**
     * 売上を計算、最高売上であれば価格を保存
     *
     * @return 売上
     */
    public int calcPayoff() {
        return this.serviceStatus.calcPayoff();
    }

    /**
     * 最大売上、最大売上価格、最大売上顧客数をリセット
     */
    public void resetBest() {
        this.serviceStatus.resetBest();
    }

    /**
     * 能力上昇
     *
     * @param nConsumers 顧客数
     */
    public void increseOperantResource(int nConsumers) {
        this.serviceStatus.increseOperantResouce(nConsumers);
    }

    /**
     * 評価再計算
     */
    public void updateScore() {
        this.serviceStatus.updateScoreList();
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
        return this.serviceStatus.isProvider();
    }

    public double getOperantResource() {
        return this.serviceStatus.getOperantResource();
    }

    public void setPrice(int price) {
        this.serviceStatus.setPrice(price);
    }

    public int getPrice() {
        return this.serviceStatus.getPrice();
    }

    public int getSelectProviderId() {
        return this.serviceStatus.getSelectProviderId();
    }

    public void setnConsumer(int nConsumer) {
        this.serviceStatus.setnConsumer(nConsumer);
    }

    public int getBestPrice() {
        return this.serviceStatus.getBestPrice();
    }

    public int getBestPayoff() {
        return this.serviceStatus.getBestPayoff();
    }

    public int getBestNConsumer() {
        return this.serviceStatus.getBestNConsumer();
    }

}
