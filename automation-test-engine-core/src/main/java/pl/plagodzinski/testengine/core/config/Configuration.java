package pl.plagodzinski.testengine.core.config;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by pawel on 01/12/2018.
 */

@Getter
@Setter
public class Configuration {
    private List<Country> countries;
    private List<Environment> environments;
    private List<String> additionalTags;
    private List<String> modules;
}
