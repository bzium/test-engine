package pl.plagodzinski.testengine.core.framework;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import cucumber.api.event.EventPublisher;
import cucumber.api.event.TestStepStarted;
import cucumber.api.formatter.Formatter;
import lombok.extern.log4j.Log4j;

/**
 * Created by pawel on 02/12/2018.
 */

@Log4j
public class EngineReporter implements Formatter {

    private JiraRestClient jiraRestClient;

    EngineReporter() {
        JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
        //jiraRestClient = factory.createWithBasicHttpAuthentication(uri, JIRA_ADMIN_USERNAME, JIRA_ADMIN_PASSWORD);
    }
    /*
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
    */
    @Override
    public void setEventPublisher(EventPublisher eventPublisher) {
        eventPublisher.registerHandlerFor(TestStepStarted.class, (event) -> {
            log.info("Step text " + event.testStep.getStepText());
        });
    }
}
