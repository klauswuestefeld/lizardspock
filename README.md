Sneer API - 5 Minute Tutorial
====

This is a game of Rock-Paper-Scissors ready to play with your friends on Sneer.

![alt tag](http://i.imgur.com/nBrPhhz.png) . ![alt tag](http://i.imgur.com/4ESnGSw.png) . ![alt tag](http://i.imgur.com/x7FQgFu.png)

It is a plain Android project with a [single class](https://github.com/felipebueno/rockpaperscissors/blob/master/src/sneerteam/tutorial/rockpaperscissors/RockPaperScissorsActivity.java) implementing the game activity.

This tutorial highlights the parts of the code that use the Sneer API.


Running
----

  - Clone this project using git.

  - The Sneer API is available from the [Maven Central](http://search.maven.org/#browse%7C-358320422) repository. For this project, get it with one of these options:
    - Run gradle on Linux: ```./gradlew```
    - Run gradle on Windows: ```gradlew.bat```
    - Download the latest [sneer-api-nodeps.jar](http://dynamic.sneer.me/dist/snapi-nodeps/) into the project's libs folder.

  - Import the project into [Eclipse Android SDK](http://developer.android.com/sdk/index.html).

  - Run on devices with Sneer installed and play. :)


Using the API
----

Open the RockPaperScissorsActivity class and take a look at the code. We assume you are a [Sneer](https://play.google.com/store/search?q=SneerApp) user and know the basics of Android development.

Opening the Sneer contacts activity to pick an adversary for a match:
```JAVA
ContactPicker.pickContact(this).subscribe(new Action1<Contact>() {@Override public void call(Contact contact) {
	adversary = contact;
}});
```

Accessing the Sneer cloud:
```JAVA
cloud = Cloud.onAndroidMainThread(this);
```

All communication happens with values being published and subscribed on tree structures. The first segment on the path is the root of the tree. It is the public key of the owner of that tree.

Subscribing to challenges from a contact:
```JAVA
cloud.path(contact.publicKey(), "games", "rock-paper-scissors", ME).children().subscribe(new Action1<PathEvent>() { @Override public void call(final PathEvent child) {
	long matchTime = (Long)child.path().lastSegment();
	...
}});
```

Is this an old match we already played?
```JAVA
cloud.path(ME, "games", "rock-paper-scissors", contact.publicKey(), matchTime).exists(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Boolean>() { @Override public void call(Boolean exists) {
	if (exists) return;
	...
}});
```

Publishing happens on the user's own tree, so the first segment (public key) is omitted.

Publishing our move:
```JAVA
cloud.path("games", "rock-paper-scissors", adversary.publicKey(), matchTime).pub("ROCK");
```

Path segments and values can be: strings, long numbers, booleans, lists, sets or maps.

Listening to moves from our adversary:
```JAVA
cloud.path(adversary.publicKey(), "games", "rock-paper-scissors", ME, matchTime).value().subscribe(new Action1<Object>() { @Override public void call(Object theirMove) {
	...(String)theirMove...
}});
```

That's it. If you want to learn how to beat your friends at Rock-Paper-Scissors, take a look at [advanced gambit play](http://www.worldrps.com/gambit-play).

RxJava
----

The code above uses RxJava, a reactive programming lib included in the [Sneer API jar](http://search.maven.org/#browse%7C-358320422). RxJava provides [Observables](https://github.com/Netflix/RxJava/wiki/Observable), that make it easier to write code to handle concurrent, assynchronous events.

You can use it for simple callbacks, as above, or combine the events in many powerful ways.

RxJava has [great documentation](https://github.com/Netflix/RxJava/wiki/Observable) with many [cool diagrams](https://github.com/Netflix/RxJava/wiki/Combining-Observables#merge).
