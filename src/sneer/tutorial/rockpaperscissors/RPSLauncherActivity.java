package sneer.tutorial.rockpaperscissors;

import sneer.*;
import android.app.*;

public class RPSLauncherActivity extends Activity {

	@Override
	protected void onResume() {
		super.onResume();
		
		String title = "RPS Challenges";
		String type = "rock-paper-scissors/move";
		String newInteractionLabel = "Challenge!!";
		String newInteractionAction = "sneer.tutorial.rockpaperscissors.CHALLENGE";
		
		SneerAndroid.startInteractionList(this, title, type, newInteractionLabel, newInteractionAction);
	}

}
