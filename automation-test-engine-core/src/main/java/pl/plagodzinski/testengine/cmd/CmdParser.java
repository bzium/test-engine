package pl.plagodzinski.testengine.cmd;

import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.plagodzinski.testengine.cmd.commands.CmdCommand;
import pl.plagodzinski.testengine.core.config.Configuration;
import pl.plagodzinski.testengine.core.framework.AutomationTestEngine;

import java.util.List;

/**
 * Created by pawel on 01/12/2018.
 */
@Log4j
@Component
class CmdParser {
    private Options options = new Options();

    private List<CmdCommand> commandList;
    private AutomationTestEngine engine;

    @Autowired
    CmdParser(List<CmdCommand> commandList, AutomationTestEngine engine) {
        this.commandList = commandList;
        this.engine = engine;
    }

    void parse(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            commandList.forEach(cl -> options.addOption(cl.getOption()));

            CommandLine cmd = parser.parse(options, args);
            Configuration configuration = new Configuration();

            commandList.forEach(cl2 -> cl2.execute(cmd, configuration));
            engine.runTests(configuration);
        } catch (ParseException e) {
            help();
        }
    }

    private void help() {
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("automation-test-engine", options);
        System.exit(0);
    }

}
