package com.example.elephantbook_fieldguide;


import android.content.Intent
import android.app.Activity

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.navigateUp
//import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.example.elephantbook_fieldguide.R
import com.example.elephantbook_fieldguide.R.layout.activity_main
//import com.example.elephantbook_fieldguide.databinding.ActivityMainBinding
//import com.example.elephantbook_fieldguide.dummy
//const val EXTRA_MESSAGE = "com.example.elephantbook_fieldguide.elephant"
public class MainActivity : AppCompatActivity() {

    //declare string to hold the user input for elephant
//val elephant: String

//this is the user input
//EditText elephant_input;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // declare the button and state what its going to do
        // which is open a second activity which will
        // access our single elephant value that the user
        // gave in only ONE of the two text box options

        // depending on how search will be done, may need to make two
        // intents seperate depending on which box the user decides to
        // search in
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {
            fun sendElephant(view: View) {
                val editText = findViewById<EditText>(R.id.search_name)
                val elephant = editText.text.toString()
                //val intent = Intent(this, dummy::class.java).apply {
                  //  putExtra(EXTRA_MESSAGE, elephant)
                //}
               // startActivity(intent)
            }
            // send elephant input to search activity
            // by making an intent that holds this current activity
            // and the next activity
            //elephant = elephant_input.getText().toString()

            //may be able to get elephant input like this ?
            // elephant = findViewByI<EditText>d (R.id.elephant_input)

            //make an intent that starts the activity
            //val intent = Intent(this, dummy_search_activity.class)
            //put extra stores name of data and data itself to be extended
            // to the intent target activity
            //intent.putExtra("elephant",elephant)
            //start that activity
            //startActivity(intent)
        }
    }
}