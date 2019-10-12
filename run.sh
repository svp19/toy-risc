ant
ant make-jar
java -Xmx1g -jar jars/simulator.jar src/configuration/config.xml stat.txt test_cases/$1.out
