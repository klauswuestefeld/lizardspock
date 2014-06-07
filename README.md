Sneer API - 5 Minute Tutorial
====

This is a game of Rock-Paper-Scissors ready to play with your friends on Sneer.

It is a plain Android project with a [single class](https://github.com/felipebueno/rockpaperscissors/blob/master/src/sneerteam/tutorial/rockpaperscissors/RockPaperScissorsActivity.java) implementing the game activity.

![alt tag](http://i.imgur.com/nBrPhhz.png) . ![alt tag](http://i.imgur.com/4ESnGSw.png)

This tutorial highlights the parts of the code that use the Sneer API. We assume you are an experienced [Sneer](http://sneer.me) user and Android developer.


Getting Started
----

  - Clone this project using git.

  - Get the latest Sneer API jar: (Felipe, please check what the right commands are).
    - Maven: ```mvn build```
    - Gradle: ```./gradlew```
    - Manual: Download the [latest sneer-api-nodeps.jar](#) into the libs folder. 

  - Import the project into your [Eclipse Android SDK](http://developer.android.com/sdk/index.html) workspace.

  - Run the project on two devices or emulators and play. :)


Usage
----

Open the RockPaperScissorsActivity class and take a look at the code.

Accessing the Sneer cloud:
```JAVA
Cloud cloud = new Cloud();
```

Choosing an adversary for a match:
```JAVA
code goes here
```

Subscribing to challenges from our friends:
```JAVA
code goes here
```

Sending our move:
```JAVA
code goes here
```

Listening to moves from our adversary:
```JAVA
code goes here
```

That's it. If you want to learn how to beat your friends at Rock-Paper-Scissors, take a look at [advanced gambit play](http://www.worldrps.com/gambit-play).

![alt tag](http://i.imgur.com/x7FQgFu.png)

RxJava
----

The simple code above actually uses RxJava, a reactive programming lib included in the [Sneer API jar](#). RxJava provides [Observables](https://github.com/Netflix/RxJava/wiki/Observable), that make it easier to write code that handles concurrent, assynchronous events.

You can use it for simple callbacks, as above, or combine the events in many useful ways.

RxJava has [great documentation](https://github.com/Netflix/RxJava/wiki/Observable) with many [cool diagrams](https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge).
