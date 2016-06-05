package jp.yuta.model;

import java.util.List;

/**
 * Created by yutakase on 2016/06/04.
 */
public class Market {

    private List<Actor> actors;

    public Market(List<Actor> actors) {
        this.actors = actors;
    }

    public List<Actor> getActors(){
        return actors;
    }
}
