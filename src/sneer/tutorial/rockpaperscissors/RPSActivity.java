package sneer.tutorial.rockpaperscissors;

import static sneer.tutorial.rockpaperscissors.RPSActivity.Move.*;
import sneer.android.ui.*;
import android.app.*;
import android.content.*;

public class RPSActivity extends SessionActivity {

	enum Move { ROCK, PAPER, SCISSORS };

	private Move yourMove;
	private boolean waitingForYourMove;

	private String adversary;
	private Move adversarysMove;
	private ProgressDialog waitingForAdversarysMove;


	@Override
	protected void onPeerName(String name) {
		adversary = name;
	}

	
	@Override
	protected void messageSent(Object content) {
		yourMove = Move.valueOf((String)content);
	}


	@Override
	protected void messageReceived(Object content) {
		adversarysMove = Move.valueOf((String)content);
	}

	
	@Override
	protected void react() {
		animateGame();
	}


	private void animateGame() {
		if (yourMove == null) {
			waitForYourMove();
			return;
		}
		
		if (adversarysMove == null) {
			waitingForAdversarysMove = progressDialog("Waiting for " + adversary + "...");
			return;
		}
		if (waitingForAdversarysMove != null) waitingForAdversarysMove.dismiss();
		
		gameOver();
	}


	private void waitForYourMove() {
		if (waitingForYourMove) return;
		waitingForYourMove = true;
		
		alert("Choose your move against " + adversary,
			options("Rock", "Paper", "Scissors"),
			new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int option) {
				sendMessage(Move.values()[option].name());
			}}
		);
	}


	private void gameOver() {
		String outcome = outcome();				
		String message = "You used " + yourMove + ". " + adversary + " used " + adversarysMove + ".";

		alert(outcome + " " + message, options("OK"), new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int which) {
			finish();
		}});
	}


	private String outcome() {
		if (yourMove == adversarysMove) return "Draw!";

		if (yourMove == ROCK     && adversarysMove == SCISSORS) return "You win!";
		if (yourMove == SCISSORS && adversarysMove == PAPER   ) return "You win!";
		if (yourMove == PAPER    && adversarysMove == ROCK    ) return "You win!";

		return "You lose!";
	}


	private ProgressDialog progressDialog(String message) {
		ProgressDialog ret = ProgressDialog.show(this, null, message);
		ret.setIndeterminate(true);
		ret.setCancelable(true);
		return ret;
	}


	private void alert(String title, CharSequence[] items, DialogInterface.OnClickListener onClickListener) {
		new AlertDialog.Builder(this)
			.setTitle(title)
			.setItems(items, onClickListener)
			.show();
	}


	private CharSequence[] options(CharSequence... options) {
		return options;
	}

}
