Rock Paper Scissors
===================

Really simple Rock Paper Scissors game to show the Sneer API (snapi) in action.

It's also an interactive tutorial showing how to use the snapi in your own project.

##5 Minute Tutorial

+ this tutorial, we are going to add the missing code to use the Sneer cloud.
  - Clone this project using git and import it into the Android Development Toolkit.
  - Run the project. The app itself will tell you what to do to make it work, so you can stop reading here and just use the app. :)
  - Download the latest snapi.jar from here[link] and add it to [path inside project].

+ To access the cloud:

```JAVA
Cloud cloud = new Cloud();
```

+ To challenge a friend for a match:

```JAVA
cloud.pub...
```

+ To listen to challenges from friends:

```JAVA
cloud.sub…
```
etc.