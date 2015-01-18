import net.gorbov.ElectricKettle;
import net.gorbov.Kettle;
import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * Created by Mihail on 18.01.2015.
 */
public class KettleTest {

    //Test constructor
    @Test(expected = UnsupportedOperationException.class)
    public void createElectricKettle1() {
        Kettle kettle = new ElectricKettle(2000, 1000);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createElectricKettle2(){
        Kettle kettle = new ElectricKettle(-1, 10);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createElectricKettle3(){
        Kettle kettle = new ElectricKettle(0, -1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createElectricKettle4(){
        Kettle kettle = new ElectricKettle(-1, 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void createElectricKettle5(){
        Kettle kettle = new ElectricKettle(0, 0);
    }

    //Test addWater
    @Test
    public void addWater0(){
        Kettle kettle = new ElectricKettle(1000, 2000);
        assertFalse(kettle.addWater(100, 30));
    }

    @Test
    public void addWater1(){
        Kettle kettle = new ElectricKettle(1000, 2000);
        kettle.openLid();
        assertFalse(kettle.addWater(-1, 30));
    }

    @Test
    public void addWater2(){
        Kettle kettle = new ElectricKettle(1000, 2000);
        kettle.openLid();
        assertFalse(kettle.addWater(-1,30));
    }

    @Test
    public void addWater3(){
        Kettle kettle = new ElectricKettle(1000, 2000);
        kettle.openLid();
        assertFalse(kettle.addWater(1000,-1));
    }

    @Test
    public void addWater4(){
        Kettle kettle = new ElectricKettle(1000, 2000);
        kettle.openLid();
        assertFalse(kettle.addWater(2001,30));
    }

    @Test
    public void addWater5(){
        Kettle kettle = new ElectricKettle(1000, 2000);
        kettle.openLid();
        assertFalse(kettle.addWater(1000,120));
    }

    @Test
    public void addWater6(){
        Kettle kettle = new ElectricKettle(1000, 2000);
        kettle.openLid();
        assertFalse(kettle.addWater(1000,120));
    }

    //Test boil
    @Test
    public void startBoil1(){
        Kettle kettle = new ElectricKettle(1000, 2000);
        kettle.openLid();
        kettle.addWater(1000, 120);
        assertFalse(kettle.startBoil());
    }

    @Test
         public void startBoil2(){
        Kettle kettle = new ElectricKettle(1000, 2000);
        kettle.openLid();
        kettle.addWater(900, 30);
        kettle.closeLid();
        assertFalse(kettle.startBoil());
    }

    @Test
    public void startBoil3(){
        Kettle kettle = new ElectricKettle(1000, 2000);
        kettle.openLid();
        kettle.addWater(2001, 30);
        kettle.closeLid();
        assertFalse(kettle.startBoil());
    }
}
