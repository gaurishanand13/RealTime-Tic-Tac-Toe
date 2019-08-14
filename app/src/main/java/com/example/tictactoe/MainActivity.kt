package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tictactoe.local_game_activity.Game_Activity
import com.example.tictactoe.onlineGame.online_game_room
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playLocalButton.setOnClickListener {
            startActivity(Intent(this, Game_Activity::class.java))
        }

        playOnlineButton.setOnClickListener {
            startActivity(Intent(this,online_game_room::class.java))
        }


    }
}
