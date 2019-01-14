package pl.plagodzinski.testengine.core.framework;

import org.apache.log4j.Logger;
import org.junit.runner.JUnitCore;
import org.junit.runners.model.InitializationError;
import pl.plagodzinski.testengine.core.config.Configuration;

/**
 * Created by pawel on 01/12/2018.
 */

public class TestRunner {

    private final JUnitCore junit = new JUnitCore();
    private final Logger log = Logger.getLogger(TestRunner.class);

    public void setupAndRunTests(Configuration configuration) {
        try {
            for (TestModuleTypes testType : configuration.getTestTypes()) {
                try {
                    Class<?> testModuleClass = Class.forName(testType.getConfigurationClassName());
                    junit.run(new EngineCucumberRunner(testModuleClass, configuration));
                } catch (ClassNotFoundException e) {
                    log.error("Can't find class with name " + testType.getConfigurationClassName());
                    throw new IllegalStateException("Can't find class with name " + testType.getConfigurationClassName());
                }

            }
        } catch (InitializationError e) {
            log.error(e);
        }
    }
}



