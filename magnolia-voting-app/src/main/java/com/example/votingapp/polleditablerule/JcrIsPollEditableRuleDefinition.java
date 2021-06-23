package com.example.votingapp.polleditablerule;

import info.magnolia.ui.api.availability.AvailabilityRuleType;
import info.magnolia.ui.api.availability.ConfiguredAvailabilityRuleDefinition;

@AvailabilityRuleType("jcrIsPollEditableRule")
public class JcrIsPollEditableRuleDefinition extends ConfiguredAvailabilityRuleDefinition {
    public JcrIsPollEditableRuleDefinition() {
        setImplementationClass(JcrIsPollEditableRule.class);
    }
}