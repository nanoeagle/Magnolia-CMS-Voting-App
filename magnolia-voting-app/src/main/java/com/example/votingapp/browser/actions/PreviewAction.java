package com.example.votingapp.browser.actions;

import com.example.votingapp.util.VotingAppActionUtil;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.ui.ValueContext;
import info.magnolia.ui.api.action.AbstractAction;
import info.magnolia.ui.api.action.Action;

import javax.inject.Inject;
import javax.jcr.Node;

public class PreviewAction extends AbstractAction<PreviewActionDefinition> implements Action {
    private static final String TEMPLATE_NAME = "Poll-page";
    private final SimpleTranslator translator;
    private final ValueContext<Node> valueContext;

    @Inject
    public PreviewAction(ValueContext<Node> valueContext, PreviewActionDefinition previewActionDefinition, SimpleTranslator translator) {
        super(previewActionDefinition);
        this.translator = translator;
        this.valueContext = valueContext;
    }

    @Override
    public void execute() {
        VotingAppActionUtil.openNewTab(TEMPLATE_NAME, valueContext);
    }
}
