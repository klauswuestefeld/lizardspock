package sneerteam.tutorial.rockpaperscissors;

public class RockPaperScissorsCloud {

	static boolean TUTORIAL_MODE = false;

	public class PublicKey {} // Trocar isso por uma classe PublicKey na Snapi. Networker já tem uma, vale a pena usar a mesma?

	public interface MoveCallback {
		void handle(Move move);
	}

	public enum Move { ROCK, PAPER, SCISSORS }

	
	public PublicKey pickAdversary() {
		return null;
	}

	public String nameFor(PublicKey adversary) {
		return "Neide";
	}

	public void moveAgainst(PublicKey adversary, Move move, MoveCallback onReply) {
<<<<<<< HEAD
=======
		try { Thread.sleep(3000); } catch (InterruptedException e) {}
>>>>>>> dfe05467400f94f0cd7c844bdef5be0e483b16f5
		onReply.handle(Move.ROCK);
	}

}