package com.example.votingapp.controllers;

import java.io.*;

import javax.jcr.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.example.votingapp.enums.PollStatus;
import com.example.votingapp.util.PollUtils;

import org.slf4j.*;

import info.magnolia.jcr.util.*;

public class PollSubmissionHandler extends HttpServlet {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(PollSubmissionHandler.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Node thePoll = NodeUtil.getNodeByIdentifier("polls", request.getParameter("pollId"));

            if (thePoll == null) {
                throw new RepositoryException("Cannot get the poll node.");
            }

            // If the poll is not in progress, return.
            if (PollUtils.pickPollStatus(thePoll) != PollStatus.ON_GOING) {
                return;
            }

            // The poll is still in progress.
            String voterName = request.getParameter("voterFullName");
            String voterEmail = request.getParameter("voterEmail");
            
            if (voterName == null || voterEmail == null 
                || request.getParameter("questionIds") == null) return;

            for (String questionId : request.getParameterValues("questionIds")) {

                String voterChoiceValue = request.getParameter("voterChoice_" + questionId);
                
                if (voterChoiceValue == null) return;
                
                String[] voterChoiceValueTokens = voterChoiceValue.split(",");

                // If the voter's choice is a predifined value,
                if (voterChoiceValueTokens[0].equals("predifined")) {
                    // then the second token is an answer node id.
                    String answerId = voterChoiceValueTokens[1];
                    Node answer = NodeUtil.getNodeByIdentifier("polls", answerId);
                
                    if (answer == null) {
                        throw new RepositoryException("Cannot get the answer node.");
                    }

                    addVoter(answer, voterName, voterEmail);

                // If the voter provides a custom answer,
                } else {
                    // then the second token is an 'answers' parent node id.
                    String answerParentNodeId = voterChoiceValueTokens[1];
                    Node answerParentNode = NodeUtil.getNodeByIdentifier("polls", answerParentNodeId);

                    if (answerParentNode == null) {
                        throw new RepositoryException("Cannot get the answer's parent node.");
                    } 

                    String voterOwnAnswer = request.getParameter("voterOwnAnswer_" + questionId);

                    if (voterOwnAnswer == null) return;

                    // Add the actual answer node.
                    Node answer = answerParentNode
                        .addNode("answers" + generateNodeIndex(answerParentNode), "pollAnswer");
                
                    // Add the answer node's properties.
                    answer.setProperty("answerVal", voterOwnAnswer);
                    answer.setProperty("category", "other");

                    addVoter(answer, voterName, voterEmail);
                }
            } 
            request.setAttribute("message", "successful");

        } catch (RepositoryException e) {
            request.setAttribute("message", "failed");
            LOGGER.error("RepositoryException found, cannot save the poll result", e);
        
        } finally {
            String pollSelector = request.getParameter("pollSelector");

            if (pollSelector == null) pollSelector = "";

            request.getRequestDispatcher("/voting/notification-page~" + pollSelector + "~.html").forward(request, response);
        }
    }

    private Node addVoter(Node answerNode, String voterName, String voterEmail) throws RepositoryException {
        // The parent node of actual voter nodes.
        Node voterParentNode = answerNode.hasNode("voters") ? 
            answerNode.getNode("voters") : answerNode.addNode("voters", "mgnl:contentNode");
        // The actual voter node.
        Node voter = voterParentNode.addNode("voters" + generateNodeIndex(voterParentNode), "pollVoter");
        
        voter.setProperty("fullName", voterName);
        // If the voter's email string is not empty.
        if ( !voterEmail.equals("") ) {
            voter.setProperty("email", voterEmail);
        }

        // Add the actual voter node.
        answerNode.getSession().save();
        return voter;
    }

    private int generateNodeIndex(Node parentNode) throws RepositoryException {
        if (parentNode.hasNodes()) {
            // The next index.
            return (int) parentNode.getNodes().getSize();
        } else {
            // Zero is the first index.
            return 0;
        } 
    }
}