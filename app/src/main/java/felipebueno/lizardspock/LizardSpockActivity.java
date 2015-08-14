package felipebueno.lizardspock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;

import sneer.android.Message;
import sneer.android.PartnerSession;

import static android.app.AlertDialog.*;
import static felipebueno.lizardspock.LizardSpockActivity.Move.LIZARD;
import static felipebueno.lizardspock.LizardSpockActivity.Move.PAPER;
import static felipebueno.lizardspock.LizardSpockActivity.Move.ROCK;
import static felipebueno.lizardspock.LizardSpockActivity.Move.SCISSORS;
import static felipebueno.lizardspock.LizardSpockActivity.Move.SPOCK;

public class LizardSpockActivity extends Activity {

	private AlertDialog gameOverDialog;
	private AlertDialog moveDialog;

	enum Move { ROCK, PAPER, SCISSORS, LIZARD, SPOCK };

	private Move yourMove;
	private boolean waitingForYourMove;

	private Move adversarysMove;
	private ProgressDialog waitingForAdversarysMove;
	private PartnerSession session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		session = PartnerSession.join(this, new PartnerSession.Listener() {  /////////////Sneer API call
			@Override
			public void onUpToDate() {
				refresh();
			}

			@Override
			public void onMessage(Message message) {
				handle(message);
			}
		});
	}

	@Override
	protected void onDestroy() {
		session.close();                                        /////////////Sneer API call
		super.onDestroy();

		if (gameOverDialog != null) {
			gameOverDialog.dismiss();
			gameOverDialog = null;
		}

		if (moveDialog != null) {
			moveDialog.dismiss();
			moveDialog = null;
		}
	}

	private void handle(Message message) {
		Move move = Move.valueOf((String) message.payload());   /////////////Sneer API call
		if (message.wasSentByMe())                              /////////////Sneer API call
			yourMove = move;
		else
			adversarysMove = move;
	}


	private void refresh() {
        if (yourMove == null) {
			waitForYourMove();
			return;
		}

		if (adversarysMove == null) {
			waitingForAdversarysMove = progressDialog("Waiting for adversary...");
			return;
		}

		if (waitingForAdversarysMove != null) {
			waitingForAdversarysMove.dismiss();
			waitingForAdversarysMove = null;
		}

		gameOver();
	}


	private void waitForYourMove() {
		if (waitingForYourMove) return;
		if (moveDialog != null) return;
		if (this.isFinishing()) return;

		waitingForYourMove = true;

		moveDialog = builder("Choose Your Move")
						.setItems(options("Rock", "Paper", "Scissors", "Lizard", "Spock"), new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int option) {
							String move = Move.values()[option].name();
							session.send(move);      ///////////// Sneer API
						}})
						.show();
	}


	private void gameOver() {
		String outcome = outcome();
		String message = "You used " + yourMove + ". Adversary used " + adversarysMove + ".";

		if (gameOverDialog != null) return;
		if (this.isFinishing()) return;

		gameOverDialog = builder(outcome + " " + message)
							.setPositiveButton("OK", new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int which) {
								finish();
							}})
							.show();
	}


	private String outcome() {
		if (yourMove == adversarysMove) return "Draw!";

		if (yourMove == ROCK     && adversarysMove == SCISSORS) return "You win!";
		if (yourMove == ROCK     && adversarysMove == LIZARD  ) return "You win!";

		if (yourMove == SCISSORS && adversarysMove == PAPER   ) return "You win!";
		if (yourMove == SCISSORS && adversarysMove == LIZARD  ) return "You win!";

		if (yourMove == PAPER    && adversarysMove == ROCK    ) return "You win!";
		if (yourMove == PAPER    && adversarysMove == SPOCK   ) return "You win!";

		if (yourMove == LIZARD   && adversarysMove == SPOCK   ) return "You win!";
		if (yourMove == LIZARD   && adversarysMove == PAPER   ) return "You win!";

		if (yourMove == SPOCK    && adversarysMove == ROCK    ) return "You win!";
		if (yourMove == SPOCK    && adversarysMove == SCISSORS) return "You win!";

		return "You lose!";
	}


	private Builder builder(String title) {
		return new Builder(this)
				.setTitle(title)
				.setOnCancelListener(new OnCancelListener() { @Override public void onCancel(DialogInterface dialog) {
					finish();
				}});
	}


	private ProgressDialog progressDialog(String message) {
		if (waitingForAdversarysMove != null) return null;
		if (this.isFinishing()) return null;

		ProgressDialog ret = ProgressDialog.show(this, null, message);
		ret.setIndeterminate(true);
		ret.setCancelable(true);
		ret.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
			finish();
		}});
		return ret;
	}


	private CharSequence[] options(CharSequence... options) {
		return options;
	}

}
