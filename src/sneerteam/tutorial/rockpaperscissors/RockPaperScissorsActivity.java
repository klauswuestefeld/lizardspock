package sneerteam.tutorial.rockpaperscissors;

import static sneerteam.snapi.CloudPath.*;

import java.util.concurrent.*;

import rx.functions.*;
import sneerteam.snapi.*;
import android.app.*;
import android.content.*;
import android.os.*;
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
	private String nickname;

	private long matchTime;
	private Move move;
	
	private AlertDialog currentAlert;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button(R.id.btnChallenge).setOnClickListener(new View.OnClickListener() { @Override public void onClick(View v) {
			challengeSomeFriend();
		}});
		
		cloud = Cloud.onAndroidMainThread(this);
		
		cloud.path(ME, "contacts").children().subscribe(new Action1<PathEvent>() { @Override public void call(PathEvent child) {
			String contactKey = (String)child.path().lastSegment();
			listenToChallengesFrom(contactKey);
		}});
	}


	private void listenToChallengesFrom(final String contactKey) {
		cloud.path(contactKey, GAMES, RPS, CHALLENGES, ME).value().cast(Long.class).subscribe(new Action1<Long>() { @Override public void call(final Long matchTime) {
			
			cloud.path(ME, GAMES, RPS, MATCHES, matchTime).exists(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Boolean>() { @Override public void call(Boolean exists) {
			    if (exists) return;
			    
			    adversary = contactKey;
			    RockPaperScissorsActivity.this.matchTime = matchTime;
			    startMatch();
		    }});
		}});
	}
	
	
	private void challengeSomeFriend() {
		ContactPicker.startActivityForResult(this, PICK_CONTACT_REQUEST);
  	}
  	@Override
  	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
  		super.onActivityResult(requestCode, resultCode, intent);
  		if (requestCode != PICK_CONTACT_REQUEST) return;
  		if (resultCode != RESULT_OK) return;

		adversary = ContactPicker.publicKeyFrom(intent);
		challengeAdversary();
  	}


	private void challengeAdversary() {
		matchTime = System.currentTimeMillis();
		cloud.path(GAMES, RPS, CHALLENGES, adversary).pub(matchTime);
		startMatch();
	}


	private void startMatch() {
		ContactUtils.nickname(cloud, adversary).subscribe(new Action1<String>() {
			@Override public void call(String nickname) {
				RockPaperScissorsActivity.this.nickname = nickname;
				chooseMove();
			}});
	}

 
	private void chooseMove() {
		move = null;
		alert("Choose your move against " + nickname,
			options("Rock", "Paper", "Scissors"),
			new DialogInterface.OnClickListener() { public void onClick(DialogInterface dialog, int option) {
				move = Move.values()[option];
				cloud.path(GAMES, RPS, MATCHES, matchTime).pub(move.name());
				waitForAdversary();
			}}
		);
	}


	private void waitForAdversary() {
		final ProgressDialog waiting = progressDialog("Waiting for " + nickname + "...");		
		cloud.path(adversary, GAMES, RPS, MATCHES, matchTime).value().subscribe(new Action1<Object>() { @Override public void call(Object theirMove) {
			waiting.dismiss();
			onReply(Move.valueOf((String)theirMove));
		}});
	}


	private void onReply(Move theirMove) {
		String outcome = outcome(theirMove);				
		String message = "You used " + move + ". " + nickname + " used " + theirMove + ".";

		alert(outcome + " " + message, options("OK"), new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int which) {
			playAgain();
		}});
	}


	private String outcome(Move theirMove) {
		if (move == theirMove) return "Draw!";

		if (move == Move.ROCK     && theirMove == Move.SCISSORS) return "You win!";
		if (move == Move.SCISSORS && theirMove == Move.PAPER   ) return "You win!";
		if (move == Move.PAPER    && theirMove == Move.ROCK    ) return "You win!";

		return "You lose!";
	}


	private void playAgain() {
		alert("Challenge " + nickname + " again?",
			options("Yes", "No"),
			new DialogInterface.OnClickListener() { @Override public void onClick(DialogInterface dialog, int option) {
				if (option == 0) challengeAdversary();
			}}
		);
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

}
