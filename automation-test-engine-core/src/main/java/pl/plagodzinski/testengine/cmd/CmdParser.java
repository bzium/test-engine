package pl.plagodzinski.testengine.cmd;

import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.*;
import pl.plagodzinski.testengine.core.config.Configuration;
import pl.plagodzinski.testengine.core.config.Country;
import pl.plagodzinski.testengine.core.config.Environment;
import pl.plagodzinski.testengine.core.framework.AutomationTestEngine;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pawel on 01/12/2018.
 */
@Log4j
class CmdParser {
    private String[] args = null;
    private Options options = new Options();

    CmdParser(String[] args) {

        this.args = args;
        options.addOption(buildOption("h", "help", false, "Show help."));
        options.addOption(buildOption("c", "country", true, "Set test country"));
        options.addOption(buildOption("e", "environment", true, "Set test environment"));
        options.addOption(buildOption("t", "tags", true, "Set tags"));
        options.addOption(buildOption("x", "testClass", true, "Set test class"));
    }

    void parse() {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("h"))
                help();
            else if (cmd.hasOption("c") || cmd.hasOption("e") || cmd.hasOption("t") || cmd.hasOption("x")) {
                Configuration configuration = new Configuration();

                String[] countries = cmd.getOptionValues("c");
                if (countries != null) {
                    List<String> countryStringList = Arrays.asList(countries);
                    log.info("Set countries: " + Arrays.toString(countries));
                    configuration.setCountries(countryStringList.stream().map(Country::valueOf).collect(Collectors.toList()));
                }

                String[] environment = cmd.getOptionValues("e");
                if (environment != null) {
                    log.info("Set environments: " + Arrays.toString(environment));
                    List<String> environmentList = Arrays.asList(environment);
                    configuration.setEnvironments(environmentList.stream().map(Environment::valueOf).collect(Collectors.toList()));
                }

                String[] additionalTags = cmd.getOptionValues("t");
                if (additionalTags != null) {
                    log.info("Set additional tags: " + Arrays.toString(additionalTags));
                    configuration.setAdditionalTags(Arrays.asList(additionalTags));
                }

                String[] testClassNames = cmd.getOptionValues("x");
                if (testClassNames != null) {
                    List<String> classNames = Arrays.asList(testClassNames);
                    log.info("Set classes: " + Arrays.toString(testClassNames));
                    List<Class<?>> classList = classNames.stream().map(className -> {
                        try {
                            return Class.forName(className);
                        } catch (ClassNotFoundException e) {
                            log.error("Can't find class with name " + className);
                            throw new IllegalStateException("Can't find class with name " + className);
                        }
                    }).collect(Collectors.toList());

                    configuration.setClassList(classList);
                }

                AutomationTestEngine automationTestEngine = new AutomationTestEngine(configuration);
                automationTestEngine.runTests();
            } else {
                log.info("Show help");
                help();
            }
        } catch (ParseException e) {
            log.info("");
            help();
        }
    }

    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("automation-test-engine", options);
        System.exit(0);
    }

    private Option buildOption(String opt, String longOpt, Boolean hasArg, String desc) {
        Option option = new Option(opt, longOpt, hasArg, desc);
        if (hasArg) {
            option.setArgs(Option.UNLIMITED_VALUES);
            option.setValueSeparator(' ');
        }
        return option;
    }

}
