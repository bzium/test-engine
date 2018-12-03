package cucumberTraining;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;

public class StepDefinitionTest {

    Logger log = Logger.getLogger(StepDefinitionTest.class);
    String url = "http://www.testdiary.com/";

    @Given("^I have the current testdiary url$")
    public void i_have_the_current_testdiary_url() throws Throwable {

        log.info("my current url is: " + url);
    }

    @When("^I open the testdiary url$")
    public void i_open_the_testdiary_url() throws Throwable {
        log.info("i_open_the_testdiary_url");
        //driver.get(url);

    }

    @Then("^testdiary should be displayed$")
    public void testdiary_should_be_displayed() throws Throwable {
        //String currentUrl = driver.getCurrentUrl();
        //System.out.println(currentUrl);
        log.info(" i have the correct url for test diary");

    }
}
