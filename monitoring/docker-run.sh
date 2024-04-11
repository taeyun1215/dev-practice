# Gradle을 사용해 Spring Boot 애플리케이션을 빌드합니다.
./gradlew bootJar

# Docker 이미지를 빌드합니다.
docker build -t springboot .

# 이전에 실행된 동일 이름의 컨테이너가 있으면 제거합니다.
docker rm -f springboot || true

# Docker 컨테이너를 백그라운드에서 실행합니다.
docker run -d -p 8080:8080 --name springboot springboot