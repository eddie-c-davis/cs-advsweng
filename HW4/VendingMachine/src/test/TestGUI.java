package test;

import gui.VendingGUI;
import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.finder.NamedComponentFinder;
import vending.Coin;
import vending.Drink;
import vending.VendingMachine;

import javax.swing.*;

import java.awt.*;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by edavis on 11/17/17.
 */
public class TestGUI extends JFCTestCase {
    // JFC helper object
    private JFCTestHelper _helper;
    // JFC component finder
    private NamedComponentFinder _finder;
    // Vending GUI (JFrame)
    private VendingGUI _gui;
    // Associated vending machine
    private VendingMachine _vm;

    // JUnit test setup method
    protected void setUp() {
        _helper = new JFCTestHelper();
        _finder = new NamedComponentFinder(JComponent.class, "");
        _gui = new VendingGUI();
        _gui.pack();
        _gui.setVisible(true);
        _vm = _gui.getVendingMachine(); //MachineBuilder.build();
    }

    // JUnit test teardown method
    protected void tearDown() {
        _gui.setVisible(false); //you can't see me!
        _gui.dispose(); //Destroy the JFrame object
        _gui = null;
        JFCTestHelper.cleanUp(this);
    }

    // Test coffee drink (also tests coins)
    public void testCoffee() {
        testDrink(_vm.getCoffee());
    }

    // Test juice drink (also tests coins)
    public void testJuice() {
        testDrink(_vm.getJuice());
    }

    // Test soda drink (alsot ests coins)
    public void testSoda() {
        testDrink(_vm.getSoda());
    }

    // Test that the info text field is initialized properly.
    public void testInfoField() {
        String fieldName = "InfoField";
        JTextField infoTextField = (JTextField) findComponent(fieldName);
        assertNotNull("Could not find the '" + fieldName + "' text field", infoTextField);
        assertTrue(infoTextField.getText().equals("Welcome! "));
    }

    // Test the 'Return Coins' button
    public void testReturn() {
        // Click the Return button...
        String returnName = "ReturnCoins";
        JButton returnButton = (JButton) findComponent(returnName);
        assertNotNull("Could not find the '" + returnName + "' button", returnButton);
        clickComponent(returnButton);
        assertTrue(_vm.getDeposit() == 0);
    }

    // Test a drink (coffee, juice, or soda).
    private void testDrink(Drink drink) {
        String drinkName = drink.getName();
        JRadioButton drinkButton = (JRadioButton) findComponent(drinkName);
        assertNotNull("Could not find the '" + drinkName + "' button", drinkButton);
        clickComponent(drinkButton);

        int count = drink.getCount();
        assertTrue("Testing drink count" + drink.getName(), count > 0);

        // Test insert of each coin...
        Coin[] coins = new Coin[] {Coin.NICKEL, Coin.DIME, Coin.QUARTER, Coin.DOLLAR};

        int deposit = 0;
        for (int i = 0; i < coins.length; i++) {
            Coin coin = coins[i];
            deposit += coin.value();
            testCoin(coins[i], deposit);
        }

        // Ensure enough money for the most expensive drink (i.e, soda).
        int expectedChange = _vm.getDeposit() - drink.getPrice();

        // Click the Purchase button...
        String purchaseName = "Purchase";
        JButton purchaseButton = (JButton) findComponent(purchaseName);
        assertNotNull("Could not find the '" + purchaseName + "' button", purchaseButton);
        clickComponent(purchaseButton);

        // Verify the purchase...
        int newCount = drink.getCount();
        assertTrue("Count is reduced", newCount < count);
        int actualChange = _vm.getChange();
        assertTrue("Test proper change", actualChange == expectedChange);
        assertTrue("Change has not changed", actualChange == _vm.getChange());
    }

    // Test a coin (NICKEL, DIME, QUARTER, or DOLLAR).
    private void testCoin(Coin coin, int deposit) {
        String coinName = coin.name();
        JRadioButton coinButton = (JRadioButton) findComponent(coinName);
        assertNotNull("Could not find the '" + coinName + "' button", coinButton);
        clickComponent(coinButton);

        // Click the 'Insert Coin' button
        String insertCoinName = "InsertCoin";
        JButton insertCoinButton = (JButton) findComponent(insertCoinName);
        assertNotNull("Could not find the '" + insertCoinName + "' button", insertCoinButton);

        clickComponent(insertCoinButton);
        assertTrue("Inserted " + coinName, _vm.getDeposit() == deposit);

        // Test deposit text field is updated appropriately...
        String expectedText = deposit + " cents";
        String fieldName = "DepositField";
        JTextField depositTextField = (JTextField) findComponent(fieldName);
        assertNotNull("Could not find the text field with contents '" + fieldName + "'", depositTextField);
        assertTrue(depositTextField.getText().equals(expectedText));
    }

    // Find a GUI component of the given name using the JFC NamedComponentFinder.
    private Component findComponent(String name) {
        _finder.setName(name);
        return _finder.find(_gui, 0);
    }

    // Invoke the click event of the given JComponent object.
    private void clickComponent(JComponent component) {
        try {
            _helper.enterClickAndLeave(new MouseEventData(this, component));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail(ex.toString());
        }
    }
}
