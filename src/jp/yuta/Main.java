package jp.yuta;

import jp.yuta.simulation.MarketSimulation;
import jp.yuta.view.AppletManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yutakase on 2016/06/02.
 */
public class Main {

    private static int N_TEST = 1;

    private static MarketSimulation marketSimulation;
    private static List<Integer> countList = new ArrayList<>(N_TEST);

    public static void main(String[] args) {
        // Applet初期設定
        AppletManager.initFrame();
        simulation();
//        test();
    }

    private static void simulation(){
        marketSimulation = new MarketSimulation();
        AppletManager.setActors(marketSimulation.getActors());
        for(int i = 0; i < 1;i++){
            marketSimulation.mainLoop();
            // exchange
            // recalc score
        }
        marketSimulation.test();
    }

    private static void test(){
        for (int i = 0; i < N_TEST; i++) {
            marketSimulation = new MarketSimulation();
            AppletManager.setActors(marketSimulation.getActors());
            // simulation start
            marketSimulation.mainLoop();
            countList.add(marketSimulation.getStepCount());
            System.out.println(i + ":" + marketSimulation.getStepCount());
        }

        int sum = countList.stream().mapToInt(Integer::intValue).sum();
        double ave = sum / countList.size();
        System.out.println(ave);

    }


}
