#!/bin/bash

javadoc -link https://docs.oracle.com/javase/8/docs/api/  -author -version -private -d ./ -sourcepath ../../src/main/java/ edu.rit.LightBikesClient edu.rit.LightBikesServer
