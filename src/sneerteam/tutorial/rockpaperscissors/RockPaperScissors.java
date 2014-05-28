package sneerteam.tutorial.rockpaperscissors;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.*;
import sneerteam.snapi.*;
import android.app.*;
import android.content.*;

class RockPaperScissors {

	class Adversary {
		private String publicKey;

		public Adversary(String publicKey) {
			this.publicKey = publicKey;
		}

		@Override
		public String toString() {
			return publicKey;
		}
	}
	
	interface MoveCallback {
		void handle(Move move);
	}

	enum Move { ROCK, PAPER, SCISSORS }

	
	private final Activity context;
	private ContactPicker contactPicker;
	
	RockPaperScissors(Activity context) {
		this.context = context;
	}

	Observable<Adversary> pickAdversary() {
//Delete this code:
		alert("Now, to pick a Sneer contact as an adversary, uncomment the code in RockPaperScissors.pickAdversary() and run the app again.");
		return Observable.empty();

//		contactPicker = new ContactPicker();
//		
//		return contactPicker.pickContact(context).map(new Func1<String, Adversary>() {
//			@Override
//			public Adversary call(String publicKey) {				
//				return new Adversary(publicKey);
//			}
//		});
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

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (contactPicker != null) {
			contactPicker.onActivityResult(requestCode, resultCode, data);
		}
	}

}