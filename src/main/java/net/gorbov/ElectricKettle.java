package net.gorbov;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Mihail on 16.01.2015.
 */
public class ElectricKettle implements Kettle {

    private int maxVolume;
    private int minVolume;
    private int currentVolume       = 0;
    private AtomicInteger currentTemperature;
    private int minTemperature      = 0;
    volatile private boolean isBoil = false;
    private boolean lidIsOpen       = false;
    private boolean serviceStarted  = false;

    ExecutorService service = Executors.newFixedThreadPool(2);

    ElectricKettle(int minVolume, int maxVolume) throws UnsupportedOperationException{

        if(maxVolume == 0 ){
            throw new UnsupportedOperationException("maxVolume == 0");
        }
        if (maxVolume < minVolume){
            throw new UnsupportedOperationException("maxVolume < minVolume");
        }

        this.maxVolume = maxVolume;
        this.minVolume = minVolume;

        service.submit(new Runnable() {
            public void run() {
                temperatureStabilizingService();
            }
        });
    }

    @Override
    public boolean openLid() {

        if (isBoil) {
            return false;
        }

        lidIsOpen = true;
        return true;

    }

    @Override
    public boolean closeLid() {

        lidIsOpen = false;
        return true;
    }

    @Override
    public boolean lidIsOpen(){
        return lidIsOpen;
    }

    @Override
    public int getCurrentVolume(){
        return currentVolume;
    }

    @Override
    public boolean addWater(int volume, int currentTemperature) {

        if(!lidIsOpen){
            return false;
        }

        if(currentVolume + volume > maxVolume){
            return false;
        }

        currentVolume += volume;
        this.currentTemperature.set(currentTemperature);

        if(!serviceStarted){
            service.submit(new Runnable() {
                public void run() {
                    startBoilService();
                }
            });
        }

        return true;
    }

    @Override
    public boolean pourWater(int volume) {

        if(volume>currentVolume){
            return false;
        }

        currentVolume -= volume;
        return true;
    }

    @Override
    public boolean startBoil() {

        if(lidIsOpen){
            return false;
        }

        if(minVolume > currentVolume){
            return false;
        }

        isBoil = true;

        if(!serviceStarted){
            service.submit(new Runnable() {
                public void run() {
                    startBoilService();
                }
            });
        }

        return true;
    }

    @Override
    public boolean stopBoil() {

        if(!isBoil){
            return false;
        }

        isBoil = false;
        return true;
    }

    private void startBoilService(){

        serviceStarted = true;

        try {
            if(isBoil) {

                while (currentTemperature.incrementAndGet() < 100){

                    if(!isBoil){
                        break;
                    }

                    System.out.println("Current temperature = "+currentTemperature);
                    Thread.sleep(100);
                }

                stopBoil();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        serviceStarted = false;
    }

    private void temperatureStabilizingService(){
        try {
            while (true){

                if(currentTemperature.get() > minTemperature){
                    currentTemperature.decrementAndGet();
                }else if (currentTemperature.get() < minTemperature) {
                    currentTemperature.incrementAndGet();
                }

                Thread.sleep(1000);
                System.out.println("Current temperature = "+currentTemperature);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
