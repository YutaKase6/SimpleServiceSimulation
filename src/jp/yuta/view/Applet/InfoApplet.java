package jp.yuta.view.Applet;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static jp.yuta.util.Const.*;

/**
 * Created by yutakase on 2016/06/06.
 */
public class InfoApplet extends AbstractApplet {

    private static final int START_Y = 100;
    private static final int DELTA_Y = 20;

    private static final int START_X = 20;
    private static final int RESOURCE_WIDTH = 150;
    private static final int PRICE_WIDTH = 50;
    private static final int BEST_PRICE_WIDTH = 60;
    private static final int BEST_PAYOFF_WIDTH = 75;
    private static final int BEST_N_CONSUMERS_WIDTH = 50;

    private static final String RESOURCE = "Resource";
    private static final String PRICE = "Price";
    private static final String BEST_PRICE = "BestPrice";
    private static final String BEST_PAYOFF = "BestPayoff";
    private static final String BEST_N_CONSUMER = "BestNConsumers";

    public InfoApplet(int serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public void draw(Graphics2D buffer) {
        Map<String, String> dataMap = new HashMap<>();

        this.actors.stream()
                .filter(actor -> actor.isProvider(this.serviceId))
                .forEach(actor -> {
                    int i = actor.getId() % N_PROVIDER;
                    buffer.setColor(Color.black);
                    int y = START_Y + (i * DELTA_Y);
                    // 描画
                    dataMap.put(RESOURCE, "" + actor.getOperantResource(this.serviceId));
                    dataMap.put(PRICE, "" + actor.getPrice(this.serviceId));
                    dataMap.put(BEST_PRICE, "" + actor.getBestPrice(this.serviceId));
                    dataMap.put(BEST_PAYOFF, "" + actor.getBestPayoff(this.serviceId));
                    dataMap.put(BEST_N_CONSUMER, "" + actor.getBestNConsumer(this.serviceId));
                    this.drawRow(buffer, dataMap, y);
                    this.drawRect(buffer, i == nowProviderId, y, actor.getColor(this.serviceId));
                });

        // 一行目
        dataMap.put(RESOURCE, RESOURCE);
        dataMap.put(PRICE, PRICE);
        dataMap.put(BEST_PRICE, BEST_PRICE);
        dataMap.put(BEST_PAYOFF, BEST_PAYOFF);
        dataMap.put(BEST_N_CONSUMER, BEST_N_CONSUMER);
        this.drawRow(buffer, dataMap, START_Y - DELTA_Y);
    }

    // 一行をいい感じに描画
    private void drawRow(Graphics2D buffer, Map<String, String> strMap, int y) {
        int x = START_X;
        buffer.drawString(strMap.get(RESOURCE), x, y);
        x += RESOURCE_WIDTH;
        buffer.drawString(strMap.get(PRICE), x, y);
        x += PRICE_WIDTH;
        buffer.drawString(strMap.get(BEST_PRICE), x, y);
        x += BEST_PRICE_WIDTH;
        buffer.drawString(strMap.get(BEST_PAYOFF), x, y);
        x += BEST_PAYOFF_WIDTH;
        buffer.drawString(strMap.get(BEST_N_CONSUMER), x, y);
        x += BEST_N_CONSUMERS_WIDTH;
    }

    private void drawRect(Graphics2D buffer, boolean isNowProvider, int y, Color color) {
        int outerRectSize = 10;
        int innerRectSize = 6;
        if (isNowProvider) {
            outerRectSize *= 2;
            innerRectSize *= 2;
        }
        int rectPadding = (outerRectSize - innerRectSize) / 2;
        int rectX = START_X - outerRectSize;
        int rectY = y - outerRectSize;

        buffer.fillRect(rectX, rectY, outerRectSize, outerRectSize);
        buffer.setColor(color);
        buffer.fillRect(rectX + rectPadding, rectY + rectPadding, innerRectSize, innerRectSize);
    }
}
