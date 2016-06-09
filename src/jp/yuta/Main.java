package jp.yuta;

import jp.yuta.model.Market;
import jp.yuta.view.AppletManager;

/**
 * Created by yutakase on 2016/06/02.
 */
public class Main {

    private static Market market;

    public static void main(String[] args) {
        market = new Market();
        // Applet初期設定
        AppletManager.initFrame(market);
        // simulation start
        Simulation.simulate(market);
    }
}
