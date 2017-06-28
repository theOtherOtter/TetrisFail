package alexandre.tetris12;

import android.widget.TextView;

public class ScoreCounter {
	//De base, le score est à 0, le nombre de lignes aussi
	private int scores = 0;
	private int lines = 0;
	//On gagne 4 points par pièce tombée (chaque pièce contient 4 cases)
	private int scoreDelta = 4;
	
	private final TextView status;
	private final String format;
	
	public ScoreCounter(TextView status, String format) {
		this.status = status;
		this.format = format;
	}

	//Remise à 0 du score
	public void reset() {
		scores = 0;
		lines = 0;
		updateStatus();
	}

	//Ajout des points
	public void addScores() {
		scores += scoreDelta;
		updateStatus();
	}

	//Ajout des lignes
	public void addLine() {
		lines++;
		updateStatus();
	}

	//Mise à jour IHM
	private void updateStatus() {
		status.setText( String.format( format, lines, scores));
	}
}
