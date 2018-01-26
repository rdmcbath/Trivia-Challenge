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

/**
 * Created by Rebecca on 1/24/2018.
 */

public class QuizActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = QuizActivity.class.getSimpleName();
    private static final int CORRECT_ANSWER_DELAY_MILLIS = 1000;
    private static final String REMAINING_QUESTIONS_KEY = "remaining_questions";
    private int[] mButtonIDs = {R.id.buttonTrue, R.id.buttonFalse};
    private int mCurrentScore;
    private int mHighScore;
    private Button[] mButtons;
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

        // Initialize the Category & Question view.
        View includedLayout = findViewById(R.id.includedLayout ); // rootView id from included layout
        mCategoryView = includedLayout.findViewById( R.id.categoryView ); //id of view inside included layout
        mQuestionView = includedLayout.findViewById( R.id.questionView );

        // Initialize the buttons.
//        Button trueButton = findViewById(R.id.buttonTrue);
//        Button falseButton = findViewById(R.id.buttonFalse);
        mButtons = initializeButtons(mQuestionIDs);

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

        // If there is only one answer left, end the game.
        if (mQuestionIDs.size() < 2) {
            QuizUtils.endGame(this);
            finish();
        }

        Question answerQuestion = Question.getQuestionByID(this, mAnswerID);

        if (answerQuestion == null) {
            Toast.makeText(this, getString(R.string.question_not_found_error),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String categoryView = answerQuestion.getCategory();
        mCategoryView.setText(categoryView);
        String questionView = answerQuestion.getQuestion();
        mQuestionView.setText(questionView);
    }

    /**
     * Initializes the button to the correct views, and sets the text to the composers names,
     * and sets the OnClick listener to the buttons.
     *
     * @param answerQuestionIDs The IDs of the possible answers to the question.
     * @return The Array of initialized buttons.
     */
    private Button[] initializeButtons(ArrayList<Integer> answerQuestionIDs) {
        Log.i(TAG, "QUIZACTIVITY: Initialize Buttons Method Called");
        Button[] buttons = new Button[mButtonIDs.length];
        for (int i = 0; i < answerQuestionIDs.size(); i++) {
            Button currentButton = findViewById(mButtonIDs[i]);
            Question currentQuestion = Question.getQuestionByID(this, answerQuestionIDs.get(i));
            buttons[i] = currentButton;
            currentButton.setOnClickListener(this);
            if (currentQuestion != null) {
                currentButton.setText(currentQuestion.getCorrectAnswer());
            }
        }

        return buttons;
    }

    /**
     * The OnClick method for all of the answer buttons. The method uses the index of the button
     * in button array to to get the ID of the sample from the array of question IDs. It also
     * toggles the UI to show the correct answer.
     *
     * @param v The button that was clicked.
     */
    @Override
    public void onClick(View v) {
        Log.i(TAG, "QUIZACTIVITY: ONCLICK method called and button pressed");

        // Show the correct answer.
        showCorrectAnswer();
        // Get the button that was pressed.
        Button pressedButton = (Button) v;

        // Get the index of the pressed button
        int userAnswerIndex = -1;
        for (int i = 0; i < mButtons.length; i++) {
            if (pressedButton.getId() == mButtonIDs[i]) {
                userAnswerIndex = i;
            }
        }

        // Get the ID of the answer that the user selected.
        int userAnswerSampleID = mQuestionIDs.get(userAnswerIndex);

        // If the user is correct, increase their score.
        if (QuizUtils.userCorrect(mAnswerID, userAnswerSampleID)) {
            mCurrentScore++;
            QuizUtils.setCurrentScore(this, mCurrentScore);
            if (mCurrentScore > mHighScore) {
                mHighScore = mCurrentScore;
                QuizUtils.setHighScore(this, mHighScore);
            }
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

    /**
     * Disables the buttons and changes the background colors and player art to
     * show the correct answer.
     */
    private void showCorrectAnswer() {
        for (int i = 0; i < mQuestionIDs.size(); i++) {
            int buttonQuestionID = mQuestionIDs.get(i);

            mButtons[i].setEnabled(false);

            if (buttonQuestionID == mAnswerID) {
                mButtons[i].getBackground().setColorFilter(ContextCompat.getColor
                                (this, android.R.color.holo_green_light),
                        PorterDuff.Mode.MULTIPLY);
                mButtons[i].setTextColor(Color.WHITE);
            } else {
                mButtons[i].getBackground().setColorFilter(ContextCompat.getColor
                                (this, android.R.color.holo_red_light),
                        PorterDuff.Mode.MULTIPLY);
                mButtons[i].setTextColor(Color.WHITE);
            }
        }
    }
}