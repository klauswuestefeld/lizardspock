package sneerteam.tutorial.rockpaperscissors;

import rx.android.schedulers.*;
import rx.functions.*;
import sneerteam.tutorial.rockpaperscissors.RockPaperScissors.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

public class RockPaperScissorsActivity extends Activity {
    
    private final RockPaperScissors rps = new RockPaperScissors(this);
    private Adversary adversary;
    private Move move;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button button = (Button)findViewById(R.id.btnNewGame);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	challenge();
            }
        });
    }
    
    private void challenge() {
    	rps.pickAdversary().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Adversary>() {
			@Override
			public void call(Adversary adv) {
				adversary = adv;
				chooseMove();					
			}
		});    	
    }

	private void chooseMove() {
		move = null;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your move against " + adversary);
        builder.setItems(new CharSequence[] 
        {"Rock", "Paper", "Scissors"},
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	move = Move.values()[which];                    
                waitForAdversary();
            }
        });
        builder.create().show();
	}
    
    private void waitForAdversary() {
    	final ProgressDialog waiting = ProgressDialog.show(this, null, "Waiting for " + adversary + "...");
    	waiting.setIndeterminate(true);
    	waiting.setCancelable(true);    	
		rps.moveAgainst(adversary).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Move>() {
			@Override
			public void call(Move reply) {
				waiting.dismiss();
				onReply(reply);
		    }
		});
    }
    
    
    private void onReply(Move reply) {
    	String result = result(reply);                
    	String message = "You used " + move + ". " + adversary + " used " + reply + ".";

    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(result).setMessage(message).setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	playAgain();
		    }
		}).show();
    }

    
	private String result(Move reply) {
    	if (move == reply) return "Draw!";
    	
    	if (move == Move.ROCK     && reply == Move.SCISSORS) return "You win!";
    	if (move == Move.SCISSORS && reply == Move.PAPER   ) return "You win!";
    	if (move == Move.PAPER    && reply == Move.ROCK    ) return "You win!";
    	
    	return "You lose";
	}
    
    
    private void playAgain() {
        DialogInterface.OnClickListener chooseMove = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	chooseMove();
            }
        };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Challenge " + adversary + " again?");
        builder.setPositiveButton("Yes", chooseMove);
        builder.setNegativeButton("No", null);
        builder.show();
    }
}