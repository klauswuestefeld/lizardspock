package sneerteam.tutorial.rockpaperscissors;

import java.util.*;

import rx.android.schedulers.*;
import rx.functions.*;
import sneerteam.snapi.*;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnClickListener;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class RockPaperScissorsActivity extends Activity {

	enum Move { ROCK, PAPER, SCISSORS };
	
	private static final String GAMES = "games";
	private static final String RPS = "rock-paper-scissors";
	private static final String MATCHES = "matches";
	private static final String CHALLENGES = "challenges";

	private static final int PICK_CONTACT_REQUEST = 100;

	private Cloud cloud;
	private String adversary;
	private String match;
	private Move move;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		

		button(R.id.btnChallenge).setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
			challenge();
		}});
		
		cloud = Cloud.cloudObservingOnAndroidMainThread(this);	   
		
		cloud.path(":me", "contacts").children().subscribe(new Action1<PathEvent>() { @Override public void call(PathEvent child) {
			final String contactKey = (String)child.path().lastSegment();
			cloud.path(contactKey, GAMES, RPS, CHALLENGES, ":me").value().cast(String.class).subscribe(new Action1<String>() { @Override public void call(final String match) {
				RockPaperScissorsActivity.this.match = match;
				int makeThisIntoANotificationInsteadOfAnAlert;
				adversary = contactKey;
				alert("Challenge from " + contactKey,
					options("OK", "Cancel"),
					new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int option) {
						boolean accepted = option == 0;
						onChallengeReceived(contactKey, match, accepted);
					}}
				);
			}});
		}});		
	}

	
	private void onChallengeReceived(final String contactKey, String match, boolean accepted) {
		Log.d("RockPaperScissorsActivity", "-----> testeste 1");
		if (!accepted) return;
		chooseMove();		
	}
	

	private void challenge() {
		ContactPicker.startActivityForResult(this, PICK_CONTACT_REQUEST);
  	}	
  	
	
  	@Override
  	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
  		super.onActivityResult(requestCode, resultCode, intent);
  		if (requestCode != PICK_CONTACT_REQUEST) return;
  		if (resultCode != RESULT_OK) return;

		adversary = ContactPicker.publicKeyFrom(intent);
		toast(adversary);

		startMatch(); 
  	}


	private void startMatch() {
		String match = UUID.randomUUID().toString();
		cloud.path(GAMES, RPS, CHALLENGES, adversary).pub(match);
		
		cloud.path(adversary, GAMES, RPS, MATCHES, match).value().subscribe(new Action1<Object>() { @Override public void call(Object event) {
			Log.d("RockPaperScissors", "-----> testeste 2");
			toast((String) event);
			chooseMove();
			// do stuff
		}});
	}

  	
	private void chooseMove() {
		move = null;
		alert("Choose your move against " + adversary,
			options("Rock", "Paper", "Scissors"),
			new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int option) {
				move = Move.values()[option];
				cloud.path(GAMES, RPS, MATCHES, match).pub(move.name());
				waitForAdversary();
			}}
		);
	}
	
	
	private void waitForAdversary() {
		final ProgressDialog waiting = progressDialog("Waiting for " + adversary + "...");		
		cloud.path(adversary, GAMES, RPS, MATCHES, match).value().subscribe(new Action1<Object>() { @Override public void call(Object theirMove) {
			waiting.dismiss();
			onReply(Move.valueOf((String)theirMove));
		}});
	}
	
	
	private void onReply(Move theirMove) {
		String outcome = outcome(theirMove);				
		String message = "You used " + move + ". " + adversary + " used " + theirMove + ".";

		alert(outcome, message, options("OK"), new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int which) {
			playAgain();
		}});
	}
	
	
	private String outcome(Move theirMove) {
		if (move == theirMove) return "Draw!";
		
		if (move == Move.ROCK     && theirMove == Move.SCISSORS) return "You win!";
		if (move == Move.SCISSORS && theirMove == Move.PAPER   ) return "You win!";
		if (move == Move.PAPER    && theirMove == Move.ROCK    ) return "You win!";
		
		return "You lose";
	}	
	
	
	private void playAgain() {
		alert("Challenge " + adversary + " again?",
			options("Yes", "No"),
			new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int option) {
				if (option == 0) startMatch();
			}}
		);
	}
	
	
	private void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
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


	private void alert(String title, CharSequence[] items, OnClickListener onClickListener) {
		alert(title, null, items, onClickListener);
	}


	private void alert(String title, CharSequence message, CharSequence[] items, DialogInterface.OnClickListener onClickListener) {
		new AlertDialog.Builder(this)
			.setTitle(title)
			.setMessage(message)
			.setItems(items, onClickListener)
			.show();
	}


	private CharSequence[] options(CharSequence... options) {
		return options;
	}
}