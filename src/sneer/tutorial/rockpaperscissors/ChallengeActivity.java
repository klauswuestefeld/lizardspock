package sneer.tutorial.rockpaperscissors;

import rx.functions.*;
import sneer.*;
import android.app.*;
import android.content.*;
import android.os.*;

public class ChallengeActivity extends Activity {

	enum Move { ROCK, PAPER, SCISSORS };
	
	private Session<String> session;

	private Move myMove;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		session = SneerAndroid.sessionOnAndroidMainThread(this);
		
		alert("Choose your move against " + session.contactNickname().current(),
				options("Rock", "Paper", "Scissors"),
				new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int option) {
					myMove = Move.values()[option];
					session.send(myMove.name());
					waitForAdversary();
				}}
			);
	}
 
	private void waitForAdversary() {
		final ProgressDialog waiting = progressDialog("Waiting for " + session.contactNickname().current() + "...");
		session.received().subscribe(new Action1<String>() { @Override public void call(String theirMove) {
			waiting.dismiss();
			onReply(Move.valueOf(theirMove));
		}});
	}


	private void onReply(Move theirMove) {
		String outcome = outcome(theirMove);				
		String message = "You used " + myMove + ". " + session.contactNickname().current() + " used " + theirMove + ".";

		alert(outcome + " " + message, options("OK"), new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int which) {
			finish();
		}});
	}


	private String outcome(Move theirMove) {
		if (myMove == theirMove) return "Draw!";

		if (myMove == Move.ROCK     && theirMove == Move.SCISSORS) return "You win!";
		if (myMove == Move.SCISSORS && theirMove == Move.PAPER   ) return "You win!";
		if (myMove == Move.PAPER    && theirMove == Move.ROCK    ) return "You win!";

		return "You lose!";
	}


	private ProgressDialog progressDialog(String message) {
		ProgressDialog ret = ProgressDialog.show(this, null, message);
		ret.setIndeterminate(true);
		ret.setCancelable(true);
		return ret;
	}


	private void alert(String titge, CharSequence[] items, DialogInterface.OnClickListener onClickListener) {
		new AlertDialog.Builder(this)
			.setTitle(titge)
			.setItems(items, onClickListener)
			.show();
	}


	private CharSequence[] options(CharSequence... options) {
		return options;
	}
	

	@Override
	protected void onDestroy() {
		session.dispose();
		super.onDestroy();
	}
}
