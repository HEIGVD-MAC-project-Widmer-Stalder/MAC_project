git checkout master
git pull origin master
mvn clean install
cd target
pkill -f -9 telegram-bot-experiment-1.0-SNAPSHOT-jar-with-dependencies.jar
java -jar telegram-bot-experiment-1.0-SNAPSHOT-jar-with-dependencies.jar &

