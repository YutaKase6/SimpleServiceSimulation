package jp.yuta.simulation;

/**
 * Created by yutakase on 2016/06/08.
 */
public abstract class Simulation {

    public void mainLoop(){
        while (isSimulationFinish()){
            step();
        }
    }

    public abstract void step();

    public abstract boolean isSimulationFinish();

}
