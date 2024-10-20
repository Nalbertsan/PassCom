# Use uma imagem base do OpenJDK
FROM openjdk:17-jdk-alpine

# Cria um diretório para a aplicação
VOLUME /tmp

# Define o argumento para o jar da aplicação
ARG JAR_FILE=target/*.jar

# Copia o arquivo jar gerado para dentro do container
COPY ${JAR_FILE} app.jar

# Expor a porta que a aplicação usará
EXPOSE 8080

# Comando para rodar a aplicação
ENTRYPOINT ["java","-jar", "/app.jar"]