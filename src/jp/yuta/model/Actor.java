package jp.yuta.model;

import java.awt.*;
import java.util.*;
import java.util.List;

import static jp.yuta.util.Const.*;
import static jp.yuta.util.CalcUtil.*;

/**
 * Created by yutakase on 2016/06/02.
 */
public class Actor {

    private int id;
    // 座標
    private int[] pos;
    // 評価や能力といったサービスに関する情報、サービスの種類分
    private List<ServiceStatus> serviceStatusList = new ArrayList<>(N_SERVICE);


    public Actor(int[] pos, int id) {
        this.id = id;
        this.pos = pos;
        // ProviderかConsumerか判定し、関する情報を生成
        for (int serviceId = 0; serviceId < N_SERVICE; serviceId++) {
            boolean isProvider = (this.id / N_PROVIDER == serviceId);
            this.serviceStatusList.add(new ServiceStatus(this.id, serviceId, isProvider));
        }
    }

    /**
     * Providerに対する価値を再計算
     *
     * @param provider 再計算する対象のProvider
     */
    public void updateValue(Actor provider, int serviceId) {
        // 距離や評価から、得られる価値のリストを更新
        double dist = calcDist(this.pos, provider.getPos());
        this.serviceStatusList.get(serviceId).updateValue(provider, dist);
    }

    /**
     * 最も価値の得られるProviderを再選択
     */
    public void updateSelectProvider(int serviceId) {
        // Providerを選択
        this.serviceStatusList.get(serviceId).updateSelectProvider();
    }

    /**
     * 売上を計算、最高売上であれば価格を保存
     *
     * @return 売上
     */
    public int calcPayoff(int serviceId) {
        return this.serviceStatusList.get(serviceId).calcPayoff();
    }

    /**
     * 最大売上、最大売上価格、最大売上顧客数をリセット
     */
    public void resetBest(int serviceId) {
        this.serviceStatusList.get(serviceId).resetBest();
    }

    /**
     * 能力上昇
     *
     * @param nConsumers 顧客数
     */
    public void increseOperantResource(int nConsumers, int serviceId) {
        this.serviceStatusList.get(serviceId).increseOperantResouce(nConsumers);
    }

    /**
     * 評価再計算
     */
    public void updateScore(int serviceId) {
        this.serviceStatusList.get(serviceId).updateScoreList();
    }

    public void addFriend(Actor actor, int serviceId) {
        this.serviceStatusList.get(serviceId).addFriend(actor);
    }

    public void propagatedScore(int serviceId) {
        this.serviceStatusList.get(serviceId).propagatedScore();
    }

    public int getId() {
        return this.id;
    }

    public Color getColor(int serviceId) {
        return this.serviceStatusList.get(serviceId).getColor();
    }

    public int[] getPos() {
        return this.pos;
    }

    public boolean isProvider(int serviceId) {
        return this.serviceStatusList.get(serviceId).isProvider();
    }

    public double getOperantResource(int serviceId) {
        return this.serviceStatusList.get(serviceId).getOperantResource();
    }

    public void setPrice(int price, int serviceId) {
        this.serviceStatusList.get(serviceId).setPrice(price);
    }

    public int getPrice(int serviceId) {
        return this.serviceStatusList.get(serviceId).getPrice();
    }

    public int getSelectProviderId(int serviceId) {
        return this.serviceStatusList.get(serviceId).getSelectProviderId();
    }

    public void setnConsumer(int nConsumer, int serviceId) {
        this.serviceStatusList.get(serviceId).setnConsumer(nConsumer);
    }

    public int getBestPrice(int serviceId) {
        return this.serviceStatusList.get(serviceId).getBestPrice();
    }

    public int getBestPayoff(int serviceId) {
        return this.serviceStatusList.get(serviceId).getBestPayoff();
    }

    public int getBestNConsumer(int serviceId) {
        return this.serviceStatusList.get(serviceId).getBestNConsumer();
    }

    public List<Double> getScoreList(int serviceId) {
        return this.serviceStatusList.get(serviceId).getScoreList();
    }

    public List<Actor> getFriends(int serviceId) {
        return this.serviceStatusList.get(serviceId).getFriends();
    }
}
