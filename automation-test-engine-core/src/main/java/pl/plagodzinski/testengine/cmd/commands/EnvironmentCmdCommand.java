package pl.plagodzinski.testengine.cmd.commands;

import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import pl.plagodzinski.testengine.cmd.CmdHelper;
import pl.plagodzinski.testengine.core.config.Configuration;
import pl.plagodzinski.testengine.core.config.Environment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pawel on 23/01/2019.
 */

@Log4j
public class EnvironmentCmdCommand implements CmdCommand {

    @Override
    public void execute(CommandLine cmd, Configuration configuration) {
        String[] environment = cmd.getOptionValues("e");
        if (environment != null) {
            log.info("Set environments: " + Arrays.toString(environment));
            List<String> environmentList = Arrays.asList(environment);
            configuration.setEnvironments(environmentList.stream().map(Environment::valueOf).collect(Collectors.toList()));
        }
    }

    @Override
    public Option getOption() {
        return CmdHelper.buildOption("e", "environment", true, "Set test environment(s)");
    }
}
