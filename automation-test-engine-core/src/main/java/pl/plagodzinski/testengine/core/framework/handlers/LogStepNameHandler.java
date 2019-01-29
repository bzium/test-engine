package pl.plagodzinski.testengine.core.framework.handlers;

import cucumber.api.event.EventHandler;
import cucumber.api.event.TestStepStarted;
import lombok.extern.log4j.Log4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pawel on 29/01/2019.
 */

@Log4j
public class LogStepNameHandler implements EventHandler<TestStepStarted> {
    @Override
    public void receive(TestStepStarted testStepStarted) {
        List<String> params = testStepStarted.testStep.getStepArgument().stream().map(Object::toString).collect(Collectors.toList());
        log.info("Executed step name: \'" + testStepStarted.testStep.getPattern() + "\' with arguments " + params);
    }
}
