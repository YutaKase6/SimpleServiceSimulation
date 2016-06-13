package jp.yuta;

import jp.yuta.simulation.MarketSimulation;
import jp.yuta.view.AppletManager;

/**
 * Created by yutakase on 2016/06/02.
 */
public class Main {

    private static MarketSimulation marketSimulation;

    public static void main(String[] args) {
        marketSimulation = new MarketSimulation();
        // Applet初期設定
        AppletManager.initFrame(marketSimulation);
        // simulation start
        marketSimulation.mainLoop();
    }
}
