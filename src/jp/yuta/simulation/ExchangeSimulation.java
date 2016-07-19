package jp.yuta.simulation;

import jp.yuta.model.Market;
import jp.yuta.view.ViewManager;

import static jp.yuta.util.Const.N_STEP;

/**
 * サービス交換まで含めたシミュレーション
 * 市場シミュレーション→交換→再評価→市場シミュレーション を繰り返す。
 * Created by yutakase on 2016/06/24.
 */
public class ExchangeSimulation extends Simulation {

    private Market market;
    private int serviceId;
    // 市場シミュレーションを行うクラス
    private MarketSimulation marketSimulation;


    public ExchangeSimulation(int serviceId, Market market, ViewManager viewManager) {
        this.market = market;
        this.serviceId = serviceId;
        this.marketSimulation = new MarketSimulation(this.serviceId, this.market, viewManager);
    }

    @Override
    public void init() {
    }

    @Override
    public void step() {
        System.out.println("ExchangeSimulation : " + this.stepCount);

        // 価格決定シミュレーション
        this.marketSimulation.mainLoop();
        // exchange

        // 能力上昇
        this.market.updateOperandResource(this.serviceId);
        // 評価再計算
        this.market.updateScore(this.serviceId);
        // 評価伝播
        this.market.propagateScore(this.serviceId);
    }

    @Override
    public boolean isSimulationFinish() {
        return this.stepCount < N_STEP;
    }


    @Override
    public void close() {

    }

    public void test() {
        this.marketSimulation.test();
    }
}
