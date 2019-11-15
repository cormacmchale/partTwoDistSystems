package partTwods;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class Appmain extends Application<WebConfiguration> {

    public static void main(String[] args) throws Exception {
        new Appmain().run(args);
    }

    public void run(WebConfiguration webconfig, Environment environment) throws Exception {
        environment.jersey().register(new UserAPI(environment.getValidator()));
    }
}
