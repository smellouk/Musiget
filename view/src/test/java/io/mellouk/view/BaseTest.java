package io.mellouk.view;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.times;

public abstract class BaseTest {
    static VerificationMode once = times(1);
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public RxImmediateSchedulerRule testSchedulerRule = new RxImmediateSchedulerRule();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}
