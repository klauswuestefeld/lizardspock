package sneerteam.tutorial.rockpaperscissors;

import sneerteam.tutorial.rockpaperscissors.RockPaperScissorsCloud.PublicKey;
import sneerteam.tutorial.rockpaperscissors.RockPaperScissorsCloud.Move;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RockPaperScissorsMain extends Activity {
    
    RockPaperScissorsCloud rps = new RockPaperScissorsCloud();
    private PublicKey adversary;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button button = (Button) findViewById(R.id.btnNewGame);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	playAgain();
            	
            	if (adversary == null)
                	adversary = rps.pickAdversary();
            }
        });
    }
    
    private void playAgain() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                    	move();
                    	break;
                    case DialogInterface.BUTTON_NEGATIVE:
                    	adversary = null;
                    	break;
                }
            }
        };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start game");
        builder.setMessage("Play with " + rps.nameFor(adversary) + "?");
        builder.setPositiveButton("Yes", dialogClickListener);
        builder.setNegativeButton("No", dialogClickListener);
        builder.show();
    }
    
    private void move() {        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your move");
        builder.setItems(new CharSequence[] 
        {"Rock", "Paper", "Scissors"},
        new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                    moveAgainst(Move.ROCK);
                    break;
                    case 1:
                    moveAgainst(Move.PAPER);
                    break;
                    case 2:
                    moveAgainst(Move.SCISSORS);
                    break;
                }
            }
        });
        builder.create().show();
    }
    
    private void moveAgainst(final Move move) {
        rps.moveAgainst(adversary, move, new RockPaperScissorsCloud.MoveCallback() {
            @Override
            public void handle(Move other) {
                String result = null;
                if (move == other) result = "draw";
                if (move == Move.ROCK && other == Move.SCISSORS) result = "win";
                if (move == Move.SCISSORS && other == Move.PAPER) result = "win";
                if (move == Move.PAPER && other == Move.ROCK) result = "win";
                if (result == null) result = "lose";
                
                if (result == "draw") {
                	result = "You used " + move + ". " + rps.nameFor(adversary) + " used " + other + ". Draw";
                } else if (result == "win") {
                	result = "You used " + move + ". " + rps.nameFor(adversary) + " used " + other + ". You win!";
                } else if (result == "lose") {
                	result = "You used " + move + ". " + rps.nameFor(adversary) + " used " + other + ". You lose";
                }
                
                msg("Game Over", result, "OK");
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