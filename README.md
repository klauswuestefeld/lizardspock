Sneer API - 5 Minute Tutorial
====

This project is a simple game of Rock/Paper/Scissors ready to play online.

![alt tag](http://i.imgur.com/nBrPhhz.png) . ![alt tag](http://i.imgur.com/4ESnGSw.png) . ![alt tag](http://i.imgur.com/x7FQgFu.png)

In this tutorial, you will learn how how to use the Sneer cloud. It's pretty simple but I assume you are familiar with [Reactive Programming](http://en.wikipedia.org/wiki/Reactive_programming). Here is a nice introduction on the subject: [Functional Reactive Programming on Android With RxJava](http://mttkay.github.io/blog/2013/08/25/functional-reactive-programming-on-android-with-rxjava/). You should definitely give it a try.

Getting Started
---------------
As it's a multiplayer game you'll need two emulators (or devices) to test the app.

Ok, clone this project using git, import it into your Android Development Toolkit workspace and follow the steps below.

  - Create a libs folder in the root folder of your project
  
  - Download the latest snapi-no-deps.jar from [here](#) and add it to the libs folder

  - Run the project on both emulators and play with it for a little :)
   
  
Now open the file RockPaperScissorsActivity.java and take a few minutes to understand the code until you feel confortable with it.

...

To access the cloud:

```JAVA
Cloud cloud = new Cloud();
```
To challenge a friend for a match we open a contact picker that return a contact. Then we can subscribe to his/her tree and listen to his/her moves:
```JAVA
private void challenge() {
    ContactPicker.startActivityForResult(this, PICK_CONTACT_REQUEST);
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    ...

	adversary = ContactPicker.publicKeyFrom(intent);

	ContactUtils.nickname(cloud, adversary).subscribe(new Action1<String>() {
	    @Override public void call(String nickname) {
			RockPaperScissorsActivity.this.nickname = nickname;
			startMatch(); 
        }});
}
```

To listen to challenges from friends we do this:
```JAVA
cloud.path(ME, "contacts").children().subscribe(new Action1<PathEvent>() { @Override public void call(PathEvent child) {
	final String contactKey = (String)child.path().lastSegment();
	cloud.path(contactKey, GAMES, RPS, CHALLENGES, ME).value().cast(String.class).subscribe(new Action1<String>() { @Override public void call(final String match) {
		RockPaperScissorsActivity.this.match = match;
		
		cloud.path(ME, GAMES, RPS, MATCHES, match).exists(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Boolean>() { @Override public void call(Boolean exists) {
		    if (exists) return;
            adversary = contactKey;
                    
            ContactUtils.nickname(cloud, contactKey).subscribe(new Action1<String>() {@Override public void call(String nickname) {
				RockPaperScissorsActivity.this.nickname = nickname;
                alert("Challenge from " + nickname, options("OK", "Cancel"), new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int option) {                    				boolean accepted = option == 0;
                    onChallengeReceived(contactKey, match, accepted);
                }});
            }});
        }});
	}});
}});
```

Then we do that:
```JAVA
private void chooseMove() {
...
    move = Move.values()[option];
    cloud.path(GAMES, RPS, MATCHES, match).pub(move.name());
    waitForAdversary();
...
}
```

etc
