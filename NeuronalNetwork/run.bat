@echo off
rem SET CLASSPATH=.;lib/httpunit.jar;lib/js.jar;lib/junit.jar;lib/nekohtml.jar;lib/servlet.jar;lib/Tidy.jar;lib/xercesImpl.jar;lib/xmlParserAPIs.jar;
SET PATH=%PATH%;C:\Progra~1\java\jdk1.5.0\bin
javac ro/sarsa/neuronal/*.java
java  ro.sarsa.neuronal.TestNetwork 