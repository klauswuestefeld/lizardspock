Sneer API - 5 Minute Tutorial
====

This is a game of Rock-Paper-Scissors ready to play with your friends on Sneer.

It is a plain Android project with a [single class](https://github.com/felipebueno/rockpaperscissors/blob/master/src/sneerteam/tutorial/rockpaperscissors/RockPaperScissorsActivity.java) implementing the game activity.

![alt tag](http://i.imgur.com/nBrPhhz.png) . ![alt tag](http://i.imgur.com/4ESnGSw.png)

This tutorial highlights the parts of the code that use the Sneer API. We assume you know how to use [Sneer](http://sneer.me) and the basics of Android development.


Getting Started
----

  - Clone this project using git.

  - Get the Sneer API jar: (Felipe, please check what the right commands are).
    - Maven: ```mvn build```
    - Gradle: ```./gradlew```
    - Manual: Download the [latest sneer-api-nodeps.jar](#) into the libs folder. 

  - Import the project into [Eclipse Android SDK](http://developer.android.com/sdk/index.html).

  - Run the project on a devices with Sneer installed and play. :)


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

![alt tag](http://i.imgur.com/x7FQgFu.png)

That's it. If you want to learn how to beat your friends at Rock-Paper-Scissors, take a look at [advanced gambit play](http://www.worldrps.com/gambit-play).

RxJava
----

The code above uses RxJava, a reactive programming lib included in the [Sneer API jar](#). RxJava provides [Observables](https://github.com/Netflix/RxJava/wiki/Observable), that make it easier to write code to handle concurrent, assynchronous events.

You can use it for simple callbacks, as above, or combine the events in many powerful ways.

RxJava has [great documentation](https://github.com/Netflix/RxJava/wiki/Observable) with many [cool diagrams](https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge).
