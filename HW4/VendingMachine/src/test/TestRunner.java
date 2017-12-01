package test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestCoins.class, TestDrinks.class, VendingTest.class);

        int nRuns = result.getRunCount();
        int nPass = nRuns - result.getFailureCount();
        long runTime = result.getRunTime();

        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        System.out.println(nPass + "/" + nRuns + " tests passed in " + runTime + " ms");
    }
}  	