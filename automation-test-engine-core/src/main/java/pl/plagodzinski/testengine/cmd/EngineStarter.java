package pl.plagodzinski.testengine.cmd;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.plagodzinski.testengine.core.config.spring.ApiConfiguration;
import pl.plagodzinski.testengine.core.config.spring.EngineConfiguration;

/**
 * Created by pawel on 01/12/2018.
 */

public class EngineStarter {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(EngineConfiguration.class);
        ctx.register(ApiConfiguration.class);
        ctx.refresh();

        CmdParser cmdParser = ctx.getBean(CmdParser.class);
        cmdParser.parse(args);
    }
}
