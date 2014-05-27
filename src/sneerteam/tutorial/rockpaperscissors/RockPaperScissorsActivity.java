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
    
    RockPaperScissors rps = new RockPaperScissors();
    private Adversary adversary;
    private Move move;
        
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button button = (Button) findViewById(R.id.btnNewGame);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	move();
            }
        });
    }
    
    private void playAgain() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	if (which == DialogInterface.BUTTON_POSITIVE)
                   	move();
            	if (which == DialogInterface.BUTTON_NEGATIVE)
                  	adversary = null;
            }
        };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Challenge " + adversary + " again?");
        builder.setPositiveButton("Yes", dialogClickListener);
        builder.setNegativeButton("No", dialogClickListener);
        builder.show();
    }
   	
    private void move() {
    	if (adversary == null) {
        	rps.pickAdversary().observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Adversary>() {
				@Override
				public void call(Adversary foe) {
					adversary = foe;
					chooseMove();					
				}
			});    	
    	} else {
    		chooseMove();
    	}
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
    	final ProgressDialog waiting = ProgressDialog.show(this, null, "Waiting for " + adversary + "...", true);
    	waiting.setCancelable(true);    	
		rps.moveAgainst(adversary).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Move>() {
			@Override
			public void call(Move other) {
				waiting.dismiss();
		       
				String result = null;
		        String resultMessage = null;
		        
		        if (move == other) result = "Draw!";
		        if (move == Move.ROCK && other == Move.SCISSORS) result = "You win!";
		        if (move == Move.SCISSORS && other == Move.PAPER) result = "You win!";
		        if (move == Move.PAPER && other == Move.ROCK) result = "You win!";
		        if (result == null) result = "You lose";                
		        
		       	resultMessage = "You used " + move + ". " + adversary + " used " + other + ".";
		        msg(result, resultMessage, "OK");
		    }
		});
    }
    
    private void msg(String title, String message, String button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setMessage(message).setPositiveButton(button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	playAgain();
            }
        }).show();
    }    
}