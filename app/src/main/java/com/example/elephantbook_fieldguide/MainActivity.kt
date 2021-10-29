package com.example.elephantbook_fieldguide

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.osmdroid.config.Configuration

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.elephantpage)
        val simpleImageView = findViewById<View>(R.id.elephantPicture) as ImageView

        // https://github.com/osmdroid/osmdroid/wiki/Important-notes-on-using-osmdroid-in-your-app
        //5.6 and newer
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID

        // TESTING CODE
        val dbW = DatabaseWrapper(applicationContext)
        dbW.updateDatabase {
            simpleImageView.setImageDrawable(dbW.getElephantPfp(5))
            println(dbW.getElephantsByNamePrefix("F"))
            println(dbW.getLatestLocation(5))
        }
        // TESTING CODE

    }
}