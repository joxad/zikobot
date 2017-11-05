[![Build Status](https://www.bitrise.io/app/ace43e7608e6cea0/status.svg?token=Y3XbouQU8l_wbwOfbtirAg&branch=v2)](https://www.bitrise.io/app/ace43e7608e6cea0)

Download LINK 
https://www.bitrise.io/artifact/3163405/p/3e1c768302cc305128a11f9bbf39dd64

# Zikobot

This project is an open-source alarm using Spotify SDK + Databinding

Link on the GooglePlay : https://play.google.com/apps/testing/com.joxad.zikobot


# Current Version

VERSION_NAME = 2.0.0-alpha

# How to Install

You will need Java 8 and at least version 3.0 of Android Studio

In the file local.properties, you will need to add the location of your java 8 installation, in order
to make retrolambda working

local.properties

```groovy
jdk.dir=/usr/lib/jvm/java-8-openjdk-amd64

keystore.file=path/to/keystore
file.password=passwordKeystore
keystore.alias=passwordAlias
keystore.password=password

```
# Goals

Zikobot is first an application on music.

The main goal is the play it on different support :

- Local
- Spotify
- Soundcloud
- Deezer
....

The second  is to create alarms with the different players / account of the user.

I wanted to test different techn that are going to me amazing (or not), or which are already.
So this project is a opportunity to make a clean architecture of a project, which regroups DB data + WebServices + Scheduled Tasks

- Create Alarms using music

- Create Alarm Scheduler with different media
    - Local Music
    - Spotify API + SDK (SDK implemented using android-easy-spotify)
    - Deezer (TODO)

- Connect to Spotify Auth (done)
    - Can it be donne better (I'm sure it can be, but I am still very new to Dagger)
- Launch a Spotify Player

# Major libs used

- DataBinding
In order to test MVVM on Android
- Rx
- Retrofit

# Thanks 

Some libs that made it possible (thanks to them)
 
- https://github.com/mrmaffen/vlc-android-sdk
- https://code.videolan.org/videolan/vlc-android
- https://github.com/deezer/android-sample
- https://github.com/spotify/android-sdk
- https://github.com/lapism/SearchView

# Contributing 

Feel free to contribute to this project :) Pull request are welcome !
