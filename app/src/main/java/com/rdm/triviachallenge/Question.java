package com.rdm.triviachallenge; ;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.JsonReader;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static java.lang.System.in;


/**
 * Java Object representing a single question. Also includes utility methods for obtaining questions
 * from assets.
 */
class Question implements Parcelable {

    private int mQuestionID;
    private String mCategory;
    private String mDifficulty;
    private String mQuestion;
    private String mCorrectAnswer;
    private String mIncorrectAnswer;

    private Question(int questionID, String category, String difficulty, String question,
                     String correct_answer, String incorrect_answer) {
        mQuestionID = questionID;
        mCategory = category;
        mDifficulty = difficulty;
        mQuestion = question;
        mCorrectAnswer = correct_answer;
        mIncorrectAnswer = incorrect_answer;
    }

    protected Question(Parcel in) {
        mQuestionID = in.readInt();
        mCategory = in.readString();
        mDifficulty = in.readString();
        mQuestion = in.readString();
        mCorrectAnswer = in.readString();
        mIncorrectAnswer = in.readString();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    /**
     * Gets a single question by its ID.
     * @param context The application context.
     * @param questionID The question ID.
     * @return The question object.
     */
    static Question getQuestionByID(Context context, int questionID) {
        JsonReader reader;
        try {
            reader = readJSONFromAsset(context);
            reader.beginArray();
            while (reader.hasNext()) {
                Question currentQuestion = readQuestion(reader);
                if (currentQuestion.getQuestionID() == questionID) {
                    reader.close();
                    return currentQuestion;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Gets an ArrayList of the IDs for all of the Questions from the JSON file.
     * @param context The application context.
     * @return The ArrayList of all question IDs.
     */
    static ArrayList<Integer> getAllQuestionIDs(Context context){
        JsonReader reader;
        ArrayList<Integer> questionIDs = new ArrayList<>();
        try {
            reader = readJSONFromAsset(context);
            reader.beginArray();
            while (reader.hasNext()) {
                questionIDs.add(readQuestion(reader).getQuestionID());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questionIDs;
    }

    /**
     * Method used for obtaining a single question from the JSON file.
     * @param reader The JSON reader object pointing a single question JSON object.
     * @return The question the JsonReader is pointing to.
     */
    private static Question readQuestion(JsonReader reader) throws IOException {
        Integer id = -1;
        String category = null;
        String difficulty = null;
        String question = null;
        String correct_answer = null;
        String incorrect_answer = null;

        try {
            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {

                    case "id":
                        id = reader.nextInt();
                        break;
                    case "category":
                        category = reader.nextString();
                        break;
                    case "difficulty":
                        difficulty = reader.nextString();
                        break;
                    case "question":
                        question = reader.nextString();
                        break;
                    case "correct_answer":
                        correct_answer = reader.nextString();
                        break;
                    case "incorrect_answer":
                        incorrect_answer = reader.nextString();
                        break;
                    default:
                        break;
                }
            }
            reader.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Question(id, category, difficulty, question, correct_answer, incorrect_answer);
    }

    /**
     * Method for creating a JsonReader object that points to the JSON array of questions.
     * @param context The application context.
     * @return The JsonReader object pointing to the JSON array of questions.
     * @throws IOException Exception thrown if the question file can't be found.
     */
    private static JsonReader readJSONFromAsset(Context context) throws IOException {
        AssetManager assetManager = context.getAssets();
        JsonReader reader;
        try {
        reader = new JsonReader(new InputStreamReader(assetManager.open("questionlist.json")));
        } finally {

        }
        return reader;
    }

    // Getters and Setters
    String getCategory() {
        return mCategory;
    }
    void setCategory(String category) {
        mCategory = category;
    }

    int getQuestionID() {
        return mQuestionID;
    }
    void setQuestionID(int questionID) {
        mQuestionID = questionID;
    }

    String getDifficulty() {
        return mDifficulty;
    }
    void setDifficulty(String difficulty) {
        mDifficulty = difficulty;
    }

    String getQuestion() {
        return mQuestion;
    }
    void setQuestion(String question) {
        mQuestion = question;
    }

    String getCorrectAnswer() {
        return mCorrectAnswer;
    }
    void setCorrectAnswer(String correct_answer) {
        mCorrectAnswer = correct_answer;
    }

    String getIncorrectAnswer() {
        return mIncorrectAnswer;
    }
    void setIncorrectAnswer(String incorrect_answer) {
        mIncorrectAnswer = incorrect_answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mQuestionID);
        dest.writeString(mCategory);
        dest.writeString(mDifficulty);
        dest.writeString(mQuestion);
        dest.writeString(mCorrectAnswer);
        dest.writeString(mIncorrectAnswer);
    }
}
