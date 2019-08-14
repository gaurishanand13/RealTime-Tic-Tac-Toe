package com.example.tictactoe.local_game_activity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.tictactoe.R
import com.example.tictactoe.check_winner_algo.winner
import kotlinx.android.synthetic.main.activity_game_.*

class Game_Activity : AppCompatActivity(),View.OnClickListener{

    /**
     * Array of all the buttons
     */
    val arr by lazy {
        arrayOf(button1,button2,button3,button4,button5,button6,button7,button8,button9)
    }

    var myObject = winner()
    var count = 0

    /**
     * If player 1 is true, then player 1 is active, otherwise player 2 is active
     * Player1 will be by default the cross and player 2 will be by default the O
     */
    var player1 = true

    override fun onClick(p0: View?) {

        count++
        val tag = p0?.tag.toString().toInt()
        val row : Int = tag/3
        val col : Int = tag%3
        Log.i("tag","row = ${row} col = ${col}")


        /**
         * If in the 2D Array the value of that is 0, then only we will insert,
         * otherwise it is already filled
         */
        if(myObject.checkValue(row,col) == 0)
        {
            if(player1)
            {
                /**
                 * Since player 1 is true i.e chance is of player 1. Therefore we should insert the cross
                 */
                myObject.update(row,col,1)
                player1 = false
                p0?.setBackgroundResource(R.drawable.x)

            }
            else
            {
                myObject.update(row,col,2)
                player1 = true
                p0?.setBackgroundResource(R.drawable.circle)
            }


            /**
             * Now check for the winner condition
             */
            if(count>=5)
            {
                if(myObject.checkWinner() == true)
                {
                    if(player1 == false)
                    {
                        //winner is player 1
                        AlertDialog.Builder(this)
                            .setTitle("Congratulations to PLAYER 1 for winning")
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
                            .setIcon(android.R.drawable.dialog_frame)
                            .setTitle("Congratulations to PLAYER 2 for winning")
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
                    .setIcon(android.R.drawable.dialog_frame)
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
    }

    private fun playAgainSetUp() {
        /**
         * Set up for play again
         */
        myObject = winner()
        count = 0
        player1 = true
        for (x in arr){
            x.setBackgroundColor(Color.WHITE)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_)


        /**
         * First set the onClick on each button
         */
        for (x in arr)
        {
            x.setOnClickListener(this)
        }
    }
}




