FROM quay.io/badfic/liberica-openjdk-alpine:17 as build
COPY . .
RUN . mvnw install

FROM quay.io/badfic/nginx:1-alpine
COPY --from=build ./public /usr/share/nginx/html