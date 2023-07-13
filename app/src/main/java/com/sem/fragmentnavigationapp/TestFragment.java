package com.sem.fragmentnavigationapp;


import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;


public class TestFragment extends Fragment
{
    private TextView letterTextView, answerTextView;

    private char[] grassLetters = {'g', 'j', 'p', 'q', 'y'};
    private char[] skyLetters = {'b', 'd', 'f', 'h', 'k', 'l', 't'};
    private char[] rootLetters = {'a', 'c', 'e', 'i', 'm', 'n', 'o', 'r', 's', 'u', 'v', 'w', 'x', 'z'};
    private String answerString = "";
    private int questionCounter = 0;
    private SQLiteDatabase database;

    public TestFragment()
    {
        // Required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        SQLiteOpenHelper dbHelper = new MyDatabaseHelper(getActivity());
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        letterTextView = view.findViewById(R.id.letter_text_view);
        letterTextView.setText(getRandomLetter());

        answerTextView = view.findViewById(R.id.answer_text_view);

        Button skyButton = view.findViewById(R.id.sky_button);
        skyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer("Sky Letter");
            }
        });

        Button grassButton = view.findViewById(R.id.grass_button);
        grassButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                checkAnswer("Grass Letter");
            }
        });

        Button rootButton = view.findViewById(R.id.root_button);
        rootButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer("Root Letter");
            }
        });
    }

    private void checkAnswer(String expectedAnswer) {
        if (answerString.equals(expectedAnswer)) {
            answerTextView.setText("Awesome! Your answer is right");
        } else {
            answerTextView.setText("Incorrect! The answer is " + answerString);
        }

        questionCounter++;

        if (questionCounter == 5) {
            saveResultsToDatabase();
            navigateToResultFragment();
        } else {
            // Wait for 2 seconds and create a new question
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    letterTextView.setText(getRandomLetter());
                    answerTextView.setText("");
                }
            }, 2000); // 2000 milliseconds = 2 seconds
        }
    }

    private void saveResultsToDatabase() {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (int i = 1; i <= questionCounter; i++) {
            String result = getResultForQuestion(i);
            MyDatabaseHelper.insertResult(db, i, result);
        }

        db.close();
    }

    private void navigateToResultFragment() {
        ResultFragment resultFragment = new ResultFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, resultFragment)
                .commit();
    }

    private String getRandomLetter() {
        Random random = new Random();
        int category = random.nextInt(3);
        char letter;
        switch (category) {
            case 0:
                letter = skyLetters[random.nextInt(skyLetters.length)];
                answerString = "Sky Letter";
                break;
            case 1:
                letter = grassLetters[random.nextInt(grassLetters.length)];
                answerString = "Grass Letter";
                break;
            default:
                letter = rootLetters[random.nextInt(rootLetters.length)];
                answerString = "Root Letter";
                break;
        }
        return String.valueOf(letter);
    }

    private String getResultForQuestion(int questionNumber) {
        String result;
        if (questionNumber <= questionCounter) {
            result = "Correct"; // Or retrieve the actual result from your logic
        } else {
            result = "Not answered";
        }
        return result;
    }
}

