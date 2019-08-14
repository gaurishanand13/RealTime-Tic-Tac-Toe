package com.example.tictactoe.onlineGame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.tictactoe.R
import com.example.tictactoe.firebaseDataClass.TicTacToeDataClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_online_game_room.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class online_game_room : AppCompatActivity(),CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    val myFirebaseDatabase = FirebaseDatabase.getInstance().reference.child("tictactoe")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_game_room)

        enterRoombtn.setOnClickListener {
            if(roomEditText.text.equals("")){
                Toast.makeText(this,"Enter Room ID",Toast.LENGTH_SHORT).show()
            }else{
                /**
                 * First check if the entered room id is correct or not
                 */
                val roomId = roomEditText.text.toString()
                checkForRoomValidation(roomId)
            }
        }


        createRoomBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            var newRoomId = myFirebaseDatabase.push().key?.subSequence(0,10).toString()
            newRoomId.replace('-','5')
            val data = TicTacToeDataClass(10,newRoomId,true,false,1)
            myFirebaseDatabase.child("${newRoomId}/").setValue(data)
            val intent = Intent(this@online_game_room,online_game_activity::class.java)
            intent.putExtra("roomID",newRoomId)
            startActivity(intent)
            progressBar.visibility = View.GONE
        }

    }


     fun checkForRoomValidation(roomId : String)
     {
         progressBar.visibility = View.VISIBLE

        myFirebaseDatabase.child("${roomId}").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                progressBar.visibility = View.GONE
            }

            override fun onDataChange(p0: DataSnapshot) {

                if(p0.exists()){

                    /**
                     * Now check that the number of players shold not be more than 2
                     */
                    val data = p0.getValue(TicTacToeDataClass::class.java)
                    if(data?.playersJoined == (2.toLong())){
                        runOnUiThread{
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@online_game_room,"Room is full",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        myFirebaseDatabase.child("${roomId}").child("playersJoined").setValue(2)
                        progressBar.visibility = View.GONE
                        val intent = Intent(this@online_game_room,online_game_activity::class.java)
                        intent.putExtra("roomID",roomId)
                        startActivity(intent)
                    }

                }else{
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@online_game_room,"Room does not exist",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}
