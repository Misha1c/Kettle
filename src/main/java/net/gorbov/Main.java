package net.gorbov;

/**
 * Created by Mihail on 17.01.2015.
 */
public class Main {

    public static void main(String[] args){
        Kettle kettle = new ElectricKettle(100,200);
        kettle.openLid();
        kettle.addWater(200,10);
        kettle.closeLid();
        kettle.startBoil();
    }
}
