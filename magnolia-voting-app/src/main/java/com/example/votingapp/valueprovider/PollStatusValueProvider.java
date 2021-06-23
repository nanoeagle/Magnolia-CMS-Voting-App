package com.example.votingapp.valueprovider;

import javax.jcr.*;

import com.example.votingapp.enums.*;
import com.example.votingapp.util.*;

import com.vaadin.data.*;

/**
 * This class provides value for the poll status column. 
 */
public class PollStatusValueProvider implements ValueProvider<Node, Enum<PollStatus>> {
    
    @Override
    public Enum<PollStatus> apply(Node source) {
        return PollUtils.pickPollStatus(source);
    }
}