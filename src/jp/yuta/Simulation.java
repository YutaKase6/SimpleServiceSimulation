package jp.yuta;

import jp.yuta.model.Actor;
import jp.yuta.model.Market;
import jp.yuta.view.AppletManager;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/08.
 */
public class Simulation {

    private Simulation() {
    }

    public static void simulate(Market market) {
        init(market);
        AppletManager.callRepaint();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        market.getActors().stream().filter(Actor::isProvider).forEach(provider -> simulatePrice(market, provider));
    }

    private static void init(Market market) {
        market.getActors().stream().parallel().filter(Actor::isProvider).forEach(provider -> market.updatePrice(provider, MIN_PRICE));
    }

    private static void simulatePrice(Market market, Actor provider) {
        int price = MIN_PRICE;
        // 現在の価格を保存(価格シミュレーションの後、他のProviderのシミュレーションのために求めた価格から戻す必要があるため)
        provider.setCurrentPrice(provider.getBestPrice());

        // 売上が最大となる価格をシミュレーション
        while (price <= MAX_PRICE) {

            market.updatePrice(provider, price);

            AppletManager.callRepaint();
            price += DELTA_PRICE;

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 他のProviderのシミュレーションのために、価格を計算前の価格に戻す
        provider.setPrice(provider.getCurrentPrice());
    }
}
