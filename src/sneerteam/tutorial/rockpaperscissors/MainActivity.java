package sneerteam.tutorial.rockpaperscissors;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Choose your move");
	    
	    builder.setTitle("Choose your move").setItems(new CharSequence[]
	            {"Rock", "Paper", "Scissors"},
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int which) {
	                    switch (which) {
	                        case 0:
	                        	toast("choose rock");
	                            break;
	                        case 1:
	                        	toast("choose paper");
	                            break;
	                        case 2:
	                        	toast("choose scissors");
	                            break;
	                    }
	                }
	            });
	    builder.create().show();
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
			return true;
		} else if (id == R.id.action_invite) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	
	
	private void toast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

}