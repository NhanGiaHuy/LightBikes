#!/bin/bash

echo "Cleaning out any existing JARs..."
rm *.jar
echo "Compiling classes..."
javac edu/rit/LightBikesClient/*.java
javac edu/rit/LightBikesServer/*.java
echo "Building JAR files..."
jar cfm LightBikesClient.jar MANIFEST-CLIENT.MF edu/rit/LightBikesClient/*.class
jar cfm LightBikesServer.jar MANIFEST-SERVER.MF edu/rit/LightBikesServer/*.class
echo "JAR files built."