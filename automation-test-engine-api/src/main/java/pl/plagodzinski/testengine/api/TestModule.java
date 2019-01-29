package pl.plagodzinski.testengine.api;

/**
 * Created by pawel on 23/01/2019.
 */
public interface TestModule {
    /*
        Get name of module
     */
    String getName();

    /*
        Method used to put validate conditions.
        Example:
        Module need mycondition1 property from system properties to correct work.
        When override this method you can check if mycondition1 is available in system properties
        If it is not set you should throw ValidateException
     */

    void validate() throws ValidateException;
}
