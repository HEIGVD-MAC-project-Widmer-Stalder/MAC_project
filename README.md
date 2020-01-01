# MAC_Project
MAC-Project

## Aim
Should be described in pdf file: doc/MAC_2019_20_Projet.pdf

## Initial project description
[description](doc/project_description.md)

## Specification
[Specification](doc/Specification.md)

## Data model and analytic queries
[data_model_and_analytic_queries](doc/data_model_and_analytic_queries.md)

## How to run
### 1 Have a running neo4j database
The telegram bot needs to have access to a running neo4j database. You can install it normally or use the official neo4j docker image to start a container. For convenience, in the folder neo4j, we added a script which should create and start a docker container running a neo4j database. (Use this script only the first time since it will start a new container each time it is called. After that, you should then instead use command ```docker start <containerid>``` to restart it, and ```docker stop <containerid>``` to stop it).
### 2 Configure connection with neo4j
In folder __telegram_bot/src/main/resources__ you'll find a file called __neo4j.properties__. Edit it if needed in order to specify the uri, username and password that will be used by the bot to connect to the database.
(Alternatively, you could also edit this file directly in the jar archive if you want)
### 3 Configure the telegram bot
In folder __telegram_bot/src/main/resources__ you'll find a file called __telegram_bot.properties__. Edit it if needed for the bot to use the proper __bot_username__ and __bot_secret_token__. Refer to the official telegram documentation if you want to create a new bot, and know about the bot username and token.
(Alternatively, you could also edit the property file directly in the jar archive if you want)
In case you created a bot, it is convenient to let the user see a list of the commands when talking to the bot. This has to be done by you; see the telegram official reference.
### 4 Start the bot
Having maven installed, execute command ```mvn clean install``` in folder __telegram_bot__. This will generate a folder named __target__. In this folder, you'll find two jar archives. Execute the one which name end with "with-dependencies.jar".

## How to add new commands to the bot
In folder __telegram_bot/src/main/java/Actions__, add (and implement) a new class implementing the Action interface from the same folder.
Then, add appropriate code in ActionsResolver.java in order to map a telegram command to the Action.
It is a good idea to add the command to the list of commands the user will see when talking to the bot. To modify this list, refer to the official telegram documentation (it can normally be done by talking to the bot father).
