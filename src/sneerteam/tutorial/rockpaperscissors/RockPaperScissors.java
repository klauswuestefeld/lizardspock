package sneerteam.tutorial.rockpaperscissors;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import android.app.AlertDialog;
import android.content.Context;

class RockPaperScissors {

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

	
	private final Context context;
	
	RockPaperScissors(Context context) {
		this.context = context;
	}

	Observable<Adversary> pickAdversary() {
//Delete this code:
		alert("Now, to pick a Sneer contact as an adversary, uncomment the code in RockPaperScissors.pickAdversary() and run the app again.");
		return Observable.empty();

//Uncomment this code:		
//		return Observable.from(new Adversary());

	}
	
	Observable<Move> moveAgainst(Adversary adversary) {
//Delete this code:
		alert("Now, to receive your adversary's move, uncomment the code in RockPaperScissors.moveAgainst() and run the app again.");
		return Observable.empty();

//Uncomment this code:		
//		return Observable.from(Move.ROCK).delay(1000l, TimeUnit.MILLISECONDS);

	}
	
	void alert(CharSequence message) {
		new AlertDialog.Builder(context)
			.setTitle("Well done!")
			.setMessage(message)
		    .setCancelable(false)
		    .setPositiveButton("OK", null)
		    .create().show();
	}

}