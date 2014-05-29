package sneerteam.tutorial.rockpaperscissors;

import java.util.*;

import rx.android.schedulers.*;
import rx.functions.*;
import sneerteam.snapi.*;
import sneerteam.tutorial.rockpaperscissors.RockPaperScissors.*;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.OnClickListener;
import android.os.*;
import android.view.*;
import android.widget.*;

public class RockPaperScissorsActivity extends Activity {
	
	private static final String GAMES = "games";
	private static final String RPS = "rock-paper-scissors";
	private static final String MATCHES = "matches";
	private static final String CHALLENGES = "challenges";

	private static final int PICK_CONTACT_REQUEST = 100;
	
	private final RockPaperScissors rps = new RockPaperScissors(this);
	private Cloud cloud;
	private String adversary;
	private Move move;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);		

		int rename_btnNewGame_to_btnChallenge;
		button(R.id.btnNewGame).setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
			challenge();
		}});
		
		cloud = Cloud.cloudFor(this);	   
		
		cloud.path(":me", "contacts").children().subscribe(new Action1<PathEvent>() { @Override public void call(PathEvent child) {
			final String contactKey = (String)child.path().lastSegment();
			cloud.path(contactKey, GAMES, RPS, ":me", CHALLENGES).value().cast(String.class).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() { @Override public void call(final String match) {
				int makeThisIntoANotificationInsteadOfAnAlert;
				alert("Challenge from " + contactKey,
					options("OK", "Cancel"),
					new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int option) {
						if (option == 0)
							onChallengeAccepted(contactKey, match);
					}});
			}});
		}});		
	}

	
	private void onChallengeAccepted(final String contactKey, String id) {
		chooseMove();
		cloud.path(contactKey, GAMES, RPS, MATCHES, id).value().subscribe(new Action1<Object>() { @Override public void call(Object event) {
			toast((String) event);
			// do stuff
		}});
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

		String match = UUID.randomUUID().toString();
		cloud.path(GAMES, RPS, adversary, CHALLENGES).pub(match);
		
		cloud.path(adversary, GAMES, RPS, MATCHES, match).value().subscribe(new Action1<Object>() { @Override public void call(Object event) {
			toast((String) event);
			// do stuff
		}}); 
  	}

  	
	private void chooseMove() {
		move = null;
		alert("Choose your move against " + adversary,
			options("Rock", "Paper", "Scissors"),
			new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int option) {
				move = Move.values()[option];					
				waitForAdversary();
			}}
		);
	}
	
	
	private void waitForAdversary() {
		final ProgressDialog waiting = progressDialog("Waiting for " + adversary + "...");		
		rps.moveAgainst(adversary).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Move>() { @Override public void call(Move reply) {
			waiting.dismiss();
			onReply(reply);
		}});
	}
	
	
	private void onReply(Move reply) {
		String outcome = outcome(reply);				
		String message = "You used " + move + ". " + adversary + " used " + reply + ".";

		alert(outcome, message, options("OK"), new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int which) {
			playAgain();
		}});
	}
	
	
	private String outcome(Move reply) {
		if (move == reply) return "Draw!";
		
		if (move == Move.ROCK     && reply == Move.SCISSORS) return "You win!";
		if (move == Move.SCISSORS && reply == Move.PAPER   ) return "You win!";
		if (move == Move.PAPER    && reply == Move.ROCK    ) return "You win!";
		
		return "You lose";
	}	
	
	
	private void playAgain() {
		alert("Challenge " + adversary + " again?",
			options("Yes", "No"),
			new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int option) {
				if (option == 0) chooseMove();
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