package jp.yuta.view;

import jp.yuta.model.Actor;

import java.util.List;

/**
 * 描画クラス管理クラス
 * 描画関係処理はすべてこのクラスを経由して行う。
 * Created by yutakase on 2016/07/15.
 */
public abstract class ViewManager {
    public abstract void callRepaint(int serviceId);

    public abstract void setActors(List<Actor> actors);

    public abstract void setNowProviderId(int providerId, int serviceId);

    public abstract void initFrame();
}
