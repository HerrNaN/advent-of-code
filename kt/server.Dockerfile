FROM gradle:alpine as BASE

RUN pwd
RUN git clone https://github.com/HerrNaN/advent-of-code.git /app
WORKDIR /app/21/kt
RUN gradle build -x test

FROM openjdk:17-alpine
COPY --from=BASE /app/21/kt/build/libs/kt-1.0.0.jar /kt.jar
ENV day=01
COPY input.txt .
CMD java -jar /kt.jar < input.txt

# FROM docker

# RUN apk add git


# WORKDIR /app/21/kt
# RUN ls -la
# RUN docker build -q . -t aoc

# CMD docker run --rm aoc
