# BE

[NUVIBE]

## 👥 Contributors

| <img src="https://github.com/jaemin0413.png" width="100"> | <img src="https://github.com/yunseo-leeo.png" width="100"> | <img src="https://github.com/Chhun-Lee.png" width="100"> | <img src="https://github.com/wlsldm.png" width="100"> | <img src="https://github.com/Neo1228.png" width="100"> |
| :---: | :---: | :---: | :---: | :---: |
| [**한재민**](https://github.com/jaemin_0413) | [**이윤서**](https://github.com/yunseo-leeo) | [**이창훈**](https://github.com/Chhun-Lee) | [**조수진**](https://github.com/wlsldm) | [**최민수**](https://github.com/Neo1228) |

<br> 


## Tech Stack
| **구분** | **기술** |
| --- | --- |
| **Language** | Java 21 |
| **Framework** | Spring Boot 3.5.9 |
| **Database** | MySQL (rds) |
| **Infra** | AWS (EC2 t3, RDS, S3, SQS), Docker |
| **CI / CD** | GitHub Actions, Dockerhub |
| **Monitoring** | Grafana, Prometeus, Loki |


<br> 

## System Architecture

<img width="1201" height="656" alt="image" src="https://github.com/user-attachments/assets/e18bed44-2986-44a8-a46c-ab3447ab6635" />

- AWS t3 인스턴스를 기반으로 가용성을 고려한 인프라 구축
- 운영 환경 리소스 및 로그 추적을 위해 SaaS를 통한 모니터링 구축

<br> 

### Branch
`컨벤션명/#이슈번호`

### Commit Convention
| 커밋 타입 | 설명 | **커밋 메시지 예시** |
| --- | --- | --- |
| ✨ **Feat** | 새로운 기능 추가 | `[FEAT] #이슈번호: 기능 추가` |
| 🐛 **Fix** | 버그 수정 | `[FIX] #이슈번호: 오류 수정` |
| 📄 **Docs** | 문서 수정 | `[DOCS] #이슈번호: README 파일 수정` |
| ♻️ **Refactor** | 코드 리팩토링 | `[REFACTOR] #이슈번호: 함수 구조 개선` |
| 📦 **Chore** | 빌드 업무 수정, 패키지 매니저 수정 등 production code와 무관한 변경 | `[CHORE] #이슈번호: .gitignore 파일 수정` |
| 💬 **Comment** | 주석 추가 및 변경 | `[COMMENT] #이슈번호: 함수 설명 주석 추가` |
| 🔥 **Remove** | 파일 또는 폴더 삭제 | `[REMOVE] #이슈번호: 불필요한 파일 삭제` |
| 🚚 **Rename** | 파일 또는 폴더명 수정 | `[RENAME] #이슈번호: 폴더명 변경` |


### Issue Template
```
## 어떤 기능인가요?

> 추가하려는 기능에 대해 간결하게 설명해주세요

## 작업 상세 내용

- [ ] TODO
- [ ] TODO
- [ ] TODO

## 참고할만한 자료(선택)
```


### Pull Request Template
```
## 🎋 이슈 및 작업중인 브랜치

-

## 🔑 주요 내용

-


## Check List

- [ ] **Reviewers** 등록을 하였나요?
- [ ] **Assignees** 등록을 하였나요?
- [ ] **라벨(Label)** 등록을 하였나요?
- [ ] PR 머지하기 전 반드시 **CI가 정상적으로 작동하는지 확인**해주세요!
```

