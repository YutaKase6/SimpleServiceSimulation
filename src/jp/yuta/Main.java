package jp.yuta;

import jp.yuta.model.Actor;
import jp.yuta.model.Market;
import jp.yuta.view.BaseApplet;
import jp.yuta.view.InfoApplet;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.Config.*;
import static jp.yuta.util.CalcUtil.*;
import static jp.yuta.util.Config.FRAME_PADDING;

/**
 * Created by yutakase on 2016/06/02.
 */
public class Main {

    private static List<Actor> actors = new ArrayList<>(N_ACTOR);
    private static Market market;
    private static BaseApplet applet;
    private static InfoApplet infoApplet;

    public static void main(String[] args) {
        initFrame();

        for (int i = 0; i < 6; i++) {
            step(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void step(int step) {
        for (Actor actor : actors) {
            if (actor.isProvider()) {
                if (actor.getId() == 0)
                    actor.setPrice(step * 1000);
            } else {
                actor.update();
            }
        }
        SwingUtilities.invokeLater(() -> applet.repaint());
        System.out.println(market.countConsumer(0));
    }

    private static void initFrame() {
        JFrame mainFrame = new JFrame("Simple Simulation");
        applet = new BaseApplet();
        infoApplet = new InfoApplet();

        market = new Market(actors);
        initActors();
        applet.setActors(actors);
        infoApplet.setActors(actors);

        SwingUtilities.invokeLater(() -> {
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            mainFrame.setVisible(true);
            mainFrame.setLayout(null);

            mainFrame.add(applet);
            mainFrame.add(infoApplet);

            setBoundsOnGrid(applet, 0, 0);
            setBoundsOnGrid(infoApplet, 0, 1);

        });
    }

    private static void initActors() {
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

    /**
     * コンポーネントの座標を引数のグリッド座標にあわせて設定
     *
     * @param component 座標を設定したいコンポーネント
     * @param column    行番号
     * @param row       列番号
     */
    private static void setBoundsOnGrid(Component component, int column, int row) {
        int x = FRAME_PADDING * (row + 1) + CANVAS_SIZE * row;
        int y = FRAME_PADDING * (column + 1) + CANVAS_SIZE * column;
        component.setBounds(x, y, CANVAS_SIZE, CANVAS_SIZE);
    }
}
