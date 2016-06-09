package jp.yuta.view;

import jp.yuta.model.Market;

import javax.swing.*;

import java.awt.*;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/09.
 */
public class AppletManager {

    private static BaseApplet applet;
    private static InfoApplet infoApplet;

    private AppletManager() {
    }

    public static void callRepaint() {
        applet.repaint();
        infoApplet.repaint();
    }

    public static void initFrame(Market market) {
        JFrame mainFrame = new JFrame("Simple Simulation");
        applet = new BaseApplet();
        infoApplet = new InfoApplet();
        applet.setActors(market.getActors());
        infoApplet.setActors(market.getActors());

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        mainFrame.setVisible(true);
        mainFrame.setLayout(null);

        mainFrame.add(applet);
        mainFrame.add(infoApplet);

        setBoundsOnGrid(applet, 0, 0);
        setBoundsOnGrid(infoApplet, 0, 1);

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
