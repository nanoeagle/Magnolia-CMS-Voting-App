package com.example.votingapp.templatingfunctions;

import info.magnolia.cms.security.JCRSessionOp;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollReportTemplatingFunctions {

    private static final Logger log = LoggerFactory.getLogger(PollReportTemplatingFunctions.class);

    private static String POLLS = "polls";
    private static String SUM_QUESTIONS_NUMBER = "sumQuestionsNumber";
    private static String SUM_ANSWERS_NUMBER = "sumAnswersNumber";
    private static String SUM_VOTES_NUMBER = "sumVoteNumber";

    Map<String, Integer> contentNodeMap = new HashMap<>();
    Map<String, Integer> answerNodeMap = new HashMap<>();
    Map<Integer, Map<String, Integer>> root;

    List<Node> questionList;
    List<Node> answerList;
    List<Node> voterList;

    public String getAllChildren(String nodePath) {
        questionList = new ArrayList<>();
        answerList = new ArrayList<>();
        voterList = new ArrayList<>();
        try {
            MgnlContext.doInSystemContext(new JCRSessionOp<Void>(POLLS) {
                @Override
                public Void exec(Session session) throws RepositoryException {
                    Node parentNode = session.getNode("/" + nodePath);

                    List<Node> childrenNodes = NodeUtil.asList(NodeUtil.collectAllChildren(parentNode));
//                    List<Node> childrenNodes = NodeUtil.asList((Iterable<Node>) parentNode.getNodes());
//                    List<Node> childrenNodes = NodeUtil.asList(NodeUtil.asIterable(parentNode.getNodes()));
//                    List<Node> answerList = NodeUtil.asList(NodeUtil.asIterable(parentNode.getNodes()));

                    for (int i = 0; i < childrenNodes.size(); i++) {
                        Node questionNode = childrenNodes.get(i);

                        int level = getLevel(questionNode);
                        if (level == 3) {
                            questionList.add(questionNode);
                        }
                        if (level == 5) {
                            answerList.add(questionNode);
                        }
                        if (level == 7) {
                            voterList.add(questionNode);
                        }
                    }
                    contentNodeMap.put(SUM_QUESTIONS_NUMBER, questionList.size());
                    contentNodeMap.put(SUM_ANSWERS_NUMBER, answerList.size());
                    contentNodeMap.put(SUM_VOTES_NUMBER, voterList.size() / questionList.size());

                    countQuestionAndAnswerNumberByIndex();
                    return null;
                }
            });
        } catch (RepositoryException e) {
            log.error("Could not get node {}.", e.getMessage(), e);
        }
        return null;
    }

//    private void collectAllChildren(Node parentNode){
//        List<Node> nodes = new ArrayList();
//        return collectAllChildren(nodes, node, MAGNOLIA_FILTER);
//    }
//
//    public static AbstractPredicate<Node> MAGNOLIA_FILTER = new AbstractPredicate<Node>() {
//        public boolean evaluateTyped(Node node) {
//            try {
//                String nodeTypeName = node.getPrimaryNodeType().getName();
//                return nodeTypeName.startsWith("mgnl:");
//            } catch (RepositoryException var3) {
//                NodeUtil.log.error("Unable to read nodeType for node {}", NodeUtil.getNodePathIfPossible(node));
//                return false;
//            }
//        }
//    };


    private void countQuestionAndAnswerNumberByIndex() throws RepositoryException {
        root = new HashMap<>();
        answerNodeMap = new HashMap<>();
        for (int k = 0; k < questionList.size(); k++) {
            int countOtherVote = 0;
            for (int j = 0; j < answerList.size(); j++) {
                Node answerNode = answerList.get(j);
                List<Node> listAnswerByIndex = new ArrayList<>();
                if (!isOtherAnswer(answerNode)) {
                    addToList(listAnswerByIndex, j, k);
                    contentNodeMap.put(SUM_ANSWERS_NUMBER + k + j, listAnswerByIndex.size());
                    answerNodeMap.put(getAnswerContent(answerList.get(j)), listAnswerByIndex.size());
                } else if(isOtherAnswer(answerNode)){
                    addToList(listAnswerByIndex, j, k);
                    countOtherVote += listAnswerByIndex.size();
                }
            }
            contentNodeMap.put(SUM_ANSWERS_NUMBER + k + "Other", countOtherVote);
            answerNodeMap.put("Other", countOtherVote);
            root.put(k, answerNodeMap);
        }
    }

    private void addToList(List<Node> list, int j, int k) throws RepositoryException {
        for (int i = 0; i < voterList.size(); i++) {
            Node node = voterList.get(i);
            String[] str = StringUtils.split(node.getPath(), "/");
            if (str[4] != null && (str[4]).equals("answers" + j) && str[2] != null && (str[2]).equals("questions" + k)) {
                list.add(node);
            }
        }
    }

    public List<ArrayList<String>> getVoteDetail() throws RepositoryException {
        List<ArrayList<String>> voteDetailList = new ArrayList<ArrayList<String>>();
        ArrayList<String> aVoteDetail = new ArrayList<>();
        for (int i = 0; i < voterList.size(); i++) {
            String name = voterList.get(i).getProperty("fullName").getValue().getString();
            String email = voterList.get(i).getProperty("email").getValue().getString();
            String answerValue = "";
            String otherAnswerValue = "";
            if (isOtherAnswer(voterList.get(i).getParent().getParent())) {
                answerValue = "";
                otherAnswerValue = getAnswerContent(voterList.get(i).getParent().getParent());
            } else {
                otherAnswerValue = "";
                answerValue = getAnswerContent(voterList.get(i).getParent().getParent());
            }
            aVoteDetail.add(name);
            aVoteDetail.add(email);
            aVoteDetail.add(answerValue);
            aVoteDetail.add(otherAnswerValue);
            voteDetailList.add(aVoteDetail);
            aVoteDetail = new ArrayList<>();
        }
        return voteDetailList;
    }

    public boolean isOtherAnswer(Node node) throws RepositoryException {
        if (node.hasProperty("category")) {
            return true;
        }
        return false;
    }

    private int getLevel(Node node) throws RepositoryException {
        String path = node.getPath();
        String[] str = StringUtils.split(path, "/");
        return str != null ? str.length : 0;
    }

    public int sumVotesNumber() {
        if (contentNodeMap.containsKey(SUM_VOTES_NUMBER)) {
            return contentNodeMap.get(SUM_VOTES_NUMBER);
        }
        return 0;
    }

    public int sumQuestionNumber() {
        if (contentNodeMap.containsKey(SUM_QUESTIONS_NUMBER)) {
            return contentNodeMap.get(SUM_QUESTIONS_NUMBER);
        }
        return 0;
    }

    public int sumAnswersNumber() {
        if (contentNodeMap.containsKey(SUM_ANSWERS_NUMBER)) {
            return contentNodeMap.get(SUM_ANSWERS_NUMBER);
        }
        return 0;
    }

    public String getAnswerContent(Node node) throws RepositoryException {
        return node.getProperty("answerVal").getValue().getString();
    }

    public Map<Integer, Map<String, Integer>> getNumberOfVoteForEachAnswer() {
        return root;
    }
}