package pl.plagodzinski.testengine.cmd;

import org.apache.commons.cli.Option;

/**
 * Created by pawel on 23/01/2019.
 */
public class CmdHelper {
    public static Option buildOption(String opt, String longOpt, Boolean hasArg, String desc) {
        Option option = new Option(opt, longOpt, hasArg, desc);
        if (hasArg) {
            option.setArgs(Option.UNLIMITED_VALUES);
            option.setValueSeparator(' ');
        }
        return option;
    }
}
