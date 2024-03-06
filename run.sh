#!/bin/bash

# Run the Java program with input arguments
cd ~/Dev/cs250hw2/app/build/classes/java/main
/usr/lib/jvm/java-11-openjdk-11.0.22.0.7-2.el8.x86_64/bin/java cs250.hw2.Memory "$@"
