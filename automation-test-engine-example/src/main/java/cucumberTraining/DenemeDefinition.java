package cucumberTraining;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.log4j.Logger;

/**
 * Created by celik.gumusdag on 01.06.2016.
 */

public class DenemeDefinition {

    String url = "http://www.google.com/";
    private Logger log = Logger.getLogger(DenemeDefinition.class);

    @Given("^Kontrol given$")
    public void kontrolGiven() throws Throwable {
        log.info("my current url is: " + url);
    }

    @When("^kontrol when go$")
    public void kontrolWhenGo() throws Throwable {
        log.info("kontrolWhenGo");
        //driver.get(url);
    }

    @Then("^kontrol then go$")
    public void kontrolThenGo() throws Throwable {
        //String currentUrl = driver.getCurrentUrl();
        //System.out.println(currentUrl);
        log.info(" i have the correct url for test diary");
    }
}
