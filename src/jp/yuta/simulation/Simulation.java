package jp.yuta.simulation;

/**
 * Created by yutakase on 2016/06/08.
 */
public abstract class Simulation {

    protected int stepCount = 0;

    public void mainLoop() {
        stepCount = 0;
        init();
        while (isSimulationFinish()) {
            step();
            stepCount++;
            System.out.println(stepCount);
        }
    }

    public abstract void init();

    public abstract void step();

    public abstract boolean isSimulationFinish();

    public int getStepCount() {
        return stepCount;
    }

}
