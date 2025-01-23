package com.dynatrace;

import dev.openfeature.sdk.ProviderEvent;

import java.util.function.Consumer;

public class DummyFlagStore {

    private final Consumer<ProviderEvent> emitProviderEvent;
    private volatile boolean isReady;

    public DummyFlagStore(Consumer<ProviderEvent> emitProviderEvent) {
        this.emitProviderEvent = emitProviderEvent;
    }

    public void init() {
        new Thread(() -> {

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            emitProviderEvent.accept(ProviderEvent.PROVIDER_READY);
        }).start();
    }

    public boolean isReady() {
        return isReady;
    }

    public synchronized void setReady() {
        isReady = true;
        this.notifyAll();
    }
}
