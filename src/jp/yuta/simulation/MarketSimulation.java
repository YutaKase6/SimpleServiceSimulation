package jp.yuta.simulation;

import jp.yuta.model.Actor;
import jp.yuta.model.Market;
import jp.yuta.view.ViewManager;

import java.util.HashMap;
import java.util.Map;

import static jp.yuta.util.Config.*;

/**
 * 市場シミュレーション
 * すべてのProviderの価格の均衡をシミュレーション
 * Created by yutakase on 2016/06/04.
 */
public class MarketSimulation extends Simulation {
    private int serviceId;
    private Market market;
    // 各Providerの価格がシミュレーション前後で変化したかどうかのフラグのマップ、シミュレーションの終了判定に用いる
    // Key:ProviderId, Value:Bool
    private Map<Integer, Boolean> isPriceChangedMap = new HashMap<>(N_PROVIDER);

    private ViewManager viewManager;


    public MarketSimulation(int serviceId, Market market, ViewManager viewManager) {
        this.serviceId = serviceId;
        this.market = market;
        this.viewManager = viewManager;
        // Provider全員の価格を初期化
        this.market.getActors().stream().parallel()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(actor -> this.market.updatePrice(actor, 0, this.serviceId));
    }

    @Override
    public void init() {
        // シミュレーションによる価格変化フラグのマップを初期化
        this.market.getActors().stream().parallel()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(actor -> this.isPriceChangedMap.put(actor.getId(), true));
    }

    @Override
    public void step() {
        // 価格をMIN_PRICEからMAX_PRICEまで変更させるシミュレーション
        this.market.getActors().stream()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(this::simulatePrice);
        // Provider全員の価格を更新
        this.market.getActors().stream().parallel()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(provider -> this.market.updatePrice(provider, provider.getBestPrice(this.serviceId), this.serviceId));
        // 描画
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

    @Override
    public void close() {

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
        // 収束判定フラグ初期化
        this.isPriceChangedMap.put(provider.getId(), true);

        // シミュレーション
        PriceSimulation priceSimulation = new PriceSimulation(this.market, provider, this.serviceId, this.viewManager);
        priceSimulation.mainLoop();

        // 他のProviderのシミュレーションのために、価格を計算前の価格に戻す
        provider.setPrice(currentPrice, this.serviceId);
        // 収束判定
        if (Math.abs(provider.getBestPrice(this.serviceId) - currentPrice) <= PRICE_THRESHOLD) {
            this.isPriceChangedMap.put(provider.getId(), false);
        }
    }


    // debug用
    public void test() {
        this.market.getActors().stream()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(actor -> System.out.println(this.market.countConsumer(actor, this.serviceId)));
        long count = this.market.getActors().stream().parallel()
                .filter(actor -> !actor.isProvider(this.serviceId))
                .filter(actor -> actor.getSelectProviderId(this.serviceId) == -1)
                .count();
        System.out.println("No exchange Actors :" + count);
    }
}
