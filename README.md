Sneer API - 5 Minute Tutorial
====

This is a game of Rock-Paper-Scissors ready to play with your friends on Sneer.

![alt tag](http://i.imgur.com/nBrPhhz.png) . ![alt tag](http://i.imgur.com/4ESnGSw.png) . ![alt tag](http://i.imgur.com/x7FQgFu.png)

It is a plain Android project with a [single class](https://github.com/felipebueno/rockpaperscissors/blob/master/src/sneerteam/tutorial/rockpaperscissors/RockPaperScissorsActivity.java) implementing the game activity.

This tutorial highlights the parts of the code that use the Sneer API.


Running
----

  - Clone this project using git.

  - Get the Sneer API jar using one of these: (Felipe, please check what the right commands are).
    - Maven: ```mvn build```
    - Gradle: ```./gradlew```
    - Manual: Download the [latest sneer-api-nodeps.jar](https://github.com/sneerteam/snapi/releases) into the libs folder. 

  - Import the project into [Eclipse Android SDK](http://developer.android.com/sdk/index.html).

  - Run on devices with Sneer installed and play. :)


Using the API
----

Open the RockPaperScissorsActivity class and take a look at the code. We assume you know how to use [Sneer](http://sneer.me) and the basics of Android development.

Accessing the Sneer cloud:
```JAVA
cloud = Cloud.onAndroidMainThread(this);
```

Choosing an adversary for a match:
```JAVA
ContactPicker.pickContact(this).subscribe(new Action1<Contact>() {@Override public void call(Contact contact) {
	adversary = contact;
}});
```

Subscribing to challenges from our friends:
```JAVA
cloud.path(contact.publicKey(), GAMES, RPS, ME).children().subscribe(new Action1<PathEvent>() { @Override public void call(final PathEvent child) {
	long matchTime = (Long)child.path().lastSegment();
	...
}});
```

Is this an old match we already played?
```JAVA
cloud.path(ME, GAMES, RPS, contact.publicKey(), matchTime).exists(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Boolean>() { @Override public void call(Boolean exists) {
	if (exists) return;
	...
}});
```

Sending our move:
```JAVA
cloud.path(GAMES, RPS, adversary.publicKey(), matchTime).pub("ROCK");
```

Listening to moves from our adversary:
```JAVA
cloud.path(adversary.publicKey(), GAMES, RPS, ME, matchTime).value().subscribe(new Action1<Object>() { @Override public void call(Object theirMove) {
	...(String)theirMove...
}});
```

That's it. If you want to learn how to beat your friends at Rock-Paper-Scissors, take a look at [advanced gambit play](http://www.worldrps.com/gambit-play).

RxJava
----

The code above uses RxJava, a reactive programming lib included in the [Sneer API jar](#). RxJava provides [Observables](https://github.com/Netflix/RxJava/wiki/Observable), that make it easier to write code to handle concurrent, assynchronous events.

You can use it for simple callbacks, as above, or combine the events in many powerful ways.

RxJava has [great documentation](https://github.com/Netflix/RxJava/wiki/Observable) with many [cool diagrams](https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge).
