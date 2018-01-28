package com.rdm.triviachallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String GAME_FINISHED = "game_finished";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView highScoreTextView = findViewById(R.id.highscoreText);

        // Get the high and max score.
        int highScore = QuizUtils.getHighScore(this);
        int maxScore = Question.getAllQuestionIDs(this).size();

        // Set the high score text.
        String highScoreText = getString(R.string.high_score, highScore, maxScore);
        highScoreTextView.setText(highScoreText);
    }

    // If the game is over, show the game finished UI.
    @Override
    public Intent getIntent() {
        Intent intent = new Intent(super.getIntent());
        if (intent.hasExtra(GAME_FINISHED)) {

            Intent resultsIntent = new Intent(MainActivity.this, ResultsActivity.class);
            startActivity(resultsIntent);

            TextView gameFinishedTextView = findViewById(R.id.gameResult);
            TextView yourScoreTextView = findViewById(R.id.resultScore);
            int maxScore = Question.getAllQuestionIDs(this).size();
            Integer yourScore = QuizUtils.getCurrentScore(this);
            String yourScoreText = getString(R.string.score_result, yourScore, maxScore);
            yourScoreTextView.setText(yourScoreText);
            gameFinishedTextView.setVisibility(View.VISIBLE);
            yourScoreTextView.setVisibility(View.VISIBLE);
        }
        return intent;
    }

    /**
     * The OnClick method for the New Game button that begins the game.
     * @param view The Begin button.
     */
    public void newGame(View view) {
        Log.i(TAG, "BUTTON CLICKED - NEWGAME method called");
        Intent quizIntent = new Intent(this, QuizActivity.class);
        startActivity(quizIntent);
    }
}
