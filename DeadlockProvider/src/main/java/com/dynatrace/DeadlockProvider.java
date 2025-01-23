package com.dynatrace;

import dev.openfeature.sdk.*;

import java.util.List;

public class DeadlockProvider extends EventProvider {

    private final DummyFlagStore dummyFlagStore = new DummyFlagStore(this::onProviderEvent);

    @Override
    public void initialize(EvaluationContext evaluationContext) throws Exception {
        synchronized (dummyFlagStore) {
            dummyFlagStore.init();
            while (!dummyFlagStore.isReady()) {
                try {
                    dummyFlagStore.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private void onProviderEvent(ProviderEvent providerEvent) {
        synchronized (dummyFlagStore) {
            if (providerEvent == ProviderEvent.PROVIDER_READY) {
                dummyFlagStore.setReady();

                // This line deadlocks:
                emitProviderReady(ProviderEventDetails.builder().build());
                // This does not:
                new Thread(() -> emitProviderReady(ProviderEventDetails.builder().build())).start();
            }
        }
    }

    @Override
    public Metadata getMetadata() {
        return null;
    }

    @Override
    public List<Hook> getProviderHooks() {
        return super.getProviderHooks();
    }

    @Override
    public ProviderEvaluation<Boolean> getBooleanEvaluation(String s, Boolean aBoolean, EvaluationContext evaluationContext) {
        return null;
    }

    @Override
    public ProviderEvaluation<String> getStringEvaluation(String s, String s1, EvaluationContext evaluationContext) {
        return null;
    }

    @Override
    public ProviderEvaluation<Integer> getIntegerEvaluation(String s, Integer integer, EvaluationContext evaluationContext) {
        return null;
    }

    @Override
    public ProviderEvaluation<Double> getDoubleEvaluation(String s, Double aDouble, EvaluationContext evaluationContext) {
        return null;
    }

    @Override
    public ProviderEvaluation<Value> getObjectEvaluation(String s, Value value, EvaluationContext evaluationContext) {
        return null;
    }


    @Override
    public void shutdown() {
        super.shutdown();
    }

    @Override
    public ProviderState getState() {
        if (dummyFlagStore.isReady()) {
            return ProviderState.READY;
        } else {
            return ProviderState.NOT_READY;
        }
    }
}
