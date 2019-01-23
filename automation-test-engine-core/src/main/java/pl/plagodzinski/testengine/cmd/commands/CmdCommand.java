package pl.plagodzinski.testengine.cmd.commands;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import pl.plagodzinski.testengine.core.config.Configuration;

/**
 * Created by pawel on 23/01/2019.
 */

public interface CmdCommand {
    void execute(CommandLine cmd, Configuration configuration);
    Option getOption();
}
