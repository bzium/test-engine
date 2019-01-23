package pl.plagodzinski.testengine.cmd.commands;

import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.springframework.stereotype.Component;
import pl.plagodzinski.testengine.cmd.CmdHelper;
import pl.plagodzinski.testengine.core.config.Configuration;

import java.util.Arrays;

/**
 * Created by pawel on 23/01/2019.
 */

@Component
@Log4j
public class AdditionalTagsCmdCommand implements CmdCommand {
    @Override
    public void execute(CommandLine cmd, Configuration configuration) {
        String[] additionalTags = cmd.getOptionValues("t");
        if (additionalTags != null) {
            log.info("Set additional tags: " + Arrays.toString(additionalTags));
            configuration.setAdditionalTags(Arrays.asList(additionalTags));
        }
    }

    @Override
    public Option getOption() {
        return CmdHelper.buildOption("t", "tag", true, "Set tag(s)");
    }
}
