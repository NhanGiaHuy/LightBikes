#!/bin/bash

echo "Checking for existing JARs..."
if [ -e LightBikesClient.jar ]
	then
	echo "There is a LightBikesClient JAR in this directory. Removing..."
	rm LightBikesClient.jar
else
	echo "Working directory is clean (LightBikesClient)."
fi
if [ -e LightBikesServer.jar ]
	then
	echo "There is a LightBikesServer JAR in this directory. Removing..."
	rm LightBikesServer.jar
else
	echo "Working directory is clean (LightBikesServer)."
fi
echo "Compiling classes..."
javac edu/rit/LightBikesClient/*.java
javac edu/rit/LightBikesServer/*.java
echo "Building JAR files..."
jar cfm LightBikesClient.jar MANIFEST-CLIENT.MF edu/rit/LightBikesClient/*.class
jar cfm LightBikesServer.jar MANIFEST-SERVER.MF edu/rit/LightBikesServer/*.class
echo "JAR files built."