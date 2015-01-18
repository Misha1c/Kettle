package net.gorbov;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation kettle interface.
 */
public class ElectricKettle implements Kettle {

    private int maxVolume;
    private int minVolume;
    private int currentVolume       = 0;
    private AtomicInteger currentTemperature = new AtomicInteger();
    private int minTemperature      = 0;
    private boolean isBoil          = false;
    private boolean lidIsOpen       = false;
    private boolean serviceStarted  = false;

    ExecutorService service = Executors.newFixedThreadPool(2);

    public ElectricKettle(int minVolume, int maxVolume) {

        if(maxVolume <= 0 ){
            throw new UnsupportedOperationException("maxVolume <= 0");
        }
        if(minVolume <= 0 ){
            throw new UnsupportedOperationException("minVolume <= 0");
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

        if(volume <= 0){
            return false;
        }

        if(currentTemperature < 0){
            return false;
        }

        if(currentTemperature > 100){
            return false;
        }

        currentVolume += volume;
        this.currentTemperature.set(currentTemperature);

        if(!serviceStarted){
            service.submit(new Runnable() {
                public void run() {
                    boilService();
                }
            });
        }

        return true;
    }

    @Override
    public boolean pourOutWater(int volume) {

        if(volume>currentVolume){
            return false;
        }

        currentVolume -= volume;
        return true;
    }

    @Override
    public int getCurrentTemperature(){
        return currentTemperature.get();
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
                    boilService();
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

    @Override
    public void finalizeKettle(){
        service.shutdown();
    }

    private void boilService(){

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

                Thread.sleep(1000);

                if(currentTemperature.get() > minTemperature){
                    currentTemperature.decrementAndGet();
                }else if (currentTemperature.get() < minTemperature) {
                    currentTemperature.incrementAndGet();
                }

                System.out.println("Current temperature (temperatureStabilizingService) = "+currentTemperature.get());

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
