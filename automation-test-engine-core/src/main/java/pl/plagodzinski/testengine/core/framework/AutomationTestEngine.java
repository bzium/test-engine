package pl.plagodzinski.testengine.core.framework;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.plagodzinski.testengine.core.config.Configuration;

/**
 * Created by pawel on 01/12/2018.
 */

@Component
public class AutomationTestEngine {

    private TestRunner testRunner;

    @Autowired
    AutomationTestEngine(TestRunner testRunner) {
        this.testRunner = testRunner;
    }

    public void runTests(Configuration configuration) {
        testRunner.setupAndRunTests(configuration);
    }
}
