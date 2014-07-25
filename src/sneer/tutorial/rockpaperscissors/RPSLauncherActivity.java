package sneer.tutorial.rockpaperscissors;

import sneer.*;
import android.app.*;
import android.os.*;

public class RPSLauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String title = "RPS Challenges";
		String type = "rock-paper-scissor/move";
		String newInteractionAction = "sneer.tutorial.rockpaperscissors.CHALLENGE";
		
		SneerAndroid.startInteractionList(this, title, type, newInteractionAction);
	}

}
