FROM openjdk:8-jre-alpine
WORKDIR /app
COPY build/libs/azure-openai-proxy-0.0.1.jar /app/azure-openai-proxy.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/azure-openai-proxy.jar"]