FROM eclipse-temurin:17-jdk
COPY build/libs/category-service.jar .
ENTRYPOINT ["java", "-jar", "category-service.jar"]