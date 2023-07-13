package com.sem.fragmentnavigationapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.fragment.app.Fragment;

public class ResultFragment extends Fragment
{
    private MyDatabaseHelper databaseHelper;
    private TextView resultTextView;

    public ResultFragment()
            
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        databaseHelper = new MyDatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        resultTextView = view.findViewById(R.id.result_text_view);
        displayResults();
        return view;
    }

    private void displayResults()
    {
        Cursor cursor = databaseHelper.getResult();

        StringBuilder resultBuilder = new StringBuilder();
        while (cursor.moveToNext())
        {
            int questionNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_QUESTION_NUMBER));
            String result = cursor.getString(cursor.getColumnIndexOrThrow(MyDatabaseHelper.COLUMN_RESULT));
            resultBuilder.append("Question ").append(questionNumber).append(": ").append(result).append("\n");
        }

        resultTextView.setText(resultBuilder.toString());
        cursor.close();
    }
}
