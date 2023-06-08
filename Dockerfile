FROM gradle:7.6.1-jdk17

ARG GITHUB_USERNAME
ARG GITHUB_TOKEN

COPY . /home/gradle/src
WORKDIR /home/gradle/src

# Set up GitHub Packages authentication
RUN echo "gpr.user=${GITHUB_USERNAME}" >> ~/.gradle/gradle.properties && \
    echo "gpr.key=${GITHUB_TOKEN}" >> ~/.gradle/gradle.properties

RUN gradle build

EXPOSE 8080

ENTRYPOINT ["java","-jar","/home/gradle/src/build/libs/snippet-runner-0.0.1-SNAPSHOT.jar"]