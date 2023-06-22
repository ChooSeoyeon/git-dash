package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

public class GHConnect {

    private static final String token = System.getenv("token");
    private static final Logger logger = (Logger) LogManager.getLogger(GHConnect.class);

    private GitHub github;

    public GHConnect() {

        try {
            this.github = new GitHubBuilder().withOAuthToken(token).build();
            logger.info("Connection Success");
        } catch (Exception e) {
            logger.info("Connection Failed");
        }
    }


}
