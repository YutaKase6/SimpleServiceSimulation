package jp.yuta.simulation;

import jp.yuta.model.Actor;
import jp.yuta.view.ViewManager;

import java.util.List;

import static jp.yuta.util.Config.N_STEP;

/**
 * Created by yutakase on 2016/06/24.
 */
public class ExchangeSimulation extends Simulation {

    private int serviceId;

    private int step = 0;
    private MarketSimulation marketSimulation;

    private ViewManager viewManager;

    public ExchangeSimulation(List<Actor> actors, int serviceId, ViewManager viewManager) {
        this.serviceId = serviceId;
        this.viewManager = viewManager;
        this.marketSimulation = new MarketSimulation(this.serviceId, actors, this.viewManager);
        viewManager.setActors(this.marketSimulation.getActors(), this.serviceId);
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
