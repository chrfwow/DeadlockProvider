package com.dynatrace;

import dev.openfeature.sdk.OpenFeatureAPI;

public class DeadlockTest {
    public static void main(String[] args) throws Exception {
        var deadlockProvider = new DeadlockProvider();
        OpenFeatureAPI.getInstance().setProviderAndWait(deadlockProvider);
        //deadlockProvider.initialize(new ImmutableContext());

        System.out.println("No deadlock!!!!!");

        Thread.sleep(50000);

        System.out.println("terminated");
    }
}
