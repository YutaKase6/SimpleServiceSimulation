package jp.yuta.view;

import jp.yuta.model.Actor;

import java.awt.*;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/02.
 */
public class BaseApplet extends AbstractApplet {

    @Override
    public void draw(Graphics2D buffer) {
        // ProviderはListの先頭にいるため、Providerを最後に描画するために逆順に描画、この方が見やすいってだけ。
        int[] pos;
        for (int i = this.actors.size() - 1; i > -1; i--) {
            Actor actor = this.actors.get(i);
            pos = actor.getPos();
            buffer.setColor(actor.getColor());
            if (actor.isProvider()) {
                // 座標適当、とりあえずちょっと大きく
                buffer.setColor(Color.black);
                buffer.fillRect(pos[0] * CANVAS_RATE - CANVAS_RATE, pos[1] * CANVAS_RATE - CANVAS_RATE, CANVAS_RATE * 2, CANVAS_RATE * 2);
                buffer.setColor(actor.getColor());
                buffer.fillRect(pos[0] * CANVAS_RATE - CANVAS_RATE / 2, pos[1] * CANVAS_RATE - CANVAS_RATE / 2, CANVAS_RATE, CANVAS_RATE);
            } else {
                buffer.fillOval(pos[0] * CANVAS_RATE - CANVAS_RATE / 2, pos[1] * CANVAS_RATE - CANVAS_RATE / 2, CANVAS_RATE, CANVAS_RATE);
            }
        }
    }
}
