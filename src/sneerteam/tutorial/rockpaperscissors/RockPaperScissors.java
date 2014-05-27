package sneerteam.tutorial.rockpaperscissors;

import java.util.concurrent.TimeUnit;

import rx.Observable;

class RockPaperScissors {

	static boolean TUTORIAL_MODE = false;

	class Adversary {
		@Override
		public String toString() {
			return "Neide";
		}
	}
	
	interface MoveCallback {
		void handle(Move move);
	}

	enum Move { ROCK, PAPER, SCISSORS }

	
	Observable<Adversary> pickAdversary() {
		return Observable.from(new Adversary());
	}
	
	Observable<Move> moveAgainst(Adversary adversary) {
		return Observable.from(Move.ROCK).delay(1000l, TimeUnit.MILLISECONDS);
	}

}