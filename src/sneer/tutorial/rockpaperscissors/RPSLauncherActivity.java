package sneer.tutorial.rockpaperscissors;

import sneer.*;
import android.app.*;
import android.os.*;

public class RPSLauncherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SneerAndroid.startInteractionList(this, 
				"RPS Challenges", 
				"rock-paper-scissor/move", 
				"sneer.tutorial.rockpaperscissors.CHALLENGE");
	}

}
