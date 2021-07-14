package com.example.votingapp.browser.actions;

import com.example.votingapp.util.VotingAppActionUtil;
import com.vaadin.ui.Notification;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.ui.ValueContext;
import info.magnolia.ui.api.action.AbstractAction;
import info.magnolia.ui.api.action.Action;

import javax.inject.Inject;
import javax.jcr.Node;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

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
        StringSelection requestedLink = new StringSelection(VotingAppActionUtil.getVotingAppLink(TEMPLATE_NAME, valueContext));
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(requestedLink, null);
        Notification.show(translator.translate(getDefinition().getSuccessMessage()), Notification.Type.HUMANIZED_MESSAGE);
    }
}