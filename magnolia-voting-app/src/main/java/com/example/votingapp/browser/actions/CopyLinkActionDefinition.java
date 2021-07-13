package com.example.votingapp.browser.actions;

import info.magnolia.ui.api.action.ActionType;
import info.magnolia.ui.api.action.ConfiguredActionDefinition;

@ActionType("copyLink")
public class CopyLinkActionDefinition extends ConfiguredActionDefinition {
    public CopyLinkActionDefinition() {
        this.setImplementationClass(CopyLinkAction.class);
    }
}