package alexandre.tetris12;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity {

	private TetrisView tetrisView = null;
	private TextView scoresView = null;
	private ScoreCounter scoreCounter = null;
	private Model model = new Model();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		tetrisView = TetrisView.class.cast(findViewById(R.id.tetris));
		tetrisView.setModel(model);
		tetrisView.setActivity(this);

		scoresView = TextView.class.cast(findViewById(R.id.scores));
		scoreCounter = new ScoreCounter(scoresView,
				getString(R.string.scores_format));
		model.setCounter(scoreCounter);

		final Button start = (Button) findViewById(R.id.start);
		start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (model.isGameOver() || model.isGameBeforeStart()) {
					startNewGame();
					start.setText("PAUSE");
				} else if (model.isGameActive()) {
					pauseGame();
					start.setText("RESUME");
				} else {
					resumeGame();
					start.setText("PAUSE");
				}
			}
		});

		final ImageButton right = (ImageButton) findViewById(R.id.right);
		right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (model.isGameActive()) {
					doMove(Model.Move.RIGHT);
				}
			}
		});

		final ImageButton left = (ImageButton) findViewById(R.id.left);
		left.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (model.isGameActive()) {
					doMove(Model.Move.LEFT);
				}
			}
		});

		final ImageButton down = (ImageButton) findViewById(R.id.down);
		down.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (model.isGameActive()) {
					doMove(Model.Move.DOWN);
				}
			}
		});

		final ImageButton rotate = (ImageButton) findViewById(R.id.rotate);
		rotate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (model.isGameActive()) {
					doMove(Model.Move.ROTATE);
				}
			}
		});
	}

	public void doMove(Model.Move move) {
		if (model.isGameActive()) {
			tetrisView.setGameCommand(move);
			scoresView.invalidate();
		}
	}

	public final void startNewGame() {
		if (!model.isGameActive()) {
			scoreCounter.reset();
			model.gameStart();
		}
	}

	public void endGame() {
		final Button start = (Button) findViewById(R.id.start);
		start.setText("START");
	}

	public void pauseGame() {
		model.setGamePaused();
	}
	
	public void resumeGame() {
		model.setGameActive();	
	}

	@Override
	protected void onPause() {
		super.onPause();
		pauseGame();
	}
}
