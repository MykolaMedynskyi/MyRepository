package com.example.kik

class Board {

    private var board: Array<IntArray>
    private var turn: Int
    private var count: Int
    var result: Int

    init {
        board = Array(5) { IntArray(5) }
        for (i in 0..4) {
            for (j in 0..4) {
                board[i][j] = 0
            }
        }
        turn = 1
        count = 0
        result = 0
    }

    fun turnToString(): String {
        return if (turn == 1)
            "X"
        else
            "O"
    }

    fun put(xy: String) {
        val x = Integer.parseInt(xy.substring(0, 1))
        val y = Integer.parseInt(xy.substring(1, 2))
        board[x][y] = turn
        if (turn == -1) {
            turn = 1
        } else
            turn = -1
        count++
    }

    fun restart() {
        for (i in 0..4) {
            for (j in 0..4) {
                board[i][j] = 0
            }
        }
        turn = 1
        count = 0
        result = 0
    }

    fun check4Win() {
        if (count < 9) return
        var xWin: Boolean = false
        var oWin: Boolean = false
        var c1: Int = 0
        var c2: Int = 0
        for (i in 0..4) {
            for (j in 0..4) {
                c1+=board[i][j]
                c2+=board[j][i]
            }
            if (c1 == 5 || c2 == 5) {
                xWin = true
            } else if (c1 == -5 || c2 == -5) {
                oWin = true
            }
            c1 = 0
            c2 = 0
        }

        c1 = board[0][0] + board[1][1] + board[2][2] + board[3][3] + board[4][4]
        c2 = board[0][4] + board[1][3] + board[2][2] + board[3][1] + board[4][0]
        if (c1 == 5 || c2 == 5) {
            xWin = true
        } else if (c1 == -5 || c2 == -5) {
            oWin = true
        }

        if (xWin == true) result = 1
        if (oWin == true) result = -1
        if (result == 0 && count == 25) result = 2

    }

}