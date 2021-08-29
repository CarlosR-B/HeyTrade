# HEYTRADE CODING EXERCISE

[![Gradle build](https://github.com/CarlosR-B/HeyTrade/actions/workflows/gradle.yml/badge.svg?branch=feature%2Fpokedex_backend)](https://github.com/CarlosR-B/HeyTrade/actions/workflows/gradle.yml)

## Building

A simple `./gradlew build` should suffice for building the project. In order to run it as a Spring Boot application
you can run `./gradlew bootRun`.

## Using docker

The project can also run as a simple docker application. You can compile it from the backend root using
```
docker build . -t «tag»
```
where tag is whichever name you want to name the container.

To run it simply execute
```
docker run «tag» -p 8080:8080
```
so that the internally used 8080 port is mapped to your local 8080 port.

Enjoy!