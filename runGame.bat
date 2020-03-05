find . -name '*.java' >s
javac -cp src:$1 @s
java -cp src:$1 Tanks.Main