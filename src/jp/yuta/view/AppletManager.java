package jp.yuta.view;

import jp.yuta.model.Actor;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/09.
 */
public class AppletManager {

//    public static ActorApplet applet;
//    private static InfoApplet infoApplet;

    private static List<ActorApplet> applets = new ArrayList<>(N_SERVICE);
    private static List<InfoApplet> infoApplets = new ArrayList<>(N_SERVICE);

    private AppletManager() {
    }

    public static void callRepaint(int serviceId) {
        applets.get(serviceId).repaint();
        infoApplets.get(serviceId).repaint();
    }

    public static void setActors(List<Actor> actors, int serviceId) {
        applets.get(serviceId).setActors(actors);
        infoApplets.get(serviceId).setActors(actors);
    }

    public static void setNowProviderId(int providerId, int serviceId) {
        applets.get(serviceId).setNowProviderId(providerId);
        infoApplets.get(serviceId).setNowProviderId(providerId);
    }

    public static void initFrame() {
        JFrame mainFrame = new JFrame("Simple Simulation");
        for(int serviceId = 0; serviceId < N_SERVICE; serviceId++) {
            applets.add(new ActorApplet(serviceId));
            infoApplets.add(new InfoApplet(serviceId));
        }

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainFrame.setVisible(true);
        mainFrame.setLayout(null);

        for(int i = 0; i < N_SERVICE; i++) {
            mainFrame.add(applets.get(i));
            mainFrame.add(infoApplets.get(i));
        }

        for(int i = 0; i < N_SERVICE; i++) {
            setBoundsOnGrid(applets.get(i), 0, i);
            setBoundsOnGrid(infoApplets.get(i), 1, i);
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
