package test;

import vending.Coin;
import vending.Drink;
import vending.VendingMachine;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by edavis on 11/17/17.
 */
public class TestDrinks {
    @org.junit.Test
    public void testCoffee() {
        VendingMachine machine = MachineBuilder.build();   // Build a VM...
        testDrink(machine, machine.getCoffee());
    }

    @org.junit.Test
    public void testJuice() {
        VendingMachine machine = MachineBuilder.build();   // Build a VM...
        testDrink(machine, machine.getJuice());
    }

    @org.junit.Test
    public void testSoda() {
        VendingMachine machine = MachineBuilder.build();   // Build a VM...
        testDrink(machine, machine.getSoda());
    }

    private void testDrink(VendingMachine machine, Drink drink) {
        int count = drink.getCount();
        assertTrue("Testing drink count" + drink.getName(), count > 0);

        boolean purchased = machine.purchase(drink.getName());
        assertTrue("Test no purchase if NSF", !purchased);
        assertTrue("Count is unchanged", count == drink.getCount());

        // Ensure enough money for the most expensive drink (i.e, soda).
        machine.insertCoin(Coin.DOLLAR);
        machine.insertCoin(Coin.QUARTER);

        int expectedChange = machine.getDeposit() - drink.getPrice();

        purchased = machine.purchase(drink.getName());
        assertTrue("Test purchased", purchased);
        assertTrue("Count is reduced", drink.getCount() < count);
        assertTrue("Test proper change", machine.getChange() == expectedChange);
    }
}
