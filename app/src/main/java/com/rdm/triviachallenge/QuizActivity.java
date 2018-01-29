package com.rdm.triviachallenge;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Rebecca on 1/24/2018.
 */

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = QuizActivity.class.getSimpleName();
    private static final int CORRECT_ANSWER_DELAY_MILLIS = 1000;
    private static final String REMAINING_QUESTIONS_KEY = "remaining_questions";
    private int mCurrentScore;
    private int mHighScore;
    private Button[] mButtons;
    private int[] mButtonIDs = {R.id.buttonTrue, R.id.buttonFalse};
    private ArrayList<Integer> mRemainingQuestionIDs = new ArrayList<>();
    private ArrayList<Integer> mQuestionIDs = new ArrayList<>();
    private int mAnswerID;
    private TextView mCategoryView;
    private TextView mQuestionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "QUIZACTIVITY: OnCreate Method called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        boolean isNewGame = !getIntent().hasExtra(REMAINING_QUESTIONS_KEY);

        // Initialize the Category & Question view, from an included layout.
        View includedLayout = findViewById(R.id.includedLayout);
        mCategoryView = includedLayout.findViewById(R.id.categoryView);
        mQuestionView = includedLayout.findViewById(R.id.questionView);

        // If it's a new game, set the current score to 0 and load all questions.
        if (isNewGame) {
            QuizUtils.setCurrentScore(this, 0);
            mRemainingQuestionIDs = Question.getAllQuestionIDs(this);

        // Otherwise, get the remaining questions from the Intent.
        } else {
            mRemainingQuestionIDs = getIntent().getIntegerArrayListExtra(REMAINING_QUESTIONS_KEY);
        }

        // Get current score.
        mCurrentScore = QuizUtils.getCurrentScore(this);
        mHighScore = QuizUtils.getHighScore(this);

        // Generate a question and get the correct answer.
        mQuestionIDs = QuizUtils.generateQuestion(mRemainingQuestionIDs);
        mAnswerID = QuizUtils.getCorrectAnswerID(mQuestionIDs);

        Question currentQuestion = Question.getQuestionByID(this, mAnswerID);
        Log.i(TAG, "onClick: Current Question is " + currentQuestion.getQuestion());
        Log.i(TAG, "onClick: Current Answer is " + currentQuestion.getCorrectAnswer());

        if (currentQuestion == null) {
            Toast.makeText(this, getString(R.string.question_not_found_error),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // If there is only one answer left, end the game.
        if (mQuestionIDs.size() < 2) {
            QuizUtils.endGame(this);
            finish();
        }

        //Display the current question
        String categoryView = currentQuestion.getCategory();
        mCategoryView.setText(categoryView);
        String questionView = currentQuestion.getQuestion();
        mQuestionView.setText(questionView);
    }

    public void trueClick(View v) {
        Log.i(TAG, "trueClick: TRUE button clicked");

        showCorrectAnswer();

        if (Objects.equals(Question.getQuestionByID(this, mAnswerID).getCorrectAnswer(), "True")) {
                    //the the user is correct
                    mCurrentScore++;
                    QuizUtils.setCurrentScore(this, mCurrentScore);
                    Log.i(TAG, "onClick: current score is " + QuizUtils.getCurrentScore(this));
                } else {
                    //the user is incorrect
                    Toast.makeText(this, R.string.wrong, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onClick: current score is " + QuizUtils.getCurrentScore(this));
                }

        if (mCurrentScore > mHighScore) {
            mHighScore = mCurrentScore;
            QuizUtils.setHighScore(this, mHighScore);
        }

        // Remove the answer from the list of all answers, so it doesn't get asked again.
        mRemainingQuestionIDs.remove(Integer.valueOf(mAnswerID));

        // Wait some time in order for user to see correct answer, then go to the next question.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent nextQuestionIntent = new Intent(QuizActivity.this, QuizActivity.class);
                nextQuestionIntent.putExtra(REMAINING_QUESTIONS_KEY, mRemainingQuestionIDs);
                finish();
                startActivity(nextQuestionIntent);
            }
        }, CORRECT_ANSWER_DELAY_MILLIS);

    }


    public void falseClick(View v) {
        Log.i(TAG, "trueClick: FALSE Button Clicked");
        showCorrectAnswer();

        if (Objects.equals(Question.getQuestionByID(this, mAnswerID).getCorrectAnswer(), "True")) {
            //the user is incorrect
          Toast.makeText(this, R.string.wrong, Toast.LENGTH_SHORT).show();
            QuizUtils.setCurrentScore(this, mCurrentScore);
            Log.i(TAG, "onClick: current score is " + QuizUtils.getCurrentScore(this));
        } else {
             //the buser is correct
            mCurrentScore++;
            QuizUtils.setCurrentScore(this, mCurrentScore);
            Log.i(TAG, "onClick: current score is " + QuizUtils.getCurrentScore(this));
        }
        if (mCurrentScore > mHighScore) {
            mHighScore = mCurrentScore;
            QuizUtils.setHighScore(this, mHighScore);
        }

        // Remove the answer from the list of all answers, so it doesn't get asked again.
        mRemainingQuestionIDs.remove(Integer.valueOf(mAnswerID));

        // Wait some time in order for user to see correct answer, then go to the next question.
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent nextQuestionIntent = new Intent(QuizActivity.this, QuizActivity.class);
                nextQuestionIntent.putExtra(REMAINING_QUESTIONS_KEY, mRemainingQuestionIDs);
                finish();
                startActivity(nextQuestionIntent);
            }
        }, CORRECT_ANSWER_DELAY_MILLIS);

    }

    //Disable the buttons and change the background colors to show the correct answer.
    private void showCorrectAnswer() {
        Question currentQuestion = Question.getQuestionByID(this, mAnswerID);
        Log.i(TAG, "onClick: Current Answer is " + currentQuestion.getCorrectAnswer());
        Button buttonTrue = findViewById(R.id.buttonTrue);
        Button buttonFalse = findViewById(R.id.buttonFalse);
        buttonFalse.setEnabled(false);
        buttonTrue.setEnabled(false);

        switch (currentQuestion.getCorrectAnswer()) {
            case "True":
                buttonTrue.setBackgroundResource(android.R.color.holo_green_light);
                buttonFalse.setBackgroundResource(android.R.color.holo_red_light);

                break;
            case "False":
                buttonFalse.setBackgroundResource(android.R.color.holo_green_light);
                buttonTrue.setBackgroundResource(android.R.color.holo_red_light);

                break;
            default:
                throw new RuntimeException("Unknown button ID");
        }
    }
}


