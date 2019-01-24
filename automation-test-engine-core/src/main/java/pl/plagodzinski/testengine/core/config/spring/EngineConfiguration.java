package pl.plagodzinski.testengine.core.config.spring;

import org.springframework.context.annotation.ComponentScan;

/**
 * Created by pawel on 23/01/2019.
 */
@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = { "pl.plagodzinski.testengine" })
public class EngineConfiguration {
}
