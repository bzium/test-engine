package pl.plagodzinski.testengine.cmd;

/**
 * Created by pawel on 01/12/2018.
 */
public class EngineStarter {
    public static void main(String[] args) {
        if(args.length > 0) {
            CmdParser cmdParser = new CmdParser(args);
            cmdParser.parse();
        }
    }
}
