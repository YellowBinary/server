package org.yellowbinary.server.core;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.yellowbinary.server.core.annotation.AnnotationProcessor;
import org.yellowbinary.server.core.dao.ConfigurationDao;
import org.yellowbinary.server.core.model.RootNode;
import org.yellowbinary.server.core.stereotypes.Module;
import org.yellowbinary.server.core.stereotypes.OnLoad;
import org.yellowbinary.server.core.stereotypes.Provides;

import java.util.List;
import java.util.Map;

@Module(name = CoreModule.NAME, order = 0)
@Module.Version(major = 0, minor = 1, patch = 0)
public class CoreModule {

    public static final String NAME = "yellowbinary.core";

    @Autowired
    private ConfigurationDao configurationDao;

    @Module.Init
    public void init() {
        //configurationDao.setValueIfMissing(CoreConfigurationKeys.SUBMIT_HANDLER, DefaultSubmitHandler.class.getName());
        //configurationDao.setValueIfMissing(CoreConfigurationKeys.DEFAULT_FORM_TYPE, DefaultFormProvider.TYPE);

        //Formats.register();

        //EncryptionHelper.register();
        //SessionHelper.register();
    }

    @Module.Annotations
    public List<AnnotationProcessor.Prototype> annotations() {
        List<AnnotationProcessor.Prototype> annotations = Lists.newArrayList();

        // Basic types
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, RootNode.class, String.class));
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, RootNode.class, String.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class));
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class, Map.class));

/*
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class, Form.class));
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class, Form.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class, Block.class));
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class, Block.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class, Block.class));
        annotations.add(new AnnotationProcessor.Prototype(Provides.class, Object.class, Node.class, String.class, Block.class, Map.class));
*/

        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Map.class));

/*
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Navigation.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Navigation.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Form.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Form.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Form.class, Element.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Form.class, Element.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Element.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Element.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Navigation.class, NavigationElement.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Navigation.class, NavigationElement.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Text.class));
        annotations.add(new AnnotationProcessor.Prototype(OnLoad.class, null, Node.class, String.class, Text.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnInsertElement.class, null, Element.class, Element.class));
        annotations.add(new AnnotationProcessor.Prototype(OnInsertElement.class, null, Node.class, Element.class, Element.class));
        annotations.add(new AnnotationProcessor.Prototype(OnInsertElement.class, null, Element.class, Element.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnInsertElement.class, null, Node.class, Element.class, Element.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnRemoveElement.class, null, Element.class, Element.class));
        annotations.add(new AnnotationProcessor.Prototype(OnRemoveElement.class, null, Node.class, Element.class, Element.class));
        annotations.add(new AnnotationProcessor.Prototype(OnRemoveElement.class, null, Element.class, Element.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnRemoveElement.class, null, Node.class, Element.class, Element.class, Map.class));

        // Form handling types
        annotations.add(new AnnotationProcessor.Prototype(OnSubmit.class, Boolean.class));
        annotations.add(new AnnotationProcessor.Prototype(OnSubmit.class, Boolean.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnSubmit.class, Boolean.class, Form.class));
        annotations.add(new AnnotationProcessor.Prototype(OnSubmit.class, Boolean.class, Form.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(SubmitHandler.class, Result.class));
        annotations.add(new AnnotationProcessor.Prototype(SubmitHandler.class, Result.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Processing.class, Validation.Result.class, String.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Processing.class, Validation.Result.class, String.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Failure.class, Result.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Failure.class, Result.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Failure.class, Result.class, String.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Failure.class, Result.class, String.class, Validation.Result.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Failure.class, Result.class, String.class, Validation.Result.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Failure.class, Result.class, String.class, String.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Failure.class, Result.class, String.class, String.class, Validation.Result.class));
        annotations.add(new AnnotationProcessor.Prototype(Validation.Failure.class, Result.class, String.class, String.class, Validation.Result.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(SubmitState.class, Result.class));
        annotations.add(new AnnotationProcessor.Prototype(SubmitState.class, Result.class, Map.class));

        // Data types
        annotations.add(new AnnotationProcessor.Prototype(OnCreate.class, null, Object.class));
        annotations.add(new AnnotationProcessor.Prototype(OnCreate.class, null, Object.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnUpdate.class, null, Object.class));
        annotations.add(new AnnotationProcessor.Prototype(OnUpdate.class, null, Object.class, Map.class));
        annotations.add(new AnnotationProcessor.Prototype(OnDelete.class, null, Object.class));
        annotations.add(new AnnotationProcessor.Prototype(OnDelete.class, null, Object.class, Map.class));
*/

        return annotations;
    }
}
