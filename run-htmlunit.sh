#!/bin/sh
JAR=target/disputescraper-1.0-SNAPSHOT-jar-with-dependencies.jar
if [ ! -f "$JAR" ]; then
  echo "Jar not found, building project..." >&2
  mvn -q -DskipTests package
fi
java -cp "$JAR" com.example.App "$@"
