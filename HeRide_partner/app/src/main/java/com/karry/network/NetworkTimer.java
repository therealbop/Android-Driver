package com.karry.network;

/**
 * @since  12/20/2017.
 * @author 3Embed.
 * @version 1.0.
 */
public class NetworkTimer extends Thread
{
    private int SLEEP_TIME=1000;
    private static NetworkTimer networkTimer;

    private NetworkTimer() {}
    public static NetworkTimer getNetworkTimer()
    {
        if(networkTimer==null)
            networkTimer=new NetworkTimer();

        return networkTimer;
    }


    private TimerChecker timerChecker;
    public void scheduleAtFixedRate(TimerChecker temp,int sleep_time)
    {
        timerChecker=temp;
        if(sleep_time>SLEEP_TIME)
        {
            SLEEP_TIME=sleep_time;
        }
        this.start();
    }
    @Override
    public void run()
    {
        super.run();
        while(networkTimer!=null)
        {
            try
            {
                if(timerChecker!=null)
                {
                    timerChecker.run();
                }
                Thread.sleep(SLEEP_TIME);
            } catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Stopping the network service
     */
    public void cancel()
    {
        try
        {
            networkTimer=null;
            this.interrupt();
            this.stop();
        }catch (Exception e) {}
    }
}