FROM bellsoft/liberica-openjdk-alpine:8 as build
COPY . .
RUN . mvnw install

FROM nginx:1-alpine
COPY --from=build ./public /usr/share/nginx/html