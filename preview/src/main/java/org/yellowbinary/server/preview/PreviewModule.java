package org.yellowbinary.server.preview;

import org.yellowbinary.server.core.CoreModule;
import org.yellowbinary.server.core.annotation.AnnotationProcessor;
import org.yellowbinary.server.core.stereotypes.Module;

import java.util.Collections;
import java.util.List;

@Module(name=PreviewModule.NAME, order=100)
@Module.Version(major = 0, minor = 1, patch = 0)
public class PreviewModule {

    public static final String NAME = "origo.preview";

    @Module.Dependencies
    public static List<AnnotationProcessor.Dependency> dependencies() {
        return Collections.singletonList(new AnnotationProcessor.Dependency(CoreModule.NAME, 0, 1));
    }

}