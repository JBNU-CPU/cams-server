# =============================================
# 1. 빌드(Build) 단계
# =============================================
FROM eclipse-temurin:17-jdk-jammy AS builder

# 작업 디렉터리
WORKDIR /app

# 소스코드 복사
COPY . .

# Gradle 실행 권한 (리눅스 컨테이너에선 OK)
RUN chmod +x ./gradlew

# 애플리케이션 빌드
# Spring Boot면 보통 bootJar가 최종 실행 JAR을 만듭니다.
# 일반 Gradle Java 프로젝트라면 'build' 그대로 사용해도 됩니다.
RUN ./gradlew build --no-daemon -x test

# =============================================
# 2. 실행(Runtime) 단계
# =============================================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

ENV JAVA_OPTS="-Duser.timezone=Asia/Seoul"

# 빌드 산출물 복사 (필요에 맞게 패턴 조정)
# 예) Spring Boot: build/libs/*-SNAPSHOT.jar 또는 *-plain.jar 제외 필요
# 가장 단순하게 하나만 존재한다고 가정하고 *.jar -> app.jar 로 복사
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# 컨테이너 시작 시 실행할 명령
# WORKDIR가 /app 이므로 상대경로 'app.jar'로 지정
ENTRYPOINT ["java", "-jar", "app.jar"]
