package com.example.votingapp.browser.actions;


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
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class SeeReportAction extends AbstractAction<SeeReportActionDefinition> implements Action {
    private static final String TEMPLATE_NAME = "Voting-report";
    private final SimpleTranslator translator;
    private final ValueContext<Node> valueContext;

    @Inject
    public SeeReportAction(ValueContext<Node> valueContext, SeeReportActionDefinition seeReportActionDefinition, SimpleTranslator translator) {
        super(seeReportActionDefinition);
        this.translator = translator;
        this.valueContext = valueContext;
    }

    @Override
    public void execute() {
        StringSelection requestedLink = new StringSelection(getSeeReportLink());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(requestedLink, null);
        try {
            Desktop.getDesktop().browse(new URI(getSeeReportLink()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private String getSeeReportLink() {
        try {
            String requestedPage = String.format("%s~%s~.html", TEMPLATE_NAME, valueContext.getSingleOrThrow().getName());
            URI uri = new URI(MgnlContext.getWebContext().getAggregationState().getOriginalURL());
            return String.format("%s://%s:%s/%s", uri.getScheme(), uri.getHost(), uri.getPort(), requestedPage);
        } catch (URISyntaxException | RepositoryException e) {
            throw new IllegalStateException(e);
        }
    }
}
