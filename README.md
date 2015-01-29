[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/iapcKVn7DdY/0.jpg)](http://www.youtube.com/watch?v=iapcKVn7DdY)


Sneer API - 5 Minute Tutorial
====

This is a game of Rock Paper Scissors Lizard Spock ready to play with your friends on Sneer.

![alt tag](https://raw.githubusercontent.com/felipebueno/lizardspock/master/screenshots/0.png) . ![alt tag](https://raw.githubusercontent.com/felipebueno/lizardspock/master/screenshots/1.png) . ![alt tag](https://raw.githubusercontent.com/felipebueno/lizardspock/master/screenshots/2.png) . ![alt tag](https://raw.githubusercontent.com/felipebueno/lizardspock/master/screenshots/3.png)

It is a plain Android project with a [single class](https://github.com/felipebueno/lizardspock/blob/master/src/felipebueno/lizardspock/LizardSpockActivity.java) implementing the entire game.

This tutorial highlights the parts of the code that use the Sneer API.


Running
----

  - Clone this project using git.

  - The Sneer API is available from the [Maven Central](http://search.maven.org/#browse%7C-358320422) repository. For this project, get it with one of these options:
    - Run gradle on Linux: ```./gradlew```
    - Run gradle on Windows: ```gradlew.bat```
    - Download the latest [sneer-api-nodeps.jar](http://dynamic.sneer.me/dist/snapi-nodeps/) into the project's libs folder.

  - Import the project into [Android Studio](http://developer.android.com/sdk/index.html).

  - Run on devices with Sneer installed and play. :)


Using the API
----

Open the MainActivity class and take a look at the code. We assume you are a [Sneer](https://play.google.com/store/search?q=SneerApp) user and know the basics of Android development.

step 1 WIP
```JAVA
	@Override
	protected void onPartnerName(String name) {
		adversary = name;
	}
```

step 2 WIP
```JAVA
	@Override
	protected void onMessageToPartner(Object message) {
		yourMove = Move.valueOf((String)message);
	}
```

step 3 WIP
```JAVA
	@Override
	protected void onMessageFromPartner(Object message) {
		adversarysMove = Move.valueOf((String)message);
	}
```

step 4 WIP
```JAVA
	@Override
	protected void update() {
		if (yourMove == null) {
			waitForYourMove();
			return;
		}

		if (adversarysMove == null) {
			waitingForAdversarysMove = progressDialog("Waiting for " + adversary + "...");
			return;
		}
		if (waitingForAdversarysMove != null)
		    waitingForAdversarysMove.dismiss();

		gameOver();
	}
```

TO BE CONTINUED...

That's it. Only five methods.

If you want to learn how to beat your friends at Rock Paper Scissors Lizard Spock, take a look at [advanced gambit play](http://www.worldrps.com/gambit-play) :).
