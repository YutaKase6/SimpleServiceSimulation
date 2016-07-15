package jp.yuta.view;

import jp.yuta.model.Actor;

import java.util.List;

/**
 * Created by yutakase on 2016/07/15.
 */
public abstract class ViewManager {
    public abstract void callRepaint(int serviceId);

    public abstract void setActors(List<Actor> actors, int serviceId);

    public abstract void setNowProviderId(int providerId, int serviceId);

    public abstract void initFrame();
}
