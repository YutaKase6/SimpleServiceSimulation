package jp.yuta.view.Applet;

import jp.yuta.model.Actor;
import jp.yuta.view.ViewManager;

import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/09.
 */
public class AppletManager extends ViewManager {

    private static List<ActorApplet> applets = new ArrayList<>(N_SERVICE);
    private static List<InfoApplet> infoApplets = new ArrayList<>(N_SERVICE);

    public AppletManager() {
    }

    @Override
    public void callRepaint(int serviceId) {
        applets.get(serviceId).repaint();
        infoApplets.get(serviceId).repaint();
    }

    @Override
    public void setActors(List<Actor> actors) {
        for (int i = 0; i < N_SERVICE; i++) {
            applets.get(i).setActors(actors);
            infoApplets.get(i).setActors(actors);
        }
    }

    @Override
    public void setNowProviderId(int providerId, int serviceId) {
        applets.get(serviceId).setNowProviderId(providerId);
        infoApplets.get(serviceId).setNowProviderId(providerId);
    }

    @Override
    public void initFrame() {
        JFrame mainFrame = new JFrame("Simple Simulation");
        JTabbedPane tabbedPane = new JTabbedPane();
        for (int serviceId = 0; serviceId < N_SERVICE; serviceId++) {
            applets.add(new ActorApplet(serviceId));
            infoApplets.add(new InfoApplet(serviceId));
        }
        JPanel p = new JPanel();
        for (int i = 0; i < N_SERVICE; i++) {
            p.add(applets.get(i));
            p.add(infoApplets.get(i));
//            tabbedPane.addTab(""+i, applets.get(i));
//            tabPanels.get(i).add(infoApplets.get(i));
        }
        mainFrame.add(p);
//        tabbedPane.addTab("hoge", p);
//        mainFrame.add(tabbedPane, BorderLayout.CENTER);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainFrame.setVisible(true);
//        mainFrame.setLayout(null);

        for (int i = 0; i < N_SERVICE; i++) {
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

