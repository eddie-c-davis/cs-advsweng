package test;

import vending.Coin;
import vending.VendingMachine;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by edavis on 11/17/17.
 */
public class TestCoins {
    private final int NICKEL_AMOUNT = 5;
    private final int DIME_AMOUNT = 10;
    private final int QUARTER_AMOUNT = 25;
    private final int DOLLAR_AMOUNT = 100;

    @org.junit.Test
    public void testNickel() {
        VendingMachine machine = MachineBuilder.build();   // Build a VM...
        machine.insertCoin(Coin.NICKEL);
        assertTrue("Inserting Nickel", machine.getDeposit() == NICKEL_AMOUNT);
    }

    @org.junit.Test
    public void testDime() {
        VendingMachine machine = MachineBuilder.build();   // Build a VM...
        machine.insertCoin(Coin.DIME);
        assertTrue("Inserting Dime", machine.getDeposit() == DIME_AMOUNT);
    }

    @org.junit.Test
    public void testQuarter() {
        VendingMachine machine = MachineBuilder.build();   // Build a VM...
        machine.insertCoin(Coin.QUARTER);
        assertTrue("Inserting Quarter", machine.getDeposit() == QUARTER_AMOUNT);
    }

    @org.junit.Test
    public void testDollar() {
        VendingMachine machine = MachineBuilder.build();   // Build a VM...
        machine.insertCoin(Coin.DOLLAR);
        assertTrue("Inserting Quarter", machine.getDeposit() == DOLLAR_AMOUNT);
    }
}
