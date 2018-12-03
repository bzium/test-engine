package pl.plagodzinski.testengine.core.framework;

import org.apache.log4j.Logger;
import org.junit.runner.JUnitCore;
import org.junit.runners.model.InitializationError;
import pl.plagodzinski.testengine.core.config.Configuration;

import java.io.IOException;

/**
 * Created by pawel on 01/12/2018.
 */

public class TestRunner {

    private final JUnitCore junit = new JUnitCore();
    private final Logger log = Logger.getLogger(TestRunner.class);

    public void setupAndRunTests(Configuration configuration) {
        try {
            for (Class<?> aClass : configuration.getClassList()) {
                junit.run(new EngineCucumberRunner(aClass, configuration));
            }
        } catch (InitializationError|IOException e) {
            log.error(e);
        }
    }
}



