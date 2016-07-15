package jp.yuta;

import jp.yuta.model.Actor;
import jp.yuta.simulation.ExchangeSimulation;
import jp.yuta.view.AppletManager;
import jp.yuta.view.ViewManager;

import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.CalcUtil.generateRandomDouble;
import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/02.
 */
public class Main {

    // Actorのリスト
    private static List<Actor> actors = new ArrayList<>(N_ACTOR);

    private static List<ExchangeSimulation> exchangeSimulations = new ArrayList<>(N_SERVICE);

    private static ViewManager appletManager = new AppletManager();

    public static void main(String[] args) {
        // Applet初期設定

        appletManager.initFrame();

        // Actorのリストを生成
        int[] pos;
        for (int i = 0; i < N_ACTOR; i++) {
            pos = new int[DIM];
            for (int j = 0; j < DIM; j++) {
                pos[j] = (int) generateRandomDouble(0, FIELD_SIZE);
            }
            actors.add(new Actor(pos, i));
        }

        for (int serviceId = 0; serviceId < N_SERVICE; serviceId++) {
            exchangeSimulations.add(new ExchangeSimulation(actors, serviceId, appletManager));
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
