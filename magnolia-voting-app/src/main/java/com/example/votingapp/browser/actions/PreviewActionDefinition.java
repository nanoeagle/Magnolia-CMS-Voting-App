package com.example.votingapp.browser.actions;

import info.magnolia.ui.api.action.ActionType;
import info.magnolia.ui.api.action.ConfiguredActionDefinition;

@ActionType("preview")
public class PreviewActionDefinition extends ConfiguredActionDefinition {
    public PreviewActionDefinition() {
        this.setImplementationClass(PreviewAction.class);
    }
}