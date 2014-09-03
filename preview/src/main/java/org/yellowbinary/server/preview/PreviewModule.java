package org.yellowbinary.server.preview;

import org.yellowbinary.server.backend.BackendModule;
import org.yellowbinary.server.core.annotation.Dependency;
import org.yellowbinary.server.core.stereotypes.Module;

import java.util.Collections;
import java.util.List;

@Module(name=PreviewModule.NAME, order=100)
@Module.Version(major = 0, minor = 1, patch = 0)
public class PreviewModule {

    public static final String NAME = "yellowbinary.preview";

    @Module.Dependencies
    public static List<Dependency> dependencies() {
        return Collections.singletonList(new Dependency(BackendModule.NAME, 0, 1));
    }

}
