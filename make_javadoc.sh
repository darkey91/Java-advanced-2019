#!/bin/sh
 javac -cp artifacts/info.kgeorgiy.java.advanced.implementor.jar -d bin src/ifmo/rain/kudaiberdieva/implementor/Implementor.java

javadoc -private -author -sourcepath src -classpath artifacts/info.kgeorgiy.java.advanced.implementor.jar -d javadoc ifmo.rain.kudaiberdieva.implementor
