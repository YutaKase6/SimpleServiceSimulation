package jp.yuta.simulation;

/**
 * Created by yutakase on 2016/06/08.
 */
public abstract class Simulation {

    private int stepCount = 0;

    public void mainLoop(){
        stepCount = 0;
        while (isSimulationFinish()){
            step();
            stepCount++;
        }
    }

    public abstract void step();

    public abstract boolean isSimulationFinish();

    public int getStepCount(){
        return stepCount;
    }

}
