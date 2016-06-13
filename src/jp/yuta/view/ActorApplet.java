package jp.yuta.view;

import jp.yuta.model.Actor;

import java.awt.*;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/02.
 */
public class ActorApplet extends AbstractApplet {


    @Override
    public void draw(Graphics2D buffer) {
        // ProviderはListの先頭にいるため、Providerを最後に描画するために逆順に描画、この方が見やすいってだけ。
        int[] pos;
        for (int i = this.actors.size() - 1; i > -1; i--) {
            Actor actor = this.actors.get(i);
            pos = actor.getPos();
            buffer.setColor(actor.getColor());
            if (actor.isProvider()) {
                // シミュレーション中のProviderを大きく表示
                int size = i == nowProviderId ? CANVAS_RATE * 4 : CANVAS_RATE * 2;
                drawProvider(buffer, pos[0], pos[1], size, actor.getColor());
            } else {
                buffer.fillOval(pos[0] * CANVAS_RATE - CANVAS_RATE / 2, pos[1] * CANVAS_RATE - CANVAS_RATE / 2, CANVAS_RATE, CANVAS_RATE);
            }
        }
    }

    /**
     * Providerを描画
     */
    private void drawProvider(Graphics2D g, int x, int y, int size, Color color) {
        g.setColor(Color.black);
        g.fillRect(x * CANVAS_RATE - size / 2, y * CANVAS_RATE - size / 2, size, size);
        g.setColor(color);
        g.fillRect(x * CANVAS_RATE - size / 4, y * CANVAS_RATE - size / 4, size / 2, size / 2);
    }

}
