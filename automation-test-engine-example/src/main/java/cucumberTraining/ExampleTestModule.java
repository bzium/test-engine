package cucumberTraining;

import org.springframework.stereotype.Component;
import pl.plagodzinski.testengine.api.TestModule;

/**
 * Created by pawel on 02/12/2018.
 */

@Component
public class ExampleTestModule implements TestModule{
    @Override
    public String getName() {
        return "EXAMPLE";
    }
}
