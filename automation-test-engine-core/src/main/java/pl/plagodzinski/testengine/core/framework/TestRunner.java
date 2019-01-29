package pl.plagodzinski.testengine.core.framework;

import lombok.extern.log4j.Log4j;
import org.junit.runner.JUnitCore;
import org.junit.runners.model.InitializationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.plagodzinski.testengine.api.TestModule;
import pl.plagodzinski.testengine.api.ValidateException;
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
        if (configuration.getModules() != null && !configuration.getModules().isEmpty()) {
            configuration.getModules().forEach(moduleName -> {
                Optional<TestModule> filteredModule = testModuleList.stream().filter(module -> module.getName().equals(moduleName)).findFirst();
                if (filteredModule.isPresent()) {
                    runModule(junit, filteredModule.get(), configuration);
                } else {
                    log.error("Not found module with name " + moduleName);
                }
            });
        } else {
            log.info("No set modules to run. Run all modules find in classpath");
            testModuleList.forEach(testModule -> runModule(junit, testModule, configuration));
        }
    }

    private void runModule(JUnitCore jUnitCore, TestModule moduleToRun, Configuration configuration) {
        try {
            log.info("Run validation for module " + moduleToRun.getName());
            moduleToRun.validate();
            log.info("Starting tests for module " + moduleToRun.getName());
            jUnitCore.run(new EngineCucumberRunner(moduleToRun.getClass(), configuration));
        } catch (InitializationError initializationError) {
            log.error("Can't run module " + moduleToRun.getName(), initializationError);
        } catch (ValidateException e) {
            log.error("Can't run module " + moduleToRun.getName() + " due to validation errors", e);
        }
    }
}



