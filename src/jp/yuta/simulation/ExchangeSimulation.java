package jp.yuta.simulation;

import jp.yuta.view.AppletManager;

import static jp.yuta.util.Config.N_STEP;

/**
 * Created by yutakase on 2016/06/24.
 */
public class ExchangeSimulation extends Simulation {

    private int step = 0;
    private MarketSimulation marketSimulation;

    public ExchangeSimulation() {
        this.marketSimulation = new MarketSimulation();
        AppletManager.setActors(this.marketSimulation.getActors());
    }

    @Override
    public void init() {
    }

    @Override
    public void step() {
        // 価格決定シミュレーション
        this.marketSimulation.mainLoop();
        // exchange

        // 能力上昇
        this.marketSimulation.updateOperandResource();
        // recalc score
        this.marketSimulation.updateScore();

        this.step++;
    }

    @Override
    public boolean isSimulationFinish() {
        return this.step < N_STEP;
    }

    public void test() {
        this.marketSimulation.test();
    }
}
