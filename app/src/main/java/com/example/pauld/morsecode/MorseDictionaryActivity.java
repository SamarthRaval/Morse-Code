package com.example.pauld.morsecode;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * MorseDictionaryActivity.java
 *      The purpose of this class is to populate the GridView presented to the user with all alphanumerical characters available
 *       and their Morse representation.
 *
 *      This is achieved by simply populating an ArrayList of Strings with the data in MorseCodeStandards
 *       and using the GridView setArrayAdapter function to populate the view.
 *
 *      @author George Faraj B00638341
 */


import java.util.ArrayList;

public class MorseDictionaryActivity extends AppCompatActivity {

    private GridView letterToMorseList;
    private ArrayList<String> letterToMorseContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_morse_dictionary);

        letterToMorseList = findViewById(R.id.letterToMorseList);

        letterToMorseContent = new ArrayList<String>();
        int indexMorseCode;
        for (char index = 'A'; index <= 'Z';index++){
            indexMorseCode = MorseCodeStandards.GetRowNotSpecial(index);
            letterToMorseContent.add(index + " " + MorseCodeStandards.InternationalStandard[indexMorseCode][1]);
        }
        for(char index = '0'; index <= '9'; index++){
            indexMorseCode = MorseCodeStandards.GetRowNotSpecial(index);
            letterToMorseContent.add(index +" "+ MorseCodeStandards.InternationalStandard[indexMorseCode][1]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_list_item_1, letterToMorseContent) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView t = (TextView) view.findViewById(android.R.id.text1);
                Typeface typeface = Typeface.create(Typeface.MONOSPACE,Typeface.NORMAL);
                t.setTypeface(typeface);
                t.setPadding(5,0,0,0);
                t.setTextSize(getResources().getDimension(R.dimen.MorseDictionaryItemSize));
                t.setTextColor(Color.BLACK);
                return view;
            }
        };

        letterToMorseList.setAdapter(adapter);
    }
}
