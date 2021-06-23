package com.example.votingapp.polleditablerule;

import com.example.votingapp.enums.PollStatus;
import com.example.votingapp.util.PollUtils;
import info.magnolia.ui.api.availability.AvailabilityDefinition;
import info.magnolia.ui.availability.rule.AbstractJcrRule;

import javax.jcr.Item;
import javax.jcr.Node;

public class JcrIsPollEditableRule extends AbstractJcrRule<JcrIsPollEditableRuleDefinition> {
    public JcrIsPollEditableRule(AvailabilityDefinition availabilityDefinition, JcrIsPollEditableRuleDefinition ruleDefinition) {
        super(availabilityDefinition, ruleDefinition);
    }

    @Override
    protected boolean isAvailableForJcrItem(Item item) {
        return PollUtils.pickPollStatus((Node) item).equals(PollStatus.TO_DO);
    }
}