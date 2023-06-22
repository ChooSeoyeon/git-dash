package org.example;

import org.kohsuke.github.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GithubClient {

    private static final String token = System.getenv("token");  // GitHub 인증 토큰
    private static final Logger logger = LogManager.getLogger(GithubClient.class);  // 로그 기록 위한 Logger 객체
    private Map<String, Double> userMap = new ConcurrentHashMap<>();  // 사용자별로 댓글 개수를 저장하는 Map 객체

    public GithubClient() {
        try {
            GitHub github = new GitHubBuilder().withOAuthToken(token).build();  // GitHub 인증 토큰으로 GitHub 객체
            GHRepository repository = github.getRepository("whiteship/live-study");  // "whiteship/live-study" 리포지토리의 정보
            List<GHIssue> issues = repository.getIssues(GHIssueState.ALL);  // 모든 이슈 정보

            issues.parallelStream().forEach(issue -> { // issues를 병렬 스트림으로 변환해 병렬 처리
                try {
                    List<GHIssueComment> comments = issue.listComments().toList();  // 한 이슈에 달린 모든 댓글 (한 번에 가져옴)
                    for (GHIssueComment comment : comments) {
                        String login = comment.getUser().getLogin();  // 댓글 작성자의 로그인 ID를 가져옴
                        userMap.compute(login, (key, value) -> value == null ? 1.0 : value + 1.0);  // 사용자별로 댓글 개수를 증가시킴(증분 연산)
                    }
                } catch (IOException e) {
                    logger.debug("Failed to fetch comments for issue", e);  // 이슈의 댓글 가져오기 실패
                }
            });

            logger.debug("Connection Success");  // 깃허브 연결, 리포지토리 가져오기, 이슈 가져오기 성공
        } catch (IOException e) {
            logger.debug("Connection Failed", e);  // 깃허브 연결, 리포지토리 가져오기, 이슈 가져오기 실패
        }
    }

    public void run() {
        for (Map.Entry<String, Double> entry : userMap.entrySet()) {
            String user = entry.getKey();  // 사용자 ID
            double count = entry.getValue();  // 댓글 개수
            double percent = (count / 18) * 100;  // 댓글 비율 계산
            System.out.printf("%s : %.2f\n", user, percent);  // 사용자별 댓글 비율을 출력
        }
    }
}
