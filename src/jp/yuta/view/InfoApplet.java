package jp.yuta.view;

import jp.yuta.model.Actor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static jp.yuta.util.Config.CANVAS_SIZE;
import static jp.yuta.util.Config.N_Provider;

/**
 * Created by yutakase on 2016/06/06.
 */
public class InfoApplet extends JApplet {

    private List<Actor> actors;

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
        for (int i = 0; i < N_Provider; i++) {
            Actor actor = this.actors.get(i);
            buffer.setColor(Color.black);
            buffer.fillRect(100, 100 + i * 20, 10, 10);
            buffer.drawString("" + actor.getOperantResource(), 120, 110 + i * 20);
            buffer.setColor(actor.getColor());
            buffer.fillRect(102, 102 + i * 20, 6, 6);
        }
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}
