# Zikobot


L'application doit permettre la synchro de ses musiques locales et faire l'agrégation de ces données avec celles des différentems comptes de l'utilisateur (last fm, spotify, soundcloud)

## 1er lancement 

1. Synchro des données locales 
2. Proposition de connexion à un compte musical 
3. Proposition des synchros playlists des comptes musicaux. 
4. Pendant la synchro de chaque chanson, récupération de l'artiste et ajout à la liste des artistes en bdd
5. Pendant la synchro, récupération des artistes préférées de spotify et proposition d'ajout 
6. Si compte spotify rechercher des infos sur les artistes locaux via leur nom, afficher message pour faire la liaison avec l'artiste. Si le bon ajouter l'id spotify
7. Page home
8. Page artistes
9. Page playlists
10. Page recherche
11. Page mon compte (drawer)

## Home

1. Si pas de compte, proposer la connexion a un des comptes 
2. Si pas de compte, chercher les artistes similaires via last fm
3. Si pas de compte, afficher les news concernant les artistes favoris sinon un random
4. Proposition par rapport aux genres favoris 

## Pages artistes

Redirection possible vers artiste

1. Affiche la liste des artistes présents en local sur le téléphone ( mis en bdd après synchro )
2. Afficher la liste des artistes sélectionnés depuis les comptes musicaux (mis en bdd)
3. Possibilité d'en ajouter des nouveaux via une recherche (last fm / spotify / SC )
4. Possibilité de mettre en favori et de filtrer par favori / genre

## Page artiste 

> Affiche les infos de l'artiste ses albums et ses chansons et une recherche au sein de cette artiste

1. Si local id récupération des local tracks depuis son local id, récupération des local albums depuis son local id
2. Si uniquement local id récupération des informations depuis last fm. Proposition d'artiste similaires 
3. Si spotify id récupération des albums top tracks et des artistes similaires 

## Page playlists 

> Redirige vers page playlist, raccourci vers alarme

1. Afficher la liste des playlists en bdd
2. Si alarme activée logo de l'alarme visible 

## Page playlist

> Redirige vers alarme, artiste

1. Récupération des tracks liés à l'id de la playlist en bdd
2. Synchronisation possible selon l'id de la playlist spotify/ soundcloud pour mettre a jour . Si chanson déjà présente ne pas la sauvegarder 
3. Gestion de l'alarme lié à l'id de la playlist 

## Page recherche 

> La page recherche doit pouvoir être réutilisée pour la page ajout d'artiste, de chansons

1. La recherche peut proposer des artistes depuis spotify last fm soundcloud ou en local
2. Dans la barre de recherche proposer le service de recherche a utiliser en cliquant dessus ( permet de le changer via radio button ou clic long)
3. En mode ajout (artistes ou chansons) les items ont une check box pour être ajoutés

## Page player

> Presente deux vues : liste et détail

1. Afficher la liste des chansons en cours avec menu options pour ajouter dans une playlist et voir la page de l'artiste 
2. Afficher des infos sur la chanson jouée et proposer des chansons similaires 

## Page mon compte

> Menu latéral qui gérera toutes les connexions plus les genres favoris

1. Liste des comptes liés (spotify soundcloud lastfm)
2. Genres favoris

## Playlists : Ajout de chansons 

Page recherche ## artiste ## album ## 