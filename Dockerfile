FROM quay.io/badfic/liberica-openjdk-alpine:17
COPY . .
RUN . mvnw install

FROM quay.io/badfic/liberica-openjdk-alpine:17-jre
COPY --from=0 target/chef-santiago-blog-1.0-SNAPSHOT.jar ./chef-santiago-blog.jar
EXPOSE 8080
CMD ["-Xms64m", "-Xmx64m", "-verbose:gc", "-jar", "chef-santiago-blog.jar"]
ENTRYPOINT ["java"]