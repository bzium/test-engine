package pl.plagodzinski.testengine.core.framework;

import lombok.extern.log4j.Log4j;
import org.junit.runner.JUnitCore;
import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.plagodzinski.testengine.api.TestModule;
import pl.plagodzinski.testengine.core.config.Configuration;

import java.util.List;
import java.util.Optional;

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
        log.info(String.format("Find %s test module(s) in classpath", testModuleList.size()));
        JUnitCore junit = new JUnitCore();
        if (configuration.getModules() != null) {
            configuration.getModules().forEach(moduleName -> {
                Optional<TestModule> filteredModule = testModuleList.stream().filter(module -> module.getName().equals(moduleName)).findFirst();
                if (filteredModule.isPresent()) {
                    log.info("Starting tests for module " + filteredModule.get().getName());
                    try {
                        junit.run(new EngineCucumberRunner(filteredModule.get().getClass(), configuration));
                    } catch (InitializationError initializationError) {
                        log.error("Can't run module " + moduleName, initializationError);
                    }
                } else {
                    log.error("Not found module with name " + moduleName);
                }
            });
        } else {
            log.info("Not set any module to run");
        }

    }
}



