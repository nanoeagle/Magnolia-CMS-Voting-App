package com.example.votingapp.browser.actions;


import com.example.votingapp.util.VotingAppActionUtil;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.ui.ValueContext;
import info.magnolia.ui.api.action.AbstractAction;
import info.magnolia.ui.api.action.Action;

import javax.inject.Inject;
import javax.jcr.Node;

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
        VotingAppActionUtil.openNewTab(TEMPLATE_NAME, valueContext);
    }
}
