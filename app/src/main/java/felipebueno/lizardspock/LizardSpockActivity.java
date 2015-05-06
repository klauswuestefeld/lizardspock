package felipebueno.lizardspock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;

import sneer.android.Message;
import sneer.android.PartnerSession;

import static felipebueno.lizardspock.LizardSpockActivity.Move.LIZARD;
import static felipebueno.lizardspock.LizardSpockActivity.Move.PAPER;
import static felipebueno.lizardspock.LizardSpockActivity.Move.ROCK;
import static felipebueno.lizardspock.LizardSpockActivity.Move.SCISSORS;
import static felipebueno.lizardspock.LizardSpockActivity.Move.SPOCK;

public class LizardSpockActivity extends Activity {

	enum Move { ROCK, PAPER, SCISSORS, LIZARD, SPOCK };

	private Move yourMove;
	private boolean waitingForYourMove;

	private Move adversarysMove;
	private ProgressDialog waitingForAdversarysMove;
	private PartnerSession session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		session = PartnerSession.join(this, getIntent(), new PartnerSession.Listener() {      /////////////Sneer API
			@Override
			public void onUpToDate() {      /////////////Sneer API
				refresh();
			}

			@Override
			public void onMessage(Message message) {      /////////////Sneer API
				handle(message);
			}
		});
	}

	@Override
	protected void onDestroy() {
		session.close();     /////////////Sneer API
		super.onDestroy();
	}

	private void handle(Message message) {
		Move move = Move.valueOf((String) message.payload());
		if (message.wasSentByMe())
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
		if (waitingForAdversarysMove != null) waitingForAdversarysMove.dismiss();

		gameOver();
	}


	private void waitForYourMove() {
		if (waitingForYourMove) return;
		waitingForYourMove = true;

		alert("Choose Your Move", options("Rock", "Paper", "Scissors", "Lizard", "Spock"), new DialogInterface.OnClickListener() { @Override
		public void onClick(DialogInterface dialog, int option) {
			String move = Move.values()[option].name();
			session.send(move);  ///////////// Sneer API
		}});
	}


	private void gameOver() {
		String outcome = outcome();
		String message = "You used " + yourMove + ". Adversary used " + adversarysMove + ".";

		alert(outcome + " " + message, options("OK"), new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int which) {
			finish();
		}});
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


	private ProgressDialog progressDialog(String message) {
		ProgressDialog ret = ProgressDialog.show(this, null, message);
		ret.setIndeterminate(true);
		ret.setCancelable(true);
		ret.setOnCancelListener(new OnCancelListener() {  @Override public void onCancel(DialogInterface dialog) {
			finish();
		}});
		return ret;
	}


	protected void alert(String title, CharSequence[] items, DialogInterface.OnClickListener onClickListener) {
		new AlertDialog.Builder(this)
			.setTitle(title)
			.setItems(items, onClickListener)
			.setOnCancelListener(new OnCancelListener() {  @Override public void onCancel(DialogInterface dialog) {
				finish();
			}})
            .show();
	}


	private CharSequence[] options(CharSequence... options) {
		return options;
	}

}
