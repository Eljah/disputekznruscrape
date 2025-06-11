#!/bin/sh
set -e

# Install Maven 3.9.9
version=3.9.9
url="https://archive.apache.org/dist/maven/maven-3/${version}/binaries/apache-maven-${version}-bin.tar.gz"
wget -q "$url"
tarFile="apache-maven-${version}-bin.tar.gz"
sudo tar -xzf "$tarFile" -C /opt/
export PATH=/opt/apache-maven-${version}/bin:$PATH

echo "\nPATH=/opt/apache-maven-${version}/bin:\$PATH" >> ~/.profile

# Configure Maven proxy settings
mkdir -p ~/.m2
cat > ~/.m2/settings.xml <<'SET'
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <proxies>
    <proxy>
      <id>defaultProxy</id>
      <active>true</active>
      <protocol>http</protocol>
      <host>proxy</host>
      <port>8080</port>
    </proxy>
    <proxy>
      <id>defaultSecureProxy</id>
      <active>true</active>
      <protocol>https</protocol>
      <host>proxy</host>
      <port>8080</port>
    </proxy>
  </proxies>
</settings>
SET

# Preload all project dependencies
mvn dependency:go-offline -B -U
