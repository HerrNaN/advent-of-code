FROM gradle:alpine as BASE

WORKDIR /app
COPY . .

RUN gradle build -x test

FROM openjdk:17-alpine
COPY --from=BASE /app/build/libs/kt-1.0.0.jar /kt.jar

ARG day
ENV day=$day

COPY input.txt .

CMD java -jar /kt.jar < input.txt