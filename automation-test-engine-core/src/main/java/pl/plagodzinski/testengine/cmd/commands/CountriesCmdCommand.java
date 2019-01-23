package pl.plagodzinski.testengine.cmd.commands;

import lombok.extern.log4j.Log4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.springframework.stereotype.Component;
import pl.plagodzinski.testengine.cmd.CmdHelper;
import pl.plagodzinski.testengine.core.config.Configuration;
import pl.plagodzinski.testengine.core.config.Country;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by pawel on 23/01/2019.
 */

@Log4j
@Component
public class CountriesCmdCommand implements CmdCommand {

    @Override
    public void execute(CommandLine cmd, Configuration configuration) {
        String[] countries = cmd.getOptionValues("c");
        if (countries != null) {
            List<String> countryStringList = Arrays.asList(countries);
            log.info("Set countries: " + Arrays.toString(countries));
            configuration.setCountries(countryStringList.stream().map(Country::valueOf).collect(Collectors.toList()));
        }
    }

    @Override
    public Option getOption() {
        return CmdHelper.buildOption("c", "country", true, "Set test country(ies)");
    }
}
