#!/bin/sh
JAR=target/disputescraper-1.0-SNAPSHOT-jar-with-dependencies.jar
if [ ! -f "$JAR" ]; then
  echo "Jar not found, building project..." >&2
  mvn -q -DskipTests package
fi
if command -v xvfb-run >/dev/null && [ -z "$DISPLAY" ]; then
  xvfb-run -a java -Dbrowser=chrome -cp "$JAR" com.example.App "$@"
else
  java -Dbrowser=chrome -cp "$JAR" com.example.App "$@"
fi
