package jp.yuta.view;

import jp.yuta.model.Actor;

import javax.swing.*;

import java.awt.*;
import java.util.List;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/02.
 */
public class BaseApplet extends JApplet {

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
        int[] pos;
        for (Actor actor : actors) {
            pos = actor.getPos();
            buffer.setColor(actor.getColor());
            if (actor.isProvider()) {
                buffer.fillRect(pos[0] * CANVAS_RATE, pos[1] * CANVAS_RATE, CANVAS_RATE*2, CANVAS_RATE*2);
            } else {
                buffer.fillOval(pos[0] * CANVAS_RATE, pos[1] * CANVAS_RATE, CANVAS_RATE, CANVAS_RATE);
            }
        }
        for(int i = 0; i < N_Provider; i++){
            pos = actors.get(i).getPos();
            buffer.setColor(Color.black);
            buffer.drawRect(pos[0] * CANVAS_RATE, pos[1] * CANVAS_RATE, CANVAS_RATE*2, CANVAS_RATE*2);
        }
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}
