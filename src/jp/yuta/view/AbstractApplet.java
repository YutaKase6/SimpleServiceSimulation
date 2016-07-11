package jp.yuta.view;

import jp.yuta.model.Actor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/09.
 */
public abstract class AbstractApplet extends JApplet {

    public List<Actor> actors;
    public int nowProviderId = -1;
    private boolean isInit = false;
    // 描画するサービスのインデックス
    public int serviceId;

    @Override
    public void init() {
        super.init();
        setSize(CANVAS_SIZE, CANVAS_SIZE);
    }

    @Override
    public void paint(Graphics g) {
        if (!this.isInit) return;
        super.paint(g);
        Dimension d = getSize();
        Image back = createImage(d.width, d.height);
        Graphics2D buffer = (Graphics2D) back.getGraphics();
        buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.draw(buffer);
        g.drawImage(back, 0, 0, this);
    }

    public abstract void draw(Graphics2D buffer);

    public void setActors(List<Actor> actors) {
        this.actors = actors;
        this.isInit = true;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public void setNowProviderId(int nowProviderId) {
        this.nowProviderId = nowProviderId;
    }
}
