package jp.yuta.model;

import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.CalcUtil.generateRandomDouble;
import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/04.
 */
public class Market {

    // Actorのリスト
    private List<Actor> actors = new ArrayList<>(N_ACTOR);

    public Market() {
        // Actorのリストを生成
        int[] pos;
        for (int i = 0; i < N_ACTOR; i++) {
            pos = new int[DIM];
            for (int j = 0; j < DIM; j++) {
                pos[j] = (int) generateRandomDouble(0, FIELD_SIZE);
            }
            actors.add(new Actor(pos, i));
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
        System.out.println(this.countConsumer(provider) + " : " + payoff + "       " + provider.getBestPrice() + ":" + provider.getBestPayoff());
    }

    /**
     * 引数にとったProviderに対するConsumerの人数を数える
     *
     * @param provider Provider
     * @return Consumerの人数
     */
    public int countConsumer(Actor provider) {
        if (!provider.isProvider()) return -1;

        int id = provider.getId();
        long count = this.actors.stream().parallel()
                .filter(actor -> actor.getSelectProviderId() == id)
                .count();

        return (int) count;
    }

    /**
     * Consumer全員の価値を再計算
     */
    public void updateConsumersValues() {
        // Consumer全員の各Providerに対する価値を再計算
        this.actors.stream().parallel()
                .filter(Actor::isProvider)
                .forEach(
                        provider -> this.actors.stream().parallel()
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
