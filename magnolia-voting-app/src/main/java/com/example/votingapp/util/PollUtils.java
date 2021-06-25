package com.example.votingapp.util;

import java.util.*;

import javax.jcr.*;

import com.example.votingapp.enums.*;

import org.slf4j.*;

import info.magnolia.jcr.*;

public class PollUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(PollUtils.class);

    public static Enum<PollStatus> pickPollStatus(Node source) throws RuntimeException {
        Calendar today = Calendar.getInstance();

        try {
            if (source.hasProperty("startDate") && source.hasProperty("expiryDate")) {
                Calendar startDate = source.getProperty("startDate").getValue().getDate();
                Calendar expiryDate = source.getProperty("expiryDate").getValue().getDate();
                
                int todayStDateCompVal = today.compareTo(startDate);
                int todayExDateCompVal = today.compareTo(expiryDate);

                if (todayStDateCompVal < 0) {
                    return PollStatus.TO_DO;
                }
                if (todayStDateCompVal >= 0 && todayExDateCompVal < 0) {
                    return PollStatus.ON_GOING;
                } 
                return PollStatus.DONE;
            }
            return PollStatus.DELETED;

        } catch (RepositoryException e) {
            LOGGER.error("RepositoryException found, cannot get the status of the poll", e);
            throw new RuntimeRepositoryException(e);
        }
    }
}