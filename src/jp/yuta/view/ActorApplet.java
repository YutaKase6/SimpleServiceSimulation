package jp.yuta.view;

import jp.yuta.model.Actor;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import static jp.yuta.util.Config.*;

/**
 * Created by yutakase on 2016/06/02.
 */
public class ActorApplet extends AbstractApplet implements MouseListener {

    @Override
    public void init() {
        super.init();
        addMouseListener(this);
    }

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
                int size = CANVAS_RATE * 2;
                buffer.fillOval(pos[0] * CANVAS_RATE - size / 2, pos[1] * CANVAS_RATE - size / 2, size, size);
            }
        }
        for (int i = this.actors.size() - 1; i > -1; i--) {
            Actor actor = this.actors.get(i);
            if(!actor.isProvider() && actor.getSelectProviderId() != this.nowProviderId) continue;
            pos = actor.getPos();
            buffer.setColor(actor.getColor());
            if (actor.isProvider()) {
                // シミュレーション中のProviderを大きく表示
                int size = i == nowProviderId ? CANVAS_RATE * 4 : CANVAS_RATE * 2;
                drawProvider(buffer, pos[0], pos[1], size, actor.getColor());
            } else {
                int size = CANVAS_RATE * 2;
                buffer.fillOval(pos[0] * CANVAS_RATE - size / 2, pos[1] * CANVAS_RATE - size / 2, size, size);
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

    // test
    public void hoge(int id){
        this.nowProviderId = id;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("hoge");
        Point p = e.getPoint();
        System.out.println(p.getX() + "," + p.getY());
        this.actors.stream().parallel().filter(Actor::isProvider).forEach(actor -> {
            if (this.isOverlapProvider(actor.getPos(), p)) {
                nowProviderId = actor.getId();
                repaint();
            }
        });
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    private boolean isOverlapProvider(int[] pos, Point p) {
        return p.getX() > pos[0] - CANVAS_RATE && p.getX() < pos[0] + CANVAS_RATE && p.getY() > pos[1] - CANVAS_RATE && p.getY() < pos[1] + CANVAS_RATE;
    }
}
