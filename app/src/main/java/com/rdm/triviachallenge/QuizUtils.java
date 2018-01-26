package com.rdm.triviachallenge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Rebecca on 1/24/2018.
 */

public class QuizUtils {

    private static final String CURRENT_SCORE_KEY = "current_score";
    private static final String GAME_FINISHED = "game_finished";
    private static final String HIGH_SCORE_KEY = "high_score";
    private static final int NUM_ANSWERS = 4;

    /**
     * Generates an ArrayList of Integers that contains IDs to NUM_ANSWERS questions. These
     * constitute the possible answers to the question.
     * @param remainingQuestionIDs The ArrayList of Integers which contains the IDs of all
     *                           questionss that haven't been used yet.
     * @return The ArrayList of possible answers.
     */
    static ArrayList<Integer> generateQuestion(ArrayList<Integer> remainingQuestionIDs){

        // Shuffle the remaining question ID's.
        Collections.shuffle(remainingQuestionIDs);

        ArrayList<Integer> answers = new ArrayList<>();

        for(int i = 0; i < NUM_ANSWERS; i++){
            if(i < remainingQuestionIDs.size()) {
                answers.add(remainingQuestionIDs.get(i));
            }
        }

        return answers;
    }

    /**
     * Helper method for getting the user's current score.
     * @param context The application context.
     * @return The user's current score.
     */
    static int getCurrentScore(Context context){
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return mPreferences.getInt(CURRENT_SCORE_KEY, 0);
    }

    /**
     * Setter method for setting the user's current score.
     * @param context The application context.
     * @param currentScore The user's current score.
     */
    static void setCurrentScore(Context context, int currentScore){
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(CURRENT_SCORE_KEY, currentScore);
        editor.apply();
    }

    /**
     * Helper method for getting the user's high score.
     * @param context The application context.
     * @return The user's high score.
     */
    static int getHighScore(Context context){
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return mPreferences.getInt(HIGH_SCORE_KEY, 0);
    }

    /**
     * Setter method for setting the user's high score.
     * @param context The application context.
     * @param highScore The user's high score.
     */
    static void setHighScore(Context context, int highScore){
        SharedPreferences mPreferences = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(HIGH_SCORE_KEY, highScore);
        editor.apply();
    }

    /**
     * Picks one of the possible answers to be the correct one at random.
     * @param answers The possible answers to the question.
     * @return The correct answer.
     */
    static int getCorrectAnswerID(ArrayList<Integer> answers){
        Random r = new Random();
        int answerIndex = r.nextInt(answers.size());
        return answers.get(answerIndex);
    }

    /**
     * Checks that the user's selected answer is the correct one.
     * @param correctAnswer The correct answer.
     * @param userAnswer The user's answer
     * @return true if the user is correct, false otherwise.
     */
    static boolean userCorrect(int correctAnswer, int userAnswer){
        return userAnswer == correctAnswer;
    }

    /**
     * Helper method for ending the game.
     * @param context The application method.
     */
    static void endGame(Context context){
        Intent endGame = new Intent(context, MainActivity.class);
        endGame.putExtra(GAME_FINISHED, true);
        context.startActivity(endGame);
    }
}