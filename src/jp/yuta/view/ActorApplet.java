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
        // Actor描画。関係ない顧客->関係ある顧客->提供者の順番。見やすさ重視。
        this.actors.stream().filter(actor -> !actor.isProvider(this.serviceId) && actor.getSelectProviderId(this.serviceId) != this.nowProviderId).forEach(actor -> this.drawActor(buffer, actor));
        this.actors.stream().filter(actor -> !actor.isProvider(this.serviceId) && actor.getSelectProviderId(this.serviceId) == this.nowProviderId).forEach(actor -> this.drawActor(buffer, actor));
        this.actors.stream().filter(actor -> actor.isProvider(this.serviceId)).forEach(actor -> this.drawActor(buffer, actor));
    }

    /**
     * Actor描画
     */
    private void drawActor(Graphics2D buffer, Actor actor) {
        int[] pos = actor.getPos();
        buffer.setColor(actor.getColor(this.serviceId));
        if (actor.isProvider(this.serviceId)) {
            // Provider
            // シミュレーション中のProviderを大きく表示
            int size = (actor.getId() == this.nowProviderId) ? CANVAS_RATE * 4 : CANVAS_RATE * 2;
            this.drawProvider(buffer, pos[0], pos[1], size, actor.getColor(this.serviceId));
        } else {
            // Consumer
            int size = CANVAS_RATE * 2;
            buffer.fillOval(pos[0] * CANVAS_RATE - size / 2, pos[1] * CANVAS_RATE - size / 2, size, size);
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
