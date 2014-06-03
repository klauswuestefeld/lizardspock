Sneer API - 5 Minute Tutorial
====

This project is a simple game of Rock/Paper/Scissors ready to play online.

![alt tag](http://i.imgur.com/nBrPhhz.png) . ![alt tag](http://i.imgur.com/4ESnGSw.png) . ![alt tag](http://i.imgur.com/x7FQgFu.png)

In this tutorial, you will learn how how to use the Sneer cloud. It's pretty simple but I assume you are familiar with [Reactive Programming](http://en.wikipedia.org/wiki/Reactive_programming). Here is a nice introduction on the subject: [Observable](https://github.com/Netflix/RxJava/wiki/Observable). You should definitely give it a try.

Getting Started
---------------
As it's a multiplayer game you'll need two emulators (or devices) to test the app.

Ok, clone this project using git, import it into your Android Development Toolkit workspace and follow the steps below.

  - Create a libs folder in the root folder of your project
  
  - Download the latest snapi-no-deps.jar from [here](#) and add it to the libs folder

  - Run the project on both emulators and play with it for a little :)
   
  
Now open the file RockPaperScissorsActivity.java and take a few minutes to understand the code until you feel confortable with it.

Usage
-----

To access the cloud:

```JAVA
Cloud cloud = new Cloud();
```
To challenge a friend for a match we open a contact picker that return a contact. Then we can subscribe to his/her tree and listen to his/her moves:
```JAVA
code goes here
```

To listen to challenges from our friends we do this:
```JAVA
code goes here
```

To send our move and pub it on our tree:
```JAVA
code goes here
```

To listen to adversary moves:
```JAVA
code goes here
```

That's it. When both players choose their move, we call the method onReply() to compare it and show the result.
