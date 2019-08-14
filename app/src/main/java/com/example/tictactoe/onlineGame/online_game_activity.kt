package com.example.tictactoe.onlineGame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import com.example.tictactoe.R
import com.example.tictactoe.check_winner_algo.winner
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_online_game_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class online_game_activity : AppCompatActivity() ,CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    val arr by lazy {
        arrayOf(onlineButton1,onlineButton2,onlineButton3,onlineButton4,onlineButton5,onlineButton6,onlineButton7,onlineButton8,onlineButton9)
    }

    var roomID = ""
    val myFirebaseDatabase  by lazy {
        FirebaseDatabase.getInstance().reference.child("tictactoe").child("$roomID")
    }


    /**
     * here if the player 1 is playing , then player 1 will always be true
     * but if the player 2 is playing then the player 2 will always be true
     */
    var player1 = false
    var player2 = false

    /**
     * To manage the number of players in the room
     */
    var count_of_joined = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_game_activity)


        /**
         * First fetching the roomID of the room
         */
        roomID = intent.getStringExtra("roomID")
        onlineGameRoomIDTextView.text = roomID



        /**
         * Now first check , which player has joined, is it player 1 or player 2. If the player
         * creates the room then it will be player 1(i.e when no. of players in the room is 1)
         * and if the player joines a room , then it will be player 2
         */

        myFirebaseDatabase.child("playersJoined").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                val playersJoined = p0.getValue(Long::class.java)?.toInt()

                if(playersJoined == 1 ){

                    player1 = true
                    count_of_joined++

                } else if(playersJoined == 2){

                   if(count_of_joined == 1){
                       player2 = true
                   }
                    if(count_of_joined==2 || count_of_joined==1){
                        /**
                         * It means that we should now begin the game as both the players have joined
                         */
                        onlineGameProgressBar.visibility = View.GONE
                        onlineGameRoomIDTextView.visibility = View.GONE
                        onlineMessageTextView.visibility = View.GONE
                        onlineGameFrameLayout.visibility = View.VISIBLE

                        runOnUiThread {
                            Toast.makeText(this@online_game_activity,"Begin the game",Toast.LENGTH_SHORT).show()
                        }
                    }
                    count_of_joined++

                }
            }

        })








        /**
         * Now we will set to change the values in the buttonPressed. Therefore the other use on whose screen, the button pressed is not executed , on that screen too. it should work
         */
        myFirebaseDatabase.child("buttonPressed").addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {

                count++
                val buttonTag = p0.getValue(Long::class.java)
                val row : Int = (buttonTag!!.toInt())/3
                val col : Int = (buttonTag!!.toInt())%3

                if(buttonTag<9){
                    myFirebaseDatabase?.child("player1").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onCancelled(p0: DatabaseError) {}

                        override fun onDataChange(p0: DataSnapshot) {

                            val chance = p0.getValue(Boolean::class.java)


                            /**
                             * Now update the corresponding UI
                             */
                            if(chance == true){

                                myObjectClass.update(row,col,1)
                                runOnUiThread {
                                    arr[buttonTag!!.toInt()].setBackgroundResource(R.drawable.x)
                                }
                            }else{

                                myObjectClass.update(row,col,2)
                                runOnUiThread {
                                    arr[buttonTag!!.toInt()].setBackgroundResource(R.drawable.circle)
                                }
                            }
                            checkForWinnerOrDraw()
                        }
                    })
                }

            }
        })
    }


    var myObjectClass = winner()
    var count = 0
    fun clickFunction(view : View?){

        val tag = view?.tag.toString().toInt()
        val row : Int = tag/3
        val col : Int = tag%3

        /**
         * If in the 2D Array the value of that is 0, then only we will insert,
         * otherwise it is already filled
         */
        if(myObjectClass.checkValue(row,col) == 0)
        {
            val playerChance = myFirebaseDatabase.child("player1").addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    val chance = p0.getValue(Boolean::class.java)
                    if(chance == true){
                        /**
                         * It means the chance is of player 1. Therefore first make sure that the player 1 is only here. Then only update th UI
                         */
                        if(player1 == true){
                            /**
                             * First make sure that for next time , the value of player 1 is opposite. so that next player can make its move
                             */
                            myFirebaseDatabase.child("player1").setValue(false)
                            myFirebaseDatabase.child("buttonPressed").setValue(tag)
                        }

                    }else{
                        /**
                         * Chance is of player 2
                         */
                        if(player2 == true){
                            myFirebaseDatabase.child("player1").setValue(true)
                            myFirebaseDatabase.child("buttonPressed").setValue(tag)
                        }
                    }
                }

            })
        }
    }











    fun checkForWinnerOrDraw(){

        /**
         * Now check for the winner condition
         */
        if(count>=5)
        {
            if(myObjectClass.checkWinner() == true)
            {

                /**
                 * First find whose active chance is not
                 */
                if(count%2==1)
                {
                    //winner is player 1
                    AlertDialog.Builder(this)
                        .setTitle("Congratulations to X for winning")
                        .setMessage("Do you want to play it again")
                        .setPositiveButton("YES"){ dialogInterface, i ->
                            playAgainSetUp()
                        }
                        .setNegativeButton("NO"){dialogInterface, i ->
                            finish()
                        }
                        .show()
                }
                else
                {
                    //winner is player too
                    AlertDialog.Builder(this)
                        .setTitle("Congratulations to O for winning")
                        .setMessage("Do you want to play it again")
                        .setPositiveButton("YES"){ dialogInterface, i ->
                            playAgainSetUp()
                        }
                        .setNegativeButton("NO"){dialogInterface, i ->
                            finish();
                        }
                        .show()
                }
            }
        }
        if(count == 9)
        {
            //match is drawn if it comes from above.
            AlertDialog.Builder(this)
                .setTitle("OOPS! MATCH IS DRAWN")
                .setMessage("Do you want to play it again")
                .setPositiveButton("YES"){ dialogInterface, i ->
                    playAgainSetUp()
                }
                .setNegativeButton("NO"){dialogInterface, i ->
                    finish();
                }
                .show()
        }
    }


    fun playAgainSetUp(){
        finish()
    }
}
