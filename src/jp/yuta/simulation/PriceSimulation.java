package jp.yuta.simulation;

import jp.yuta.model.Actor;
import jp.yuta.model.Market;
import jp.yuta.view.ViewManager;

import static jp.yuta.util.Const.*;

/**
 * 価格シミュレーションクラス
 * あるProviderにおいて売上が最高となる価格をシミュレーションする。
 * Created by yutakase on 2016/07/15.
 */
public class PriceSimulation extends Simulation {

    private Market market;
    // シミュレーションするProvider
    private Actor provider;
    // (変動する)価格
    private int price;
    private int serviceId;
    private ViewManager viewManager;

    public PriceSimulation(Market market, Actor provider, int serviceId, ViewManager viewManager) {
        this.market = market;
        this.provider = provider;
        this.serviceId = serviceId;
        this.viewManager = viewManager;
    }

    @Override
    public void init() {
        // 価格初期化
        this.price = MIN_PRICE;
        provider.resetBest(this.serviceId);
    }

    @Override
    public void step() {
        // 価格更新
        this.market.updatePrice(this.provider, this.price, this.serviceId);
//        this.viewManager.callRepaint(this.serviceId);
        price += DELTA_PRICE;

        try {
            Thread.sleep(STEP_SLEEP_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isSimulationFinish() {
        return price < MAX_PRICE;
    }

    @Override
    public void close() {

    }
}
