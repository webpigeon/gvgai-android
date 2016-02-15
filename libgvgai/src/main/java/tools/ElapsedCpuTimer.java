package tools;

/**
 * Created by diego on 26/02/14.
 */

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ElapsedCpuTimer {

    long oldTime;
    long maxTime;

    public enum TimerType {
        WALL_TIME, CPU_TIME, USER_TIME
    };

    private TimerType type = TimerType.WALL_TIME;

    public ElapsedCpuTimer(TimerType type) {
        this.type = type;
        oldTime = getTime();
    }

    public ElapsedCpuTimer() {
        oldTime = getTime();
    }

    public ElapsedCpuTimer copy()
    {
        ElapsedCpuTimer newCpuTimer = new ElapsedCpuTimer(this.type);
        newCpuTimer.maxTime = this.maxTime;
        newCpuTimer.oldTime = this.oldTime;
        return newCpuTimer;
    }

    public long elapsed() {
        return getTime() - oldTime;
    }


    public long elapsedNanos() {
        return (long) (elapsed() / 1000.0);
    }

    public long elapsedMillis() {
        return (long) (elapsed() / 1000000.0);
    }

    public double elapsedSeconds() {
        return elapsedMillis()/1000.0;
    }

    public double elapsedMinutes() {
        return elapsedMillis()/1000.0/60.0;
    }


    public double elapsedHours() {
        return elapsedMinutes()/60.0;
    }


    @Override
	public String toString() {
        // now resets the timer...
        String ret = elapsed() / 1000000.0 + " ms elapsed";
        //reset();
        return ret;
    }

    private long getTime() {
        switch (type) {
            case WALL_TIME:
                return getWallTime();

            case CPU_TIME:
                return getCpuTime();

            case USER_TIME:
                return getUserTime();

            default:
                break;
        }
        return getWallTime();
    }

    private long getWallTime() {
        return System.nanoTime();
    }

    private long getCpuTime() {
        //FIXME android doesn't support the java class used before
        return getWallTime();
    }

    private long getUserTime() {
        //FIXME android doesn't support the java class used before
        return getWallTime();
    }

    public void setMaxTimeMillis(long time) {
        maxTime = time * 1000000;

    }

    public long remainingTimeMillis()
    {
        long diff = maxTime - elapsed();
        return (long) (diff / 1000000.0);
    }

    public boolean exceededMaxTime() {
        if (elapsed() > maxTime) {
            return true;
        }
        return false;
    }

}
