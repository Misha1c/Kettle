package net.gorbov;

/**
 * Created by Mihail on 15.01.2015.
 */
public interface Kettle {

    boolean lidIsOpen();
    boolean openLid();
    boolean closeLid();
    int getCurrentVolume();
    boolean addWater(int volume, int currentTemperature);
    boolean pourWater(int volume);
    boolean startBoil();
    boolean stopBoil();
}
