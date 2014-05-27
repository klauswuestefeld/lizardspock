package sneerteam.tutorial.rockpaperscissors;

import sneerteam.tutorial.rockpaperscissors.RockPaperScissorsCloud.PublicKey;
import sneerteam.tutorial.rockpaperscissors.RockPaperScissorsCloud.Move;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

public class RockPaperScissorsMain extends Activity {
    
    RockPaperScissorsCloud rps = new RockPaperScissorsCloud();
    private PublicKey adversary;
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
        builder.setMessage("Challenge " + rps.nameFor(adversary) + " again?");
        builder.setPositiveButton("Yes", dialogClickListener);
        builder.setNegativeButton("No", dialogClickListener);
        builder.show();
    }
   	
    private void move() {
    	if (adversary == null)
        	adversary = rps.pickAdversary();
    	
    	move = null;
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your move against " + rps.nameFor(adversary));
        builder.setItems(new CharSequence[] 
        {"Rock", "Paper", "Scissors"},
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) move = Move.ROCK;                    
                if (which == 1) move = Move.PAPER;                    
                if (which == 2) move = Move.SCISSORS;                    
                waitForAdversary();
            }
        });
        builder.create().show();
    }
    
    private void waitForAdversary() {
    	final ProgressDialog waiting = ProgressDialog.show(this, null, "Wainting for " + rps.nameFor(adversary) + "...", true);
    	waiting.setCancelable(true);
        new Thread(new Runnable() {  
              @Override
              public void run() {
                    try {
                    	Thread.sleep(3000);   
                    	handle.sendMessage(handle.obtainMessage());
                    } catch(Exception e){}
                    waiting.dismiss();
              }
        }).start();
    }

    Handler handle = new Handler(){
        @Override
        public void handleMessage(Message msg) {
              super.handleMessage(msg);
              moveAgainst(move);
        }
    };
    
    private void moveAgainst(final Move move) {
        rps.moveAgainst(adversary, move, new RockPaperScissorsCloud.MoveCallback() {
            @Override
            public void handle(Move other) {
                String result = null;
                String resultMessage = null;
                if (move == other) result = "Draw!";
                if (move == Move.ROCK && other == Move.SCISSORS) result = "You win!";
                if (move == Move.SCISSORS && other == Move.PAPER) result = "You win!";
                if (move == Move.PAPER && other == Move.ROCK) result = "You win!";
                if (result == null) result = "You lose";
                
                if (result == "Draw!") {
                	resultMessage = "You used " + move + ". " + rps.nameFor(adversary) + " used " + other + ".";
                } else if (result == "You win!") {
                	resultMessage = "You used " + move + ". " + rps.nameFor(adversary) + " used " + other + ".";
                } else if (result == "You lose") {
                	resultMessage = "You used " + move + ". " + rps.nameFor(adversary) + " used " + other + ".";
                }
                
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