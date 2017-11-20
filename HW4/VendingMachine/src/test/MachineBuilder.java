package test;

import vending.VendingMachine;

/**
 * Created by edavis on 11/17/17.
 */
public class MachineBuilder {
    public static VendingMachine build() {
        VendingMachine vending = new VendingMachine();
        vending.setDrink(VendingMachine.COFFEE, 85, 3);
        vending.setDrink(VendingMachine.JUICE, 60, 2);
        vending.setDrink(VendingMachine.SODA, 115, 5);
        return vending;
    }
}
