package com.example.votingapp.browser.actions;

import com.vaadin.ui.Notification;
import info.magnolia.context.MgnlContext;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.ui.ValueContext;
import info.magnolia.ui.api.action.AbstractAction;
import info.magnolia.ui.api.action.Action;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.net.URI;
import java.net.URISyntaxException;

public class CopyLinkAction extends AbstractAction<CopyLinkActionDefinition> implements Action {
    private static final String TEMPLATE_NAME = "Poll-page";
    private final SimpleTranslator translator;
    private final ValueContext<Node> valueContext;

    @Inject
    public CopyLinkAction(ValueContext<Node> valueContext, CopyLinkActionDefinition copyLinkActionDefinition, SimpleTranslator translator) {
        super(copyLinkActionDefinition);
        this.translator = translator;
        this.valueContext = valueContext;
    }

    @Override
    public void execute() {
        StringSelection requestedLink = new StringSelection(getCopyLink());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(requestedLink, null);
        Notification.show(translator.translate(getDefinition().getSuccessMessage()), Notification.Type.HUMANIZED_MESSAGE);
    }

    private String getCopyLink() {
        try {
            String requestedPage = String.format("%s~%s~.html", TEMPLATE_NAME, valueContext.getSingleOrThrow().getName());
            URI uri = new URI(MgnlContext.getWebContext().getAggregationState().getOriginalURL());
            return String.format("%s://%s:%s/%s", uri.getScheme(), uri.getHost(), uri.getPort(), requestedPage);
        } catch (URISyntaxException | RepositoryException e) {
            throw new IllegalStateException(e);
        }
    }
}