package org.yellowbinary.server.preview.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.yellowbinary.server.core.Core;
import org.yellowbinary.server.core.ModuleException;
import org.yellowbinary.server.core.Node;
import org.yellowbinary.server.core.NodeLoadException;
import org.yellowbinary.server.core.context.Context;
import org.yellowbinary.server.core.model.Meta;
import org.yellowbinary.server.core.model.content.Text;
import org.yellowbinary.server.core.preview.Ticket;
import org.yellowbinary.server.core.stereotypes.Interceptor;
import org.yellowbinary.server.core.stereotypes.OnLoad;
import org.yellowbinary.server.core.stereotypes.Provides;
import org.yellowbinary.server.preview.model.BasicTicket;
import org.yellowbinary.server.preview.service.PreviewService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Interceptor
public class PreviewTokenInterceptor {

    private static final String DATETIME_PARAM = "date";

    private static final String COMMENT_LOADED = "preview_comment_loaded";

    @Autowired
    private PreviewService previewService;

/*
    @OnLoad(with = Core.With.PREVIEW_CREATE)
    public static void addPreviewForm(Node node, String withType) throws ModuleException, NodeLoadException {

        Form<PreviewTokenForm> form = FormHelper.getValidationResult(PreviewTokenForm.class);

        FormHelper.createFormElement(node, withType, form);
    }

    @OnLoad(type = Core.Type.FORM, with = Core.With.PREVIEW_CREATE, after = true)
    public static void addPreviewForm(Node node, String withType, Form form, Element element, Map<String, Object> args) {

        element.setId("previewtokenform").addAttribute("class", "origo-previewtokenform, form");

        Element globalErrors = FormHelper.createGlobalErrorElement();
        if (globalErrors != null) {
            element.addChild(globalErrors);
        }

        Element previewFieldSet = new Element.FieldSet().setId("preview");
        element.addChild(previewFieldSet);

        previewFieldSet.addChild(new Element.Legend().setBody("Preview"));

        previewFieldSet.addChild(new Element.Container().
                addChild(FormHelper.createField(
                        form,
                        new Element.Label().setWeight(10).setBody("Date").addAttribute("for", DATETIME_PARAM),
                        new Element.InputText().setWeight(20).addAttribute("name", DATETIME_PARAM)).
                        setWeight(10).
                        addChild(
                                new Element.Help().setWeight(1000).setBody(Messages.get("date.time.format"))
                        )
                )
        );

        element.addChild(new Element.Container().setId("actions").setWeight(1000).
                addChild(new Element.Container().
                        addChild(new Element.SubmitButton().setWeight(10).setBody("Create"))
                ));
    }

    @OnSubmit(with = Core.With.PREVIEW_CREATE, validate = PreviewTokenForm.class)
    public static Boolean handlePreviewSubmit(Form<PreviewTokenForm> loginForm) throws NodeLoadException, ModuleException {

        Form<PreviewTokenForm> form = FormHelper.getValidationResult(PreviewTokenForm.class);

        BasicTicket basicTicket = PreviewTicketHelper.updateTicket(form.get().getPreview());
        if (basicTicket != null) {
            return true;
        }

        basicTicket = PreviewTicketHelper.createNewTicket(form.get().getPreview());
        return basicTicket != null;
    }
*/

    @OnLoad(base = Core.Base.NODE)
    public void loadNode(Node node, String withType, Map<String, Object> args) throws ModuleException, NodeLoadException {
        if (!Context.current().hasAttribute(COMMENT_LOADED) && previewService.hasTicket() && previewService.verifyCurrent()) {
            node.addChild(new Text(getComment()), Meta.defaultMeta());
            Context.current().addAttribute(COMMENT_LOADED, true);
        }
    }

    private String getComment() throws ModuleException, NodeLoadException {
        BasicTicket basicTicket = previewService.getCurrent();
        return "Preview-Ticket { token: \""+ basicTicket.getToken()+"\", valid-until: \"\\/Date("+ basicTicket.getValidUntilDateTime().getMillis()+")\\/\" }";
    }

    @Provides(base = Core.Base.PREVIEW, with = Core.With.PREVIEW_TOKEN)
    public Ticket getCurrentToken(Node node, String withType, Map<String, Object> args) throws ModuleException, NodeLoadException {
        return previewService.getCurrent();
    }
}
