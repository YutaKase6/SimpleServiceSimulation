package jp.yuta;

import jp.yuta.model.Actor;
import jp.yuta.model.Market;
import jp.yuta.util.MyColor;
import jp.yuta.view.BaseApplet;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.Config.*;
import static jp.yuta.util.CalcUtil.*;

/**
 * Created by yutakase on 2016/06/02.
 */
public class Main {

    private static List<Actor> actors = new ArrayList<>(N_ACTOR);
    private static Market market;

    public static void main(String[] args) {
        JFrame mainFrame = new JFrame("Simple Simulation");
        BaseApplet applet = new BaseApplet();

        SwingUtilities.invokeLater(() -> {
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            mainFrame.setVisible(true);

            mainFrame.add(applet);

        });


        market = new Market(actors);
        initActors();
        applet.setActors(actors);
    }

    public static void initActors() {
        int[] pos;
        // ConsumeActor List
        for (int i = 0; i < N_ACTOR; i++) {
            pos = new int[DIM];
            for (int j = 0; j < DIM; j++) {
                pos[j] = (int) generateRandomDouble(0, FIELD_SIZE);
            }
            actors.add(new Actor(pos, i, market));
        }
    }
}
