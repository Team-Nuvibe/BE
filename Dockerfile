FROM eclipse-temurin:21-jre-alpine

LABEL maintainer="nuvibe"
LABEL description="Nuvibe Spring Boot Application"

# 작업 디렉토리 설정
WORKDIR /app

# Alpine에 curl 설치 (healthcheck용)
RUN apk add --no-cache curl

ENV TZ=Asia/Seoul
ENV SPRING_PROFILES_ACTIVE=prod

# 포트 노출
EXPOSE 8080

# 빌드된 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
