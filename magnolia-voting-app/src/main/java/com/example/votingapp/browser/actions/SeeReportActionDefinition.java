package com.example.votingapp.browser.actions;

import info.magnolia.ui.api.action.ActionType;
import info.magnolia.ui.api.action.ConfiguredActionDefinition;

@ActionType("seeReport")
public class SeeReportActionDefinition extends ConfiguredActionDefinition {
    public SeeReportActionDefinition() {
        this.setImplementationClass(SeeReportAction.class);
    }
}