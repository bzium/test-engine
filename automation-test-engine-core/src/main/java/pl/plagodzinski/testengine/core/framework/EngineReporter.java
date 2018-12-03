package pl.plagodzinski.testengine.core.framework;

import cucumber.runtime.StepDefinitionMatch;
import gherkin.formatter.Argument;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.Match;
import gherkin.formatter.model.Result;
import lombok.extern.log4j.Log4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pawel on 02/12/2018.
 */

@Log4j
public class EngineReporter implements Reporter {

    @Override
    public void before(Match match, Result result) {

    }

    @Override
    public void result(Result result) {

    }

    @Override
    public void after(Match match, Result result) {

    }

    @Override
    public void match(Match match) {
        StepDefinitionMatch stepDefinitionMatch = (StepDefinitionMatch)match;
        List<String> params = stepDefinitionMatch.getArguments().stream().map(Argument::getVal).collect(Collectors.toList());
        log.info("Executed step name: \'" + stepDefinitionMatch.getStepName() + "\' with params " + params);
    }

    @Override
    public void embedding(String s, byte[] bytes) {

    }

    @Override
    public void write(String s) {

    }
}
