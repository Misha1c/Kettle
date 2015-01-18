package net.gorbov;

/**
 * This is universal interface of Kettle.
 */
public interface Kettle {

    boolean lidIsOpen();
    boolean openLid();
    boolean closeLid();
    int getCurrentVolume();
    int getCurrentTemperature();
    boolean addWater(int volume, int currentTemperature);
    boolean pourOutWater(int volume);
    boolean startBoil();
    boolean stopBoil();
    void finalizeKettle();
}
