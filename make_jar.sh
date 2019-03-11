#!/bin/sh
javac -cp artifacts/info.kgeorgiy.java.advanced.implementor.jar -d bin src/ifmo/rain/kudaiberdieva/implementor/Implementor.java
jar cfm implementor.jat MANIFEST.MF bin/ifmo/rain/kudaiberdieva/implementor/*
java -jar implementor.jar


