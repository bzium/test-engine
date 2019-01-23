package pl.plagodzinski.testengine.core.framework;

import lombok.extern.log4j.Log4j;
import org.junit.runner.JUnitCore;
import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.plagodzinski.testengine.api.TestModule;
import pl.plagodzinski.testengine.core.config.Configuration;

import java.util.List;

/**
 * Created by pawel on 01/12/2018.
 */
@Component
@Log4j
public class TestRunner {

    private List<TestModule> testModuleList;

    @Autowired
    public TestRunner(List<TestModule> testModuleList) {
        this.testModuleList = testModuleList;
    }

    void setupAndRunTests(Configuration configuration) {
        JUnitCore junit = new JUnitCore();
        configuration.getModules().forEach(moduleName -> {
            testModuleList.forEach(module -> {
                if(module.getName().equals(moduleName)) {
                    try {
                        junit.run(new EngineCucumberRunner(module.getClass(), configuration));
                    } catch (InitializationError initializationError) {
                        log.error("Can't run module " + moduleName, initializationError);
                    }
                }
            });
        });
    }
}



