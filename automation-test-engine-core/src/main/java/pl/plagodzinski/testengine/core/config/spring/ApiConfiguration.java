package pl.plagodzinski.testengine.core.config.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import pl.plagodzinski.testengine.api.TestModule;

/**
 * Created by pawel on 24/01/2019.
 */

@org.springframework.context.annotation.Configuration
@ComponentScan(basePackages = {"cucumberTraining"}, includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = TestModule.class), useDefaultFilters = false)
public class ApiConfiguration { }
