package pl.plagodzinski.testengine.core.framework;

import lombok.AllArgsConstructor;
import pl.plagodzinski.testengine.core.config.Configuration;

/**
 * Created by pawel on 01/12/2018.
 */

@AllArgsConstructor
public class AutomationTestEngine {

    private Configuration configuration;

    public void runTests() {
        TestRunner testRunner = new TestRunner();
        testRunner.setupAndRunTests(configuration);
    }
}
