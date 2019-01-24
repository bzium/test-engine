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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
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
    private final List<FeatureRunner> children = new ArrayList<>();
    private final Runtime runtime;
    private final Configuration conf;

    public EngineCucumberRunner(Class clazz, Configuration conf) throws InitializationError {
        super(EngineCucumberRunner.class);
        this.conf = conf;
        ClassLoader classLoader = clazz.getClassLoader();

        RuntimeOptionsFactory runtimeOptionsFactory = new RuntimeOptionsFactory(clazz);
        RuntimeOptions runtimeOptions = runtimeOptionsFactory.create();

        // Add futures path inside jar
        runtimeOptions.getFeaturePaths().add("classpath:features/");

        // Add package with cucumber steps
        //runtimeOptions.getGlue().add(clazz.getPackage().getName());

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

        List<String> featuresInJar = listFeatureFiles(clazz);
        filteredCucumberFeatures = filteredCucumberFeatures.stream().filter(cf -> isStoreInJar(cf.getPath(), featuresInJar)).collect(Collectors.toList());

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
     */

    protected Runtime createRuntime(ResourceLoader resourceLoader, ClassLoader classLoader,
                                    RuntimeOptions runtimeOptions) {
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

    private List<String> getResources(URI uri) {
        List<String> list = new ArrayList<>();
        try (FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap())) {
            Path myPath = Paths.get(uri);
            Files.walkFileTree(myPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    list.add(file.getFileName().toString());
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            log.error("Can't read file", e);
        }
        return list;
    }

    private URI buildUri(Class clazz)  {
        try {
            return new URI("jar:" + clazz.getProtectionDomain().getCodeSource().getLocation().toURI().toString() + "!/features");
        } catch (URISyntaxException e) {
            log.error("Can't build uri", e);
            return null;
        }

    }

    private List<String> listFeatureFiles(Class clazz) {
        URI uri = buildUri(clazz);
        if(uri != null) {
            return getResources(uri);
        }
        return new ArrayList<>();
    }

    private boolean checkPath(String path) {
        Optional<Country> countryOptional =
        conf.getCountries().stream().filter(c -> FilenameUtils.getBaseName(path).endsWith(c.name())).findAny();
        return countryOptional.isPresent();
    }

    private boolean isStoreInJar(String path, List<String> featuresInJar) {
        Path p = Paths.get(path);
        return featuresInJar.contains(p.getFileName().toString());
    }
}
