package com.example.tictactoe.firebaseDataClass

import android.media.AsyncPlayer

data class TicTacToeDataClass(
    var buttonPressed : Long,
    var roomID:String,
    var player1 : Boolean,
    var player2 : Boolean,
    var playersJoined : Long
){
    constructor() : this(10,"",false,false,0)
}