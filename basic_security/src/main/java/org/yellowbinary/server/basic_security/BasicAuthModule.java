package org.yellowbinary.server.basic_security;

import org.yellowbinary.server.core.CoreModule;
import org.yellowbinary.server.core.annotation.Dependency;
import org.yellowbinary.server.core.stereotypes.Module;

import java.util.Collections;
import java.util.List;

@Module(name=BasicAuthModule.NAME, order=90)
@Module.Version(major = 0, minor = 1, patch = 0)
public class BasicAuthModule {

    public static final String NAME = "yellowbinary.basic_security";

    @Module.Dependencies
    public List<Dependency> dependencies() {
        return Collections.singletonList(new Dependency(CoreModule.NAME, 0, 1));
    }

    @Module.Init
    public void init() {
    }
}
