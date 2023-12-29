# Back-end
혹시 몰라서 적어놔욤!
### 🏷️ 작업중인 브랜치가 master인 경우
      > git branch -M main을 실행해 branch의 이름을 master에서 main으로 변경  
      
### 🏷️ git push를 했는데  ! [rejected]  main -> main (fetch first) 에러 발생한 경우
      >  git pull --rebase origin main으로 로컬과 원격 저장소를 동기화 한 후에 push 진행

### 🏷️ intellij 폴더 구조가 이상할 때 (. 형식으로 되어 있을 때)
      > https://lowell-dev.tistory.com/13 참고, 중간 패키지 압축 해제 하기

- `커밋 컨벤션`
    - Feat: 새로운 기능 추가
    - Fix: 버그 수정
    - Docs: 문서 수정
    - Style: 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
    - Refactor: 코드 리팩토링
    - Test: 테스트 코드, 리팩토링 테스트 코드 추가
    - Chore: 빌드 업무 수정, 패키지 매니저 수정
  
