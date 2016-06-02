package jp.yuta;

import jp.yuta.model.ConsumeActor;
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

    private static List<ConsumeActor> actors = new ArrayList<>(N_CONSUME_ACTOR);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame mainFrame = new JFrame("Simple Simulation");
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            mainFrame.setVisible(true);

            BaseApplet applet = new BaseApplet();
            mainFrame.add(applet);

            initActors();
            applet.setActors(actors);
        });
    }

    public static void initActors() {
        int[] pos;
        for (int i = 0; i < N_CONSUME_ACTOR; i++) {
            pos = new int[DIM];
            for (int j = 0; j < DIM; j++) {
                pos[j] = (int) generateRandomDouble(0, FIELD_SIZE);
            }
            actors.add(new ConsumeActor(pos));
        }
    }
}
