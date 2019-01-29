package pl.plagodzinski.testengine.cmd.commands;

import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.springframework.stereotype.Component;
import pl.plagodzinski.testengine.cmd.CmdHelper;
import pl.plagodzinski.testengine.core.config.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pawel on 23/01/2019.
 */

@Component
@Log4j
public class ModuleCmdCommand implements CmdCommand {

    @Override
    public void execute(CommandLine cmd, Configuration configuration) {
        String[] moduleNames = cmd.getOptionValues("m");
        if(moduleNames != null) {
            log.info("Set test modules: " + Arrays.toString(moduleNames));
            List<String> moduleToUse = Arrays.asList(moduleNames);
            configuration.setModules(moduleToUse);
        }
    }

    @Override
    public Option getOption() {
        return CmdHelper.buildOption("m", "test_module", true, "Set test module(s)");
    }
}
