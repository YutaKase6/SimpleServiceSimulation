package jp.yuta.view;

import jp.yuta.model.AbstractActor;
import jp.yuta.model.ConsumeActor;

import javax.swing.*;

import java.awt.*;
import java.util.List;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/02.
 */
public class BaseApplet extends JApplet {

    private List<ConsumeActor> actors;

    @Override
    public void init() {
        super.init();
        setSize(CANVAS_SIZE, CANVAS_SIZE);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Dimension d = getSize();
        Image back = createImage(d.width, d.height);
        Graphics2D buffer = (Graphics2D) back.getGraphics();
        buffer.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.draw(buffer);
        g.drawImage(back, 0, 0, this);
    }

    private void draw(Graphics2D buffer) {
        for (AbstractActor actor : actors) {
            int[] pos = actor.getPos();
            buffer.fillOval(pos[0] * CANVAS_RATE, pos[1] * CANVAS_RATE, CANVAS_RATE, CANVAS_RATE);
        }
    }

    public void setActors(List<ConsumeActor> actors) {
        this.actors = actors;
    }
}
