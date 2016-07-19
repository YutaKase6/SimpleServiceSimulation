package jp.yuta;

import jp.yuta.model.Market;
import jp.yuta.simulation.ExchangeSimulation;
import jp.yuta.view.Applet.AppletManager;
import jp.yuta.view.ViewManager;

import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.Const.*;

/**
 * Created by yutakase on 2016/06/02.
 */
public class Main {

    private static Market market;

    private static List<ExchangeSimulation> exchangeSimulations = new ArrayList<>(N_SERVICE);

    private static ViewManager appletManager = new AppletManager();

    public static void main(String[] args) {
        // Applet初期設定
        appletManager.initFrame();

        market = new Market();

        appletManager.setActors(market.getActors());

        for (int serviceId = 0; serviceId < N_SERVICE; serviceId++) {
            exchangeSimulations.add(new ExchangeSimulation(serviceId, market, appletManager));
        }

        simulation();
    }

    // simulation呼び出し
    private static void simulation() {
        exchangeSimulations.stream().parallel()
                .forEach(exchangeSimulation -> {
                    exchangeSimulation.mainLoop();
                    exchangeSimulation.test();
                });
    }
}
