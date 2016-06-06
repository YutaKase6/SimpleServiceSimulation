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

    public int countConsumer(int id){
        int count = 0;
        for(Actor actor:this.actors){
            if(actor.getSelectProviderId() == id){
                count++;
            }
        }
        return count;
    }

    public List<Actor> getActors(){
        return this.actors;
    }
}
