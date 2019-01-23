package pl.plagodzinski.testengine.core.framework;


public enum TestModuleTypes {

    EXAMPLE1("cucumberTraining.ExampleTestModule");

    private String configurationClassName;

    TestModuleTypes(String configurationClassName) {
        this.configurationClassName = configurationClassName;
    }

    public String getConfigurationClassName() {
        return configurationClassName;
    }
}
