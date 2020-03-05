rm *.class
javac *.java
jar cvfe $2 Main *.class Resources
rm *.class
pause 