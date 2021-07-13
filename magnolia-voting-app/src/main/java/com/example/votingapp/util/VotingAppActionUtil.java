package com.example.votingapp.util;

import info.magnolia.context.MgnlContext;
import info.magnolia.ui.ValueContext;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class VotingAppActionUtil {
    public static String getVotingAppLink(String TEMPLATE_NAME, ValueContext<Node> valueContext) {
        try {
            String requestedPage = String.format("%s~%s~.html", TEMPLATE_NAME, valueContext.getSingleOrThrow().getName());
            URI uri = new URI(MgnlContext.getWebContext().getAggregationState().getOriginalURL());
            return String.format("%s://%s:%s/%s", uri.getScheme(), uri.getHost(), uri.getPort(), requestedPage);
        } catch (URISyntaxException | RepositoryException e) {
            throw new IllegalStateException(e);
        }
    }

    public static void openNewTab(String TEMPLATE_NAME, ValueContext<Node> valueContext) {
        StringSelection requestedLink = new StringSelection(getVotingAppLink(TEMPLATE_NAME, valueContext));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(requestedLink, null);
        try {
            Desktop.getDesktop().browse(new URI((getVotingAppLink(TEMPLATE_NAME, valueContext))));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
