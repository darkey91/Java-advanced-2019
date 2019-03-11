#!/bin/sh

javac -cp artifacts/info.kgeorgiy.java.advanced.implementor.jar -d . src/ifmo/rain/kudaiberdieva/implementor/Implementor.java
jar cfm implementor.jar MANIFEST.MF ifmo/rain/kudaiberdieva/implementor/*
rm -r ifmo
java  -cp artifacts/*:implementor.jar -p "artifacts:lib:implementor.jar" -m info.kgeorgiy.java.advanced.implementor class ifmo.rain.kudaiberdieva.implementor.Implementor
