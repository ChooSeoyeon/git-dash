# live-study 대시 보드 만들기
## 1. 요구사항
- live-study 레포지토리(https://github.com/whiteship/live-study) 의 깃헙 이슈 1번부터 18번까지 댓글을 순회하며 댓글을 남긴 사용자를 체크하기.
- 참여율을 계산하기. 총 18회에 중에 몇 %를 참여했는지 소숫점 두자리가지 보여주기.
- Github 자바 라이브러리를 사용하기.

## 2. 과정
### (1) 어려웠던 점
- 이슈마다 커멘트 개수가 200~300개씩 달려있어 가져오는데 실행시간이 너무 오래 걸렸음
### (2) 해결 & 성과 : 실행 시간 `2분 44초`에서 `0분 24초`로 감소시킴 (`6.8배`의 성능 차이)
- `issues` 리스트를 병렬 스트림으로 변환하여 `병렬 처리`하도록 수정
- `userMap`을 `ConcurrentHashMap`으로 변경하여 `동시성` 문제를 방지
- `userMap.put()` 대신 `userMap.compute()`를 사용하여 `증분 연산`을 수행
- `issue.getComments()`를 `issue.listComments().toList()`로 변경하여 `한 번에 여러 코멘트를 가져오게` 수정


## 3. 결과
![image](https://github.com/ChooSeoyeon/java-study/assets/83302344/18edce40-13d8-47e1-ad8c-bca342cf30ce)
![image](https://github.com/ChooSeoyeon/java-study/assets/83302344/d7f1a67b-e328-4b88-b723-fd304d66f27c)

