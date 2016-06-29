package jp.yuta.simulation;

import jp.yuta.model.Actor;
import jp.yuta.view.AppletManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jp.yuta.util.CalcUtil.generateRandomDouble;
import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/04.
 */
public class MarketSimulation extends Simulation {

    // Actorのリスト
    private List<Actor> actors = new ArrayList<>(N_ACTOR);
    // 各Providerの価格がシミュレーション前後で変化したかどうかのフラグのマップ(Key:ProviderのID)、シミュレーションの終了判定に用いる
    private Map<Integer, Boolean> isPriceChangedMap = new HashMap<>(N_PROVIDER);

    public MarketSimulation() {
        // Actorのリストを生成
        int[] pos;
        for (int i = 0; i < N_ACTOR; i++) {
            pos = new int[DIM];
            for (int j = 0; j < DIM; j++) {
                pos[j] = (int) generateRandomDouble(0, FIELD_SIZE);
            }
            this.actors.add(new Actor(pos, i));
        }
        // Provider全員の価格を初期化
        this.actors.stream().parallel()
                .filter(Actor::isProvider)
                .forEach(actor -> this.updatePrice(actor, 0));
    }

    @Override
    public void init() {
        // シミュレーションによる価格変化フラグのマップを初期化
        this.actors.stream().parallel()
                .filter(Actor::isProvider)
                .forEach(actor -> this.isPriceChangedMap.put(actor.getId(), true));
    }

    @Override
    public void step() {
        // 価格をMIN_PRICEからMAX_PRICEまで変更させるシミュレーション
        this.actors.stream()
                .filter(Actor::isProvider)
                .forEach(this::simulatePrice);
        // Provider全員の価格を更新
        this.actors.stream().parallel()
                .filter(Actor::isProvider)
                .forEach(provider -> this.updatePrice(provider, provider.getBestPrice()));
        AppletManager.setNowProviderId(-1);
        AppletManager.callRepaint();
    }

    @Override
    public boolean isSimulationFinish() {
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
        AppletManager.setNowProviderId(provider.getId());
        // 現在の価格を保存(価格シミュレーションの後、他のProviderのシミュレーションのために求めた価格からシミュレーション前の価格に戻す必要があるため)
        int currentPrice = provider.getBestPrice();
        provider.resetBest();
        this.isPriceChangedMap.put(provider.getId(), true);
        // 売上が最大となる価格をシミュレーション
        int price = MIN_PRICE;
        while (price <= MAX_PRICE) {
            this.updatePrice(provider, price);
            AppletManager.callRepaint();
            price += DELTA_PRICE;

            try {
                Thread.sleep(STEP_SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 他のProviderのシミュレーションのために、価格を計算前の価格に戻す
        provider.setPrice(currentPrice);
        if (Math.abs(provider.getBestPrice() - currentPrice) <= PRICE_THRESHOLD) {
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
        provider.setPrice(newPrice);
        // Consumer全員の価値を再計算
        this.updateConsumersValues();
        // Providerに対するConsumerの人数を計算
        provider.setnConsumer(this.countConsumer(provider));
        // Consumerの人数と価格から売上を計算
        provider.calcPayoff();
    }

    /**
     * Consumer全員の価値を再計算
     */
    private void updateConsumersValues() {
        // Consumer全員の各Providerに対する価値を再計算
        this.actors.stream().parallel()
                .filter(Actor::isProvider)
                .forEach(provider -> this.actors.stream().parallel()
                        .filter(actor -> !actor.isProvider())
                        .forEach(actor -> actor.updateValue(provider))
                );
        // 各Consumerが最も価値を得られるProviderを選択
        this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider())
                .forEach(Actor::updateSelectProvider);
    }

    /**
     * 引数にとったProviderに対するConsumerの人数を数える
     *
     * @param provider Provider
     * @return Consumerの人数
     */
    private int countConsumer(Actor provider) {
        if (!provider.isProvider()) return -1;
        int id = provider.getId();
        long count = this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider())
                .filter(actor -> actor.getSelectProviderId() == id)
                .count();
        return (int) count;
    }

    public void updateOperandResource() {
        this.actors.stream().parallel()
                .filter(Actor::isProvider)
                .forEach(actor -> actor.increseOperantResource(this.countConsumer(actor)));
    }

    public void updateScore() {
        this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider())
                .forEach(Actor::reCalcScore);
    }

    public List<Actor> getActors() {
        return this.actors;
    }

    // debug用
    public void test() {
        this.actors.stream()
                .filter(Actor::isProvider)
                .forEach(actor -> System.out.println(this.countConsumer(actor)));
        long count = this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider())
                .filter(actor -> actor.getSelectProviderId() == -1)
                .count();
        System.out.println("No exchange Actors :" + count);
    }
}
