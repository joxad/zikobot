# musicalarm-spotify-mvvm
This project is an open-source alarm using Spotify SDK + Databinding + Dagger 2



# How to Install

You will need Java 8 and at least version 1.5.1 of AndroidStudio

# Goals

This project is an open source alarm which will be on the PlayStore.
I wanted to test different techn that are going to me amazing (or not), or which are already.
So this project is a opportunity to make a clean architecture of a project, which regroups DB data + WebServices + Scheduled Tasks

- Create Alarms using music 


- Create Alarm Scheduler with different media
    - Local Music  (TODO)
    - Spotify API + SDK (SDK implemented using android-easy-spotify)
    - Deezer (TODO)
    
- Connect to Spotify Auth (done)
    - Can it be donne better (I'm sure it can be, but I am still very new to Dagger)
- Launch a Spotify Player

# Major libs used

- DataBinding 
In order to test MVVM on Android
- Dagger 2
- Rx 
- Realm (it seems it is a new DB that is going better, so need to test that)
- Retrofit