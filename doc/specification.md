# MAC_Project
MAC-Project

## Cahier des charges

### Contexte
Création d'un mini réseau social permettant à des utilisateurs d'associer à des documents des tags liés une idéologie, un parti politique, un courant de pensée. Par exemple, si un utilisateur estime que le contenu d'un document a rapport à un courant politique appelé "protectionisme", il peut le tagger comme tel. Le tag peut représenter une association ou anti-association; par exemple, si un document est taggé "protectionisme", on peut imaginer qu'il pourrait probablement anti-taggé "mondialisation".
L'utilisateur peut aussi liker ou disliker un document, et commenter un document.

### Fonctionnalités
L'application telegram sera utilisée comme interface interface pour le mini réseau social.
Les actions à réaliser par l'utilisateur sur le réseau social se feront par l'intermédiaire d'un bot.
Comme un document peut être identifié par une URL, un utilisateur peut envoyer ou forwarder une URL au bot, pour désigner un document. Dans le message, après l'url, certains mots-clés peuvent être ajoutés pour préciser l'action  effectuer. Par exemple, un messsage permettant de tagger un document ressemblera à ceci: ```/tag "whatever-url.com" "some tag"```.
Voici la liste des fonctionnalités prévues:
#### Fonctionalités basiques
- ```/add url``` : ajoute un document
- ```/documents [range]``` : liste les documents disponibles
- ```/document url``` : liste les informations associées au document spécifié (tags, commentaires etc...)
- ```/users [range]``` : liste les utilisateurs
- ```/user user``` : liste des informations à propos de l'utilisateur désigné (nous-même si pas de user)
- ```/tag url tag_name``` : permet de tagger un document
- ```/comment url comment``` : permet de commenter un document
- ```/like url [+ | -]``` : permet de liker (ou disliker) un document
- ```/liked``` : liste les documents likés par nous-même
- ```/likedBy user``` : liste les documents likés par l'user spécifié.
#### Fonctionnalités avancées
- ```/getDocsByTag tag1 tag2 ...``` : liste les documents associés aux tags spécifiés
- ```/getUsersByTag tag1 tag2 ...``` : liste les users ayant likés des documents taggés avec les tags spécifiés
- ```/getUserTags [user]``` : liste les tags associés à un utilisateur (nous-même si user pas spécifié)
- ```/getNUsersClusters n``` forme n groupes de user de tel façon à maximiser la "ressemblance" des users au sein de ce groupe.
### Modifications potentielles
Selon l'avancée du projet et/ou les objectifs demandés, on peut imaginer ajouter d'autres relations et fonctionnalités, ou en retirer/modifier.
