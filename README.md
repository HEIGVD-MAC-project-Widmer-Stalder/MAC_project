# MAC_Project
MAC-Project

## Cahier des charges

### Contexte
Cr�ation d'un mini r�seau social permettant � des utilisateurs d'associer � des documents des tags li�s une id�ologie, un parti politique, un courant de pens�e. Par exemple, si un utilisateur estime que le contenu d'un document a rapport � un courant politique appel� "protectionisme", il peut le tagger comme tel. Le tag peut repr�senter une association ou anti-association; par exemple, si un document est tagg� "protectionisme", on peut imaginer qu'il pourrait probablement anti-tagg� "mondialisation".
L'utilisateur peut aussi liker ou disliker un document, et commenter un document.

### Fonctionnalit�s
L'application telegram sera utilis�e comme interface interface pour le mini r�seau social.
Les actions � r�aliser par l'utilisateur sur le r�seau social se feront par l'interm�diaire d'un bot.
Comme un document peut �tre identifi� par une URL, un utilisateur peut envoyer ou forwarder une URL au bot, pour d�signer un document. Dans le message, apr�s l'url, certains mots-cl�s peuvent �tre ajout�s pour pr�ciser l'action  effectuer. Par exemple, un messsage permettant de tagger un document ressemblera � ceci: ```/tag "whatever-url.com" "some tag"```.
Voici la liste des fonctionnalit�s pr�vues:
#### Fonctionalit�s basiques
- ```/add url``` : ajoute un document
- ```/documents [range]``` : liste les documents disponibles
- ```/document url``` : liste les informations associ�es au document sp�cifi� (tags, commentaires etc...)
- ```/users [range]``` : liste les utilisateurs
- ```/user user``` : liste des informations � propos de l'utilisateur d�sign� (nous-m�me si pas de user)
- ```/tag url tag_name``` : permet de tagger un document
- ```/comment url comment``` : permet de commenter un document
- ```/like url [+ | -]``` : permet de liker (ou disliker) un document
- ```/liked``` : liste les documents lik�s par nous-m�me
- ```/likedBy user``` : liste les documents lik�s par l'user sp�cifi�.
#### Fonctionnalit�s avanc�es
- ```/getDocsByTag tag1 tag2 ...``` : liste les documents associ�s aux tags sp�cifi�s
- ```/getUsersByTag tag1 tag2 ...``` : liste les users ayant lik�s des documents tagg�s avec les tags sp�cifi�s
- ```/getNUsersClusters n``` forme n groupes de user de tel fa�on � maximiser la "ressemblance" des users au sein de ce groupe.
### Modifications potentielles
Selon l'avanc�e du projet et/ou les objectifs demand�s, on peut imaginer ajouter d'autres relations et fonctionnalit�s, ou en retirer/modifier.
