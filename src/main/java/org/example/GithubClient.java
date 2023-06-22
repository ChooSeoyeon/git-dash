package org.example;

import org.kohsuke.github.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GithubClient {

    private static final String token = System.getenv("token");
    private static final Logger logger = (Logger) LogManager.getLogger(GithubClient.class);
    private GitHub github;
    private GHRepository repository;
    private List<GHIssue> issues;
    private List<GHIssueComment> comments;
    private Map<String, Double> userMap = new HashMap<>();

    public GithubClient() {

        try {
            github = new GitHubBuilder().withOAuthToken(token).build();
            repository = github.getRepository("whiteship/live-study");
            issues = repository.getIssues(GHIssueState.ALL);
            for(GHIssue issue: issues) {
                System.out.println(issue.getNumber());
                comments = issue.getComments();
                for(GHIssueComment comment: comments) {
                    final String login = comment.getUser().getLogin();
                    System.out.println(login);
                    userMap.put(login, userMap.getOrDefault(login, 0.0)+1);
                }
            }
            logger.info("Connection Success");
        } catch (Exception e) {
            logger.info("Connection Failed");
        }
    }

    public void run() {

        for(String user : userMap.keySet()) {
            final double percent = userMap.get(user) / 18*100;
            System.out.printf("%s : %.2f\n", user, percent);
        }
    }
}
