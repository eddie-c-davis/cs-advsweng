package test;

import vending.Coin;
import vending.Drink;
import vending.VendingMachine;

import java.util.Random;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by edavis on 12/7/17.
 */
public class TestFSM {
    private final int COFFEE_PRICE = 85;
    private final int JUICE_PRICE = 60;
    private final int SODA_PRICE = 115;

    private final int COFFEE_COUNT = 3;
    private final int JUICE_COUNT = 2;
    private final int SODA_COUNT = 5;

    private void insertCoin(VendingMachine vm) {
        final Coin[] coins = new Coin[] {Coin.NICKEL, Coin.DIME, Coin.QUARTER, Coin.DOLLAR};
        Random random = new Random();
        int index = random.nextInt(coins.length);

        vm.insertCoin(coins[index]);
    }

    private void setCoffee(VendingMachine vm) {
        vm.setDrink("Coffee", COFFEE_PRICE, COFFEE_COUNT);
    }

    @org.junit.Test
    public void test_1() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue(vm.getDeposit() == 0);
        insertCoin(vm);
        // HasDeposit
        assertTrue(vm.getDeposit() > 0);
        vm.returnCoins();
        // Empty
        assertTrue(vm.getDeposit() == 0);
    }

    @org.junit.Test
    public void test_2() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue("deposit > 0", vm.getDeposit() == 0);
        assertTrue(vm.getCoffee().getCount() > 0);
        setCoffee(vm);
        // HasCoffee
        assertTrue("coffeeCount > 0", vm.getCoffee().getCount() > 0);

        insertCoin(vm);
        // HasCoffeeAndCoins
        // Satisfy purchase precondition:
        while (vm.getDeposit() < COFFEE_PRICE) {
            insertCoin(vm);
        }

        assertTrue("deposit >= coffeePrice", vm.getDeposit() >= COFFEE_PRICE);

        // Purchase
        boolean isPurchased = vm.purchase("Coffee");
        assertTrue("Coffee purchased", isPurchased);
    }
}