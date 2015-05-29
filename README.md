[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/iapcKVn7DdY/0.jpg)](http://www.youtube.com/watch?v=iapcKVn7DdY)


Sneer API - 5 Minute Tutorial
====

This is a game of Rock-Paper-Scissors-Lizard-Spock ready to play with your friends on Sneer.

![alt tag](https://raw.githubusercontent.com/felipebueno/lizardspock/master/screenshots/0.png) . ![alt tag](https://raw.githubusercontent.com/felipebueno/lizardspock/master/screenshots/1.png) . ![alt tag](https://raw.githubusercontent.com/felipebueno/lizardspock/master/screenshots/2.png) . ![alt tag](https://raw.githubusercontent.com/felipebueno/lizardspock/master/screenshots/3.png)

It is a plain Android project with a [single class](https://github.com/felipebueno/lizardspock/blob/master/app/src/main/java/felipebueno/lizardspock/LizardSpockActivity.java) implementing the entire game.

The parts of the code that use the 5 methods of the Sneer PartnerSession API are highlighted.

Dependency
----
The dependency to the Sneer API is declared like this:
```
compile 'me.sneer:sneer-android-api:X.Y.Z'
```
in the dependencies section of the [app/build.gradle](https://github.com/felipebueno/lizardspock/blob/master/app/build.gradle) file. X.Y.Z stands for the latest version found [here](http://search.maven.org/#search|ga|1|sneer-android-api).

Running
----

  - Clone this git repository.

  - Import as a gradle project into [Android Studio](http://developer.android.com/sdk/index.html).

  - Run on devices with the [latest version of Sneer](https://github.com/sneerteam/sneer/releases/latest) installed and [play](http://www.worldrps.com/gambit-play) :).
