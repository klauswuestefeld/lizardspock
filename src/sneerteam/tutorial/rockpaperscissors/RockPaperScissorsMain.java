package sneerteam.tutorial.rockpaperscissors;

import sneerteam.tutorial.rockpaperscissors.RockPaperScissorsCloud.PublicKey;
import sneerteam.tutorial.rockpaperscissors.RockPaperScissorsCloud.Move;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class RockPaperScissorsMain extends ActionBarActivity {
    
    RockPaperScissorsCloud rps = new RockPaperScissorsCloud();
    private PublicKey adversary;
    private Move move;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        msg("Rock Paper Scissors", "Challenge a friend", "Invite");
    }
    
    private void playAgain() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                    play();
                    break;
                }
            }
        };
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start game");
        builder.setMessage("Play again?");
        builder.setPositiveButton("Yes", dialogClickListener);
        builder.setNegativeButton("No", dialogClickListener);
        builder.show();
    }
    
    private void play() {
        if (adversary == null)
        	adversary = rps.pickAdversary();
        
        move = null;
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your move");
        
        builder.setTitle("Choose your move").setItems(new CharSequence[]
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
                if (move == other) result = "Draw";
                if (move == Move.ROCK && other == Move.SCISSORS) result = "You Win!";
                if (move == Move.SCISSORS && other == Move.PAPER) result = "You Win!";
                if (move == Move.PAPER && other == Move.ROCK) result = "You Win!";
                if (result == null) result = "You lose!";
                
                msg("Gane Over", result, "OK");
            }
        });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_newGame) {
        	playAgain();
        } else if (id == R.id.action_contacts) {
        	adversary = rps.pickAdversary();
        }
        return super.onOptionsItemSelected(item);
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