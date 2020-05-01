package io.mellouk.repository;

import org.junit.Before;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.times;

public abstract class BaseTest {
    protected static VerificationMode once = times(1);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
}
