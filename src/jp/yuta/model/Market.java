package jp.yuta.model;

import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.CalcUtil.calcDist;
import static jp.yuta.util.CalcUtil.generateRandomDouble;
import static jp.yuta.util.Const.*;

/**
 * 市場クラス
 * Actorのリストを持つ
 * Created by yutakase on 2016/07/15.
 */
public class Market {
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
    public void updatePrice(Actor provider, int newPrice, int serviceId) {
        // Providerの価格を変更
        provider.setPrice(newPrice, serviceId);
        // Consumer全員の価値を再計算
        this.updateConsumersValues(serviceId);
        // Providerに対するConsumerの人数を計算
        provider.setnConsumer(this.countConsumer(provider, serviceId), serviceId);
        // Consumerの人数と価格から売上を計算
        provider.calcPayoff(serviceId);
    }

    /**
     * Consumer全員の価値を再計算
     */
    private void updateConsumersValues(int serviceId) {
        // Consumer全員の各Providerに対する価値を再計算
        this.actors.stream().parallel()
                .filter(actor -> actor.isProvider(serviceId))
                .forEach(provider -> this.actors.stream().parallel()
                        .filter(actor -> !actor.isProvider(serviceId))
                        .forEach(actor -> actor.updateValue(provider, serviceId))
                );
        // 各Consumerが最も価値を得られるProviderを選択
        this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider(serviceId))
                .forEach(actor -> actor.updateSelectProvider(serviceId));
    }

    /**
     * 引数にとったProviderに対するConsumerの人数を数える
     *
     * @param provider Provider
     * @return Consumerの人数
     */
    public int countConsumer(Actor provider, int serviceId) {
        if (!provider.isProvider(serviceId)) return -1;
        int id = provider.getId() % N_PROVIDER;
        long count = this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider(serviceId))
                .filter(actor -> actor.getSelectProviderId(serviceId) == id % N_PROVIDER)
                .count();
        return (int) count;
    }

    public void updateOperandResource(int serviceId) {
        this.actors.stream().parallel()
                .filter(actor -> actor.isProvider(serviceId))
                .forEach(actor -> actor.increseOperantResource(this.countConsumer(actor, serviceId), serviceId));
    }

    public void updateScore(int serviceId) {
        this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider(serviceId))
                .forEach(actor -> actor.updateScore(serviceId));
    }

    public void propagateScore(int serviceId) {
        this.actors.stream()
                .filter(actor -> !actor.isProvider(serviceId))
                .forEach(actor -> actor.propagatedScore(serviceId));
    }

    public void createActorNetworks(int serviceId) {
        this.actors.stream().parallel()
                .filter(actor -> !actor.isProvider(serviceId))
                .forEach(actor -> this.actors.stream().parallel()
                        .filter(actor1 -> !actor1.isProvider(serviceId))
                        .filter(actor1 -> calcDist(actor.getPos(), actor1.getPos()) < FRIEND_DIST)
                        .filter(actor1 -> generateRandomDouble(0, 1) < ADD_FRIEND_PROBABILITY)
                        .forEach(actor1 -> actor.addFriend(actor1, serviceId))
                );
    }

    public List<Actor> getActors() {
        return this.actors;
    }
}
