package com.rdm.triviachallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Rebecca on 1/24/2018.
 */

public class ResultsActivity extends AppCompatActivity {
    private static final String TAG = ResultsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        TextView yourScoreTextView = findViewById(R.id.resultScore);
        int maxScore = Question.getAllQuestionIDs(this).size() - 1;
        Integer yourScore = QuizUtils.getCurrentScore(this);
        String yourScoreText = getString(R.string.score_result, yourScore, maxScore);
        yourScoreTextView.setText(yourScoreText);
    }

    public void playAgain(View view) {
        Log.i(TAG, "BUTTON CLICKED - PlayAgain method called");
        Intent playAgainIntent = new Intent(this, MainActivity.class);
        startActivity(playAgainIntent);
    }
}
