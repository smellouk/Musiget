package io.mellouk.view;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class RxImmediateSchedulerRule implements TestRule {
    private final Scheduler scheduler = Schedulers.trampoline();

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                RxJavaPlugins.setInitIoSchedulerHandler(schedulerCallable -> scheduler);
                RxJavaPlugins.setIoSchedulerHandler(schedulerCallable -> scheduler);
                RxJavaPlugins.setInitComputationSchedulerHandler(schedulerCallable -> scheduler);
                RxJavaPlugins.setComputationSchedulerHandler(schedulerCallable -> scheduler);
                RxJavaPlugins.setInitNewThreadSchedulerHandler(schedulerCallable -> scheduler);
                RxJavaPlugins.setInitSingleSchedulerHandler(schedulerCallable -> scheduler);
                RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> scheduler);

                try {
                    base.evaluate();
                } finally {
                    RxJavaPlugins.reset();
                    RxAndroidPlugins.reset();
                }
            }
        };
    }
}
