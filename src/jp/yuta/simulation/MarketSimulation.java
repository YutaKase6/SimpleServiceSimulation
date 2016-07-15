package jp.yuta.simulation;

import jp.yuta.model.Actor;
import jp.yuta.view.AppletManager;
import jp.yuta.view.ViewManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/04.
 */
public class MarketSimulation extends Simulation {
    private int serviceId;
    // Actorのリスト
    private List<Actor> actors;
    // 各Providerの価格がシミュレーション前後で変化したかどうかのフラグのマップ(Key:ProviderのID)、シミュレーションの終了判定に用いる
    private Map<Integer, Boolean> isPriceChangedMap = new HashMap<>(N_PROVIDER);

    private ViewManager viewManager;

    public MarketSimulation(int serviceId, List<Actor> actors, ViewManager viewManager) {
        this.serviceId = serviceId;
        this.actors = actors;
        this.viewManager = viewManager;
        // Provider全員の価格を初期化
        this.actors.stream().parallel()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(actor -> this.updatePrice(actor, 0));
    }

    @Override
    public void init() {
        // シミュレーションによる価格変化フラグのマップを初期化
        this.actors.stream().parallel()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(actor -> this.isPriceChangedMap.put(actor.getId(), true));
    }

    @Override
    public void step() {
        // 価格をMIN_PRICEからMAX_PRICEまで変更させるシミュレーション
        this.actors.stream()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(this::simulatePrice);
        // Provider全員の価格を更新
        this.actors.stream().parallel()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(provider -> this.updatePrice(provider, provider.getBestPrice(this.serviceId)));
        this.viewManager.setNowProviderId(-1, this.serviceId);
        this.viewManager.callRepaint(this.serviceId);
    }

    @Override
    public boolean isSimulationFinish() {
        if (this.stepCount >= MAX_MARKET_SIMULATION_STEP) return false;
        // Mapの全要素を走査、すべての要素がfalseだったらシミュレーションを終了する
        for (Map.Entry<Integer, Boolean> entry : this.isPriceChangedMap.entrySet()) {
            if (entry.getValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Providerの価格を変更するシミュレーション
     *
     * @param provider
     */
    private void simulatePrice(Actor provider) {
        this.viewManager.setNowProviderId(provider.getId(), this.serviceId);
        // 現在の価格を保存(価格シミュレーションの後、他のProviderのシミュレーションのために求めた価格からシミュレーション前の価格に戻す必要があるため)
        int currentPrice = provider.getBestPrice(this.serviceId);
        provider.resetBest(this.serviceId);
        this.isPriceChangedMap.put(provider.getId(), true);
        // 売上が最大となる価格をシミュレーション
        int price = MIN_PRICE;
        while (price <= MAX_PRICE) {
            this.updatePrice(provider, price);
            this.viewManager.callRepaint(this.serviceId);
            price += DELTA_PRICE;

            try {
                Thread.sleep(STEP_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 他のProviderのシミュレーションのために、価格を計算前の価格に戻す
        provider.setPrice(currentPrice, this.serviceId);
        if (Math.abs(provider.getBestPrice(this.serviceId) - currentPrice) <= PRICE_THRESHOLD) {
            this.isPriceChangedMap.put(provider.getId(), false);
        }
    }

    /**
     * Providerの価格を、引数にとった価格に更新し、顧客全員の価値を再計算
     *
     * @param provider 価格を変更したいProvider
     * @param newPrice 新しい価格
     */
    private void updatePrice(Actor provider, int newPrice) {
        // Providerの価格を変更
        provider.setPrice(newPrice, this.serviceId);
        // Consumer全員の価値を再計算
        this.updateConsumersValues();
        // Providerに対するConsumerの人数を計算
        provider.setnConsumer(this.countConsumer(provider), this.serviceId);
        // Consumerの人数と価格から売上を計算
        provider.calcPayoff(this.serviceId);
    }

    /**
     * Consumer全員の価値を再計算
     */
    private void updateConsumersValues() {
        // Consumer全員の各Providerに対する価値を再計算
        this.actors.stream().parallel()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(provider -> this.actors.stream().parallel()
                        .filter(actor -> !actor.isProvider(this.serviceId))
                        .forEach(actor -> actor.updateValue(provider, this.serviceId))
                );
        // 各Consumerが最も価値を得られるProviderを選択
        this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider(this.serviceId))
                .forEach(actor -> actor.updateSelectProvider(this.serviceId));
    }

    /**
     * 引数にとったProviderに対するConsumerの人数を数える
     *
     * @param provider Provider
     * @return Consumerの人数
     */
    private int countConsumer(Actor provider) {
        if (!provider.isProvider(this.serviceId)) return -1;
        int id = provider.getId() % N_PROVIDER;
        long count = this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider(this.serviceId))
                .filter(actor -> actor.getSelectProviderId(this.serviceId) == id % N_PROVIDER)
                .count();
        return (int) count;
    }

    public void updateOperandResource() {
        this.actors.stream().parallel()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(actor -> actor.increseOperantResource(this.countConsumer(actor), this.serviceId));
    }

    public void updateScore() {
        this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider(this.serviceId))
                .forEach(actor -> actor.updateScore(this.serviceId));
    }

    public List<Actor> getActors() {
        return this.actors;
    }

    // debug用
    public void test() {
        this.actors.stream()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(actor -> System.out.println(this.countConsumer(actor)));
        long count = this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider(this.serviceId))
                .filter(actor -> actor.getSelectProviderId(this.serviceId) == -1)
                .count();
        System.out.println("No exchange Actors :" + count);
    }
}
