#!/bin/sh
LINK=https://docs.oracle.com/en/java/javase/11/docs/api/;
PACKAGE=ifmo.rain.kudaiberdieva.implementor
CLASSPATH=artifacts/info.kgeorgiy.java.advanced.implementor.jar:lib:bin
SOURCEPATH=src:module

rm -r javadoc/
mkdir javadoc
javac -cp artifacts/info.kgeorgiy.java.advanced.implementor.jar -d bin src/ifmo/rain/kudaiberdieva/implementor/Implementor.java
javadoc -private -author -version -sourcepath $SOURCEPATH -classpath $CLASSPATH -link $LINK -d javadoc $PACKAGE

