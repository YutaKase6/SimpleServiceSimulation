package jp.yuta.simulation;

import jp.yuta.model.Actor;
import jp.yuta.view.AppletManager;

import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.CalcUtil.generateRandomDouble;
import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/04.
 */
public class MarketSimulation extends Simulation {

    // Actorのリスト
    private List<Actor> actors = new ArrayList<>(N_ACTOR);

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
        // actorの価値を計算(初期値)
        this.actors.stream().parallel()
                .filter(Actor::isProvider)
                .forEach(provider -> this.updatePrice(provider, MIN_PRICE));
    }

    @Override
    public void step() {
        // Provider全員の価格を更新
        this.actors.stream().parallel()
                .filter(Actor::isProvider)
                .forEach(provider -> this.updatePrice(provider, provider.getBestPrice()));
        // 価格をMIN_PRICEからMAX_PRICEまで変更させるシミュレーション
        this.actors.stream()
                .filter(Actor::isProvider)
                .forEach(this::simulatePrice);
        AppletManager.setProviderId(-1);
        AppletManager.callRepaint();
    }

    @Override
    public boolean isSimulationFinish() {
//        String input = JOptionPane.showInputDialog("next?");
//        return input.equals("y");
        for (Actor provider : this.actors) {
            if (provider.isProvider() && provider.isChangePrice()) {
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
        AppletManager.setProviderId(provider.getId());
        int price = MIN_PRICE;
        // 現在の価格を保存(価格シミュレーションの後、他のProviderのシミュレーションのために求めた価格からシミュレーション前の価格に戻す必要があるため)
        int currentPrice = provider.getBestPrice();
        provider.setChangePrice(true);
        // 売上が最大となる価格をシミュレーション
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
        if (provider.getBestPrice() == currentPrice) {
            provider.setChangePrice(false);
        }
    }

    /**
     * Providerの価格を、引数にとった価格に更新し、顧客全員の価値を再計算
     *
     * @param provider 価格を変更したいProvider
     * @param newPrice 新しい価格
     */
    public void updatePrice(Actor provider, int newPrice) {
        // Providerの価格を変更
        provider.setPrice(newPrice);
        // Consumer全員の価値を再計算
        this.updateConsumersValues();
        // Providerに対するConsumerの人数を計算
        provider.setnConsumer(this.countConsumer(provider));
        // Consumerの人数と価格から売上を計算
        int payoff = provider.calcPayoff();
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
                .filter(actor -> actor.getSelectProviderId() == id)
                .count();
        count = (count > MAX_CONSUMERS) ? MAX_CONSUMERS : count;
        return (int) count;
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

    public List<Actor> getActors() {
        return this.actors;
    }
}
