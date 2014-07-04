package sneer.tutorial.rockpaperscissors;

import rx.functions.*;
import sneer.snapi.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class NewRockPaperScissorsActivity extends Activity {

	enum Move { ROCK, PAPER, SCISSORS };

	private static final String GAMES = "games";
	private static final String RPS = "rock-paper-scissors";

	private Cloud cloud;

	private Contact adversary;
	private Move adversaryMove;

	private Move move;

	private AlertDialog currentAlert;
	private ProgressDialog waitingDialog;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		button(R.id.btnChallenge).setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
			challengeSomeFriend();
		}});

		cloud = Cloud.onAndroidMainThread(this);
		
		//cloud.path("asdf", "asd").distinct() ?
		
		cloud.notificationPath(this.getClass(), GAMES, RPS).notifications().subscribe(new Action1<CloudNotification>() {@Override public void call(CloudNotification adversaryNotification) {
		    handle(adversaryNotification);
        }});
	}


	private void handle(CloudNotification adversaryNotification) {
		if (isAlreadyPlaying()) return;
		if (adversary == null) adversary = adversaryNotification.contact;
		if (isFromSomebodyElse(adversaryNotification)) return;
		stopWaiting();
		adversaryMove = Move.valueOf((String)adversaryNotification.payload);
		onMove();
	}


	private boolean isAlreadyPlaying() {
		return adversaryMove != null;
	}


	private void stopWaiting() {
		if (waitingDialog == null) return;
		waitingDialog.dismiss();
		waitingDialog = null;
	}


	private boolean isFromSomebodyElse(CloudNotification adversaryNotification) {
		return adversaryNotification.contact != adversary;
	}


	private void challengeSomeFriend() {
		ContactPicker.pickContact(this).subscribe(new Action1<Contact>() {@Override public void call(Contact contact) {
            adversary = contact;
			makeMyMove();
        }});
  	}


	private void makeMyMove() {
		alert("Choose your move against " + adversary.nickname(),
			options("Rock", "Paper", "Scissors"),
			new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int option) {
				move = Move.values()[option];
				cloud.notificationPath(NewRockPaperScissorsActivity.class, GAMES, RPS).pub(adversary.publicKey(), "Rock Paper Scissors challenge", move.name());
				onMove();
			}}
		);
	}


	private void onMove() {
		if (move == null) {
			makeMyMove();
			return;
		}

		if (adversaryMove == null) {
			waitForAdversary();
			return;
		}

		showOutcome();

		reset();
	}


	private void reset() {
		adversary = null;
		adversaryMove = null;
		move = null;
	}


	private void waitForAdversary() {
		waitingDialog = progressDialog("Waiting for " + adversary.nickname() + "...");
	}


	private void showOutcome() {
		String outcome = outcome();
		String message = "You used " + move + ". " + adversary.nickname() + " used " + adversaryMove + ".";

		alert(outcome + " " + message, options("OK"), new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int which) {
		}});
	}


	private String outcome() {
		if (move == adversaryMove) return "Draw!";

		if (move == Move.ROCK     && adversaryMove == Move.SCISSORS) return "You win!";
		if (move == Move.SCISSORS && adversaryMove == Move.PAPER   ) return "You win!";
		if (move == Move.PAPER    && adversaryMove == Move.ROCK    ) return "You win!";

		return "You lose!";
	}


	private Button button(int id) {
		return (Button)findViewById(id);
	}


	private ProgressDialog progressDialog(String message) {
		ProgressDialog ret = ProgressDialog.show(this, null, message);
		ret.setIndeterminate(true);
		ret.setCancelable(true);
		return ret;
	}


	private void alert(String titge, CharSequence[] items, DialogInterface.OnClickListener onClickListener) {
		if (currentAlert != null) currentAlert.dismiss();

		currentAlert = new AlertDialog.Builder(this)
			.setTitle(titge)
			.setItems(items, onClickListener)
			.show();
	}


	private CharSequence[] options(CharSequence... options) {
		return options;
	}


//	@Override
//	protected void onResume() {
//		super.onResume();
//		cloud.unregisterForNotification(GAMES, RPS, ME);
//	}
//
//
//	@Override
//	protected void onPause() {
//		Intent intent = new Intent(this, NewRockPaperScissorsActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		cloud.registerForNotification(intent, GAMES, RPS, ME);
//		super.onPause();
//	}

	@Override
	protected void onDestroy() {
		if (cloud != null) cloud.dispose();
		super.onDestroy();
	}

	void toast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}


}
