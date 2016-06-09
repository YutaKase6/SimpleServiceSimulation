package jp.yuta.view;

import jp.yuta.model.Actor;

import java.awt.*;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/06.
 */
public class InfoApplet extends AbstractApplet {

    @Override
    public void draw(Graphics2D buffer) {
        for (int i = 0; i < N_Provider; i++) {
            Actor actor = this.actors.get(i);
            buffer.setColor(Color.black);
            buffer.fillRect(100, 100 + i * 20, 10, 10);
            buffer.drawString("" + actor.getOperantResource(), 120, 110 + i * 20);
            buffer.drawString("" + actor.getPrice(), 300, 110 + i * 20);
            buffer.drawString("" + actor.getCurrentPrice(), 350, 110 + i * 20);
            buffer.drawString("" + actor.getBestPrice(), 400, 110 + i * 20);
            buffer.setColor(actor.getColor());
            buffer.fillRect(102, 102 + i * 20, 6, 6);
        }
    }
}