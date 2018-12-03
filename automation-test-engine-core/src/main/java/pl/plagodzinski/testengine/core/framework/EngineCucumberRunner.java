package pl.plagodzinski.testengine.core.framework;

import cucumber.runtime.ClassFinder;
import cucumber.runtime.Runtime;
import cucumber.runtime.RuntimeOptions;
import cucumber.runtime.RuntimeOptionsFactory;
import cucumber.runtime.io.MultiLoader;
import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.io.ResourceLoaderClassFinder;
import cucumber.runtime.junit.FeatureRunner;
import cucumber.runtime.junit.JUnitOptions;
import cucumber.runtime.junit.JUnitReporter;
import cucumber.runtime.model.CucumberFeature;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FilenameUtils;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.ParentRunner;
import org.junit.runners.model.InitializationError;
import pl.plagodzinski.testengine.core.config.Configuration;
import pl.plagodzinski.testengine.core.config.Country;
import pl.plagodzinski.testengine.core.config.Environment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by pawel on 01/12/2018.
 */

@Log4j
public class EngineCucumberRunner extends ParentRunner<FeatureRunner> {
    private final JUnitReporter jUnitReporter;
    private final List<FeatureRunner> children = new ArrayList<FeatureRunner>();
    private final Runtime runtime;
    private final Configuration conf;

    public EngineCucumberRunner(Class clazz, Configuration conf) throws InitializationError, IOException {
        super(EngineCucumberRunner.class);
        this.conf = conf;
        ClassLoader classLoader = clazz.getClassLoader();

        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

        // Add customized reporter
        runtimeOptions.addPlugin(new EngineReporter());

        //Add tags to Runner class
        addRunnerTag(runtimeOptions);

        ResourceLoader resourceLoader = new MultiLoader(classLoader);
        runtime = createRuntime(resourceLoader, classLoader, runtimeOptions);

        final List<CucumberFeature> cucumberFeatures = runtimeOptions.cucumberFeatures(resourceLoader);

        List<CucumberFeature> filteredCucumberFeatures;
        if(conf.getCountries() == null || conf.getCountries().isEmpty()) {
            filteredCucumberFeatures = cucumberFeatures.stream().filter(cf -> checkPath(cf.getPath())).collect(Collectors.toList());
        } else {
            filteredCucumberFeatures = cucumberFeatures;
        }

        //addFeatureTag(filteredCucumberFeatures);

        JUnitOptions jUnitOptions = new JUnitOptions(Collections.EMPTY_LIST);
        jUnitReporter = new JUnitReporter(runtimeOptions.reporter(classLoader), runtimeOptions.formatter(classLoader), runtimeOptions.isStrict(), jUnitOptions);
        addChildren(filteredCucumberFeatures);
    }

    /**
     * Create the Runtime. Can be overridden to customize the runtime or backend.
     *
     * @param resourceLoader used to load resources
     * @param classLoader    used to load classes
     * @param runtimeOptions configuration
     * @return a new runtime
     * @throws InitializationError if a JUnit error occurred
     * @throws IOException if a class or resource could not be loaded
     */
    protected Runtime createRuntime(ResourceLoader resourceLoader, ClassLoader classLoader,
                                    RuntimeOptions runtimeOptions) throws InitializationError, IOException {
        ClassFinder classFinder = new ResourceLoaderClassFinder(resourceLoader, classLoader);
        return new Runtime(resourceLoader, classFinder, classLoader, runtimeOptions);
    }

    @Override
    public List<FeatureRunner> getChildren() {
        return children;
    }

    @Override
    protected Description describeChild(FeatureRunner child) {
        return child.getDescription();
    }

    @Override
    protected void runChild(FeatureRunner child, RunNotifier notifier) {
        child.run(notifier);
    }

    @Override
    public void run(RunNotifier notifier) {
        super.run(notifier);
        jUnitReporter.done();
        jUnitReporter.close();
        runtime.printSummary();
    }

    private void addChildren(List<CucumberFeature> cucumberFeatures) throws InitializationError {
        for (CucumberFeature cucumberFeature : cucumberFeatures) {
            children.add(new FeatureRunner(cucumberFeature, runtime, jUnitReporter));
        }
    }

    private void addRunnerTag(RuntimeOptions runtimeOptions) {
        if(conf.getEnvironments() != null) {
            for(Environment envs: conf.getEnvironments()) {
                runtimeOptions.getFilters().add("@" + envs.toString());
            }
        }

        if(conf.getAdditionalTags() != null) {
            for(String additionalTag : conf.getAdditionalTags()) {
                runtimeOptions.getFilters().add("@" + additionalTag);
            }
        }

    }

    private boolean checkPath(String path) {
        Optional<Country> countryOptional =
        conf.getCountries().stream().filter(c -> FilenameUtils.getBaseName(path).endsWith(c.name())).findAny();
        return countryOptional.isPresent();
    }

    /*
    private void addFeatureTag(List<CucumberFeature> cucumberFeatures){

        final String tagToAdd = System.getProperty("dynamic.feature.tag.add","");
        if(!tagToAdd.isEmpty()){
            for(CucumberFeature cucumberFeature : cucumberFeatures) {
                cucumberFeature.getGherkinFeature().getTags().add(new gherkin.formatter.model.Tag(tagToAdd, 0));
            }
        }
    }
    */
}
