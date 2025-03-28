package portfolio.umang;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import portfolio.umang.resource.UserResource;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class LdapSampleApplication extends Application {

    private final Set<Class<?>> classes;

    public LdapSampleApplication() {
        this.classes = new HashSet<>();
        this.classes.add(UserResource.class);
    }

    @Override
    public Set<Class<?>> getClasses() {
        return this.classes;
    }
}
