package sneerteam.tutorial.rockpaperscissors;

import java.util.concurrent.TimeUnit;

import rx.Observable;

public class RockPaperScissors {

	static boolean TUTORIAL_MODE = false;

	public class Adversary {
		@Override
		public String toString() {
			return "Neide";
		}
	}
	
	public interface MoveCallback {
		void handle(Move move);
	}

	public enum Move { ROCK, PAPER, SCISSORS }

	
	public Observable<Adversary> pickAdversary() {
		return Observable.from(new Adversary());
	}
	
	public Observable<Move> moveAgainst(Adversary adversary) {
		return Observable.from(Move.ROCK).delay(1000l, TimeUnit.MILLISECONDS);
	}

}