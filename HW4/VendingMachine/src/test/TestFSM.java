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

    private final int NUM_COINS = 4;
    private final Coin[] COINS = new Coin[] {Coin.NICKEL, Coin.DIME, Coin.QUARTER, Coin.DOLLAR};

    private int _coinIndex = 0;

    private void insertCoin(VendingMachine vm) {
        vm.insertCoin(COINS[_coinIndex]);
        _coinIndex = (_coinIndex + 1) % NUM_COINS;
    }

    private void setCoffee(VendingMachine vm) {
        vm.setDrink("Coffee", COFFEE_PRICE, COFFEE_COUNT);
    }

    private void setJuice(VendingMachine vm) {
        vm.setDrink("Juice", JUICE_PRICE, JUICE_COUNT);
    }

    private void setSoda(VendingMachine vm) {
        vm.setDrink("Soda", SODA_PRICE, SODA_COUNT);
    }

    @org.junit.Test
    public void test_1() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue("deposit == 0", vm.getDeposit() == 0);
        insertCoin(vm);
        // HasDeposit
        assertTrue("deposit > 0", vm.getDeposit() > 0);
        vm.returnCoins();
        // Empty
        assertTrue("deposit == 0", vm.getDeposit() == 0);
    }

    @org.junit.Test
    public void test_2() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue("deposit == 0", vm.getDeposit() == 0);
        assertTrue("coffeeCount == 0", vm.getCoffee().getCount() == 0);
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
        int deposit = vm.getDeposit();
        boolean isPurchased = vm.purchase("Coffee");
        assertTrue("Coffee purchased", isPurchased);

        int change = vm.getChange();
        assertTrue("Deposit reduced", change == deposit - COFFEE_PRICE);

        // Return coins
        vm.returnCoins();
        // HasCoffee
        assertTrue(vm.getDeposit() == 0);
    }

    @org.junit.Test
    public void test_3() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue("deposit == 0", vm.getDeposit() == 0);
        assertTrue("coffeeCount == 0", vm.getCoffee().getCount() == 0);
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

        // Continue purchasing until out of coffee
        while (vm.getCoffee().getCount() > 0) {
            while (vm.getDeposit() < COFFEE_PRICE) {
                insertCoin(vm);
            }
            vm.purchase("Coffee");
        }

        // Empty
        assertTrue("Out of coffee", vm.getCoffee().getCount() < 1);
    }

    @org.junit.Test
    public void test_4() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue("deposit == 0", vm.getDeposit() == 0);
        insertCoin(vm);
        // HasDeposit
        assertTrue("deposit > 0", vm.getDeposit() > 0);

        setCoffee(vm);
        // HasCoffeeAndCoins
        assertTrue("coffeeCount > 0", vm.getCoffee().getCount() > 0);

        // Satisfy purchase precondition:
        while (vm.getDeposit() < COFFEE_PRICE) {
            insertCoin(vm);
        }

        // Purchase
        int deposit = vm.getDeposit();
        boolean isPurchased = vm.purchase("Coffee");
        assertTrue("Coffee purchased", isPurchased);

        int change = vm.getChange();
        // HasCoffeeAndCoins
        assertTrue("Deposit reduced", change == deposit - COFFEE_PRICE);
    }

    @org.junit.Test
    public void test_5() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue("deposit == 0", vm.getDeposit() == 0);
        assertTrue("juiceCount == 0", vm.getJuice().getCount() == 0);
        setJuice(vm);
        // HasJuice
        assertTrue("coffeeCount > 0", vm.getJuice().getCount() > 0);

        insertCoin(vm);
        // HasJuiceAndCoins

        // Satisfy purchase precondition:
        while (vm.getDeposit() < JUICE_PRICE) {
            insertCoin(vm);
        }

        assertTrue("deposit >= juicePrice", vm.getDeposit() >= JUICE_PRICE);

        // Purchase
        int deposit = vm.getDeposit();
        boolean isPurchased = vm.purchase("Juice");
        assertTrue("Juice purchased", isPurchased);

        int change = vm.getChange();
        assertTrue("Deposit reduced", change == deposit - JUICE_PRICE);

        // Return coins
        vm.returnCoins();
        // HasJuice
        assertTrue(vm.getDeposit() == 0);
    }

    @org.junit.Test
    public void test_6() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue("deposit == 0", vm.getDeposit() == 0);
        assertTrue("juiceCount == 0", vm.getJuice().getCount() == 0);
        setJuice(vm);
        // HasJuice
        assertTrue("juiceCount > 0", vm.getJuice().getCount() > 0);

        insertCoin(vm);
        // HasJuiceAndCoins
        // Satisfy purchase precondition:
        while (vm.getDeposit() < JUICE_PRICE) {
            insertCoin(vm);
        }

        assertTrue("deposit >= juicePrice", vm.getDeposit() >= JUICE_PRICE);

        // Continue purchasing until out of juice
        while (vm.getJuice().getCount() > 0) {
            while (vm.getDeposit() < JUICE_PRICE) {
                insertCoin(vm);
            }
            vm.purchase("Juice");
        }

        // Empty
        assertTrue("Out of juice", vm.getJuice().getCount() < 1);
    }

    @org.junit.Test
    public void test_7() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue("deposit == 0", vm.getDeposit() == 0);
        insertCoin(vm);
        // HasDeposit
        assertTrue("deposit > 0", vm.getDeposit() > 0);

        setJuice(vm);
        // HasJuiceAndCoins
        assertTrue("juiceCount > 0", vm.getJuice().getCount() > 0);

        // Satisfy purchase precondition:
        while (vm.getDeposit() < JUICE_PRICE) {
            insertCoin(vm);
        }

        // Purchase
        int deposit = vm.getDeposit();
        boolean isPurchased = vm.purchase("Juice");
        assertTrue("Juice purchased", isPurchased);

        int change = vm.getChange();
        // HasJuiceAndCoins
        assertTrue("Deposit reduced", change == deposit - JUICE_PRICE);
    }

    @org.junit.Test
    public void test_8() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue("deposit == 0", vm.getDeposit() == 0);
        assertTrue("sodaCount == 0", vm.getSoda().getCount() == 0);
        setSoda(vm);
        // HasSoda
        assertTrue("coffeeCount > 0", vm.getSoda().getCount() > 0);

        insertCoin(vm);
        // HasSodaAndCoins

        // Satisfy purchase precondition:
        while (vm.getDeposit() < SODA_PRICE) {
            insertCoin(vm);
        }

        assertTrue("deposit >= sodaPrice", vm.getDeposit() >= SODA_PRICE);

        // Purchase
        int deposit = vm.getDeposit();
        boolean isPurchased = vm.purchase("Soda");
        assertTrue("Soda purchased", isPurchased);

        int change = vm.getChange();
        assertTrue("Deposit reduced", change == deposit - SODA_PRICE);

        // Return coins
        vm.returnCoins();
        // HasSoda
        assertTrue(vm.getDeposit() == 0);
    }

    @org.junit.Test
    public void test_9() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue("deposit == 0", vm.getDeposit() == 0);
        assertTrue("sodaCount == 0", vm.getSoda().getCount() == 0);
        setSoda(vm);
        // HasSoda
        assertTrue("sodaCount > 0", vm.getSoda().getCount() > 0);

        insertCoin(vm);
        // HasSodaAndCoins
        // Satisfy purchase precondition:
        while (vm.getDeposit() < SODA_PRICE) {
            insertCoin(vm);
        }

        assertTrue("deposit >= sodaPrice", vm.getDeposit() >= SODA_PRICE);

        // Continue purchasing until out of soda
        while (vm.getSoda().getCount() > 0) {
            while (vm.getDeposit() < SODA_PRICE) {
                insertCoin(vm);
            }
            vm.purchase("Soda");
        }

        // Empty
        assertTrue("Out of soda", vm.getSoda().getCount() < 1);
    }

    @org.junit.Test
    public void test_10() {
        VendingMachine vm = new VendingMachine();
        // Empty
        assertTrue("deposit == 0", vm.getDeposit() == 0);
        insertCoin(vm);
        // HasDeposit
        assertTrue("deposit > 0", vm.getDeposit() > 0);

        setSoda(vm);
        // HasSodaAndCoins
        assertTrue("sodaCount > 0", vm.getSoda().getCount() > 0);

        // Satisfy purchase precondition:
        while (vm.getDeposit() < SODA_PRICE) {
            insertCoin(vm);
        }

        // Purchase
        int deposit = vm.getDeposit();
        boolean isPurchased = vm.purchase("Soda");
        assertTrue("Soda purchased", isPurchased);

        int change = vm.getChange();
        // HasSodaAndCoins
        assertTrue("Deposit reduced", change == deposit - SODA_PRICE);
    }
}
