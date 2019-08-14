package com.example.tictactoe.check_winner_algo

class winner {

    var answerArray: Array<IntArray> = arrayOf(
        intArrayOf(0, 0, 0),
        intArrayOf(0, 0, 0),
        intArrayOf(0, 0, 0)
    )

    fun update(row:Int,col:Int,value:Int) {
        answerArray[row][col] = value
    }


    fun checkValue(row:Int,col:Int):Int{
        return answerArray[row][col]
    }

    fun checkWinner():Boolean {
        if((answerArray[0][0]==1 && answerArray[0][1] == 1 && answerArray[0][2]==1) || (answerArray[0][0]==2 && answerArray[0][1] == 2 && answerArray[0][2]==2))
        {
            return true
        }
        if((answerArray[1][0] ==1 && answerArray[1][1] == 1 && answerArray[1][2]==1) || (answerArray[1][0] ==2 && answerArray[1][1] == 2 && answerArray[1][2]==2))
        {
            return true
        }
        if((answerArray[2][0] ==1 && answerArray[2][1] == 1 && answerArray[2][2]==1) || (answerArray[2][0] ==2 && answerArray[2][1] == 2 && answerArray[2][2]==2))
        {
            return true
        }
        if((answerArray[0][0] ==1 && answerArray[1][0] == 1 && answerArray[2][0]==1) || (answerArray[0][0] ==2 && answerArray[1][0] == 2 && answerArray[2][0]==2))
        {
            return true
        }
        if((answerArray[0][1] ==1 && answerArray[1][1] == 1 && answerArray[2][1]==1) || (answerArray[0][1] ==2 && answerArray[1][1] == 2 && answerArray[2][1]==2))
        {
            return true
        }
        if((answerArray[0][2] ==1 && answerArray[1][2] == 1 && answerArray[2][2]==1) || (answerArray[0][2] ==2 && answerArray[1][2] == 2 && answerArray[2][2]==2))
        {
            return true
        }
        if((answerArray[0][2] ==1 && answerArray[1][2] == 1 && answerArray[2][2]==1) || (answerArray[0][2] ==2 && answerArray[1][2] == 2 && answerArray[2][2]==2))
        {
            return true
        }
        if((answerArray[0][0]==1 && answerArray[1][1] ==1 && answerArray[2][2]==1) || (answerArray[0][0]==2 && answerArray[1][1] ==2 && answerArray[2][2]==2))
        {
            return true
        }
        if((answerArray[0][2]==1 && answerArray[1][1]==1 && answerArray[2][0] ==1) || answerArray[0][2]==2 && answerArray[1][1]==2 && answerArray[2][0] ==2)
        {
            return true
        }
        return false
    }
}