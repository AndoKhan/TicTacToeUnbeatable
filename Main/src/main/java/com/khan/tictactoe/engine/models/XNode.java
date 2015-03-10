package com.khan.tictactoe.engine.models;

import android.util.Log;

public class XNode extends Node {
    public XNode(Board currentBoard, int x, int y, int level) {
        super(currentBoard, x, y, ++level);
    }

    private static Value tmpValue;

    @Override
    public Value getValue() {
        if(mValue != null) {
            return mValue;
        }
        Field[][] currentFields = currentBoard.getBoard();
        int minValue = Integer.MAX_VALUE;
        int wins = 0;
        int count = 0;
        Log.v("LEVEL", "X " + mLevel);
        for(int y = 0; y < Board.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Board.BOARD_WIDTH; x++) {
                if(currentFields[y][x].value == Field.VALUE_UNDEFINED) {
                    tmpValue = checkMove(x, y);
                    currentFields[y][x].value = Field.VALUE_UNDEFINED;
                    minValue = Math.min(tmpValue.result, minValue);
                    count++;
                    if (tmpValue.result == 1) wins++;
                }
            }
        }
        mValue = new Value(minValue, (double) wins / count);
        return mValue;
    }

    @Override
    protected Value checkMove(int pX, int pY) {
        Value win = new Value(1, 1);
        Value draw = new Value(0, 1);
        Board moveBoard = currentBoard;
        Field[][] fields = moveBoard.getBoard();
        fields[pY][pX].value = Field.VALUE_X;
        //Check horizontal
        boolean flag = true;
        for(int x = 0; x < Board.BOARD_WIDTH; x++) {
            if(fields[pY][x].value != Field.VALUE_X) {
                flag = false;
                break;
            }
        }
        if(flag) Log.v("X Node:", "win horz");
        if(flag) return win;
        //Check vertical
        flag = true;
        for(int y = 0; y < Board.BOARD_HEIGHT; y++) {
            if(fields[y][pX].value != Field.VALUE_X) {
                flag = false;
                break;
            }
        }
        if(flag) Log.v("X Node:", "win vert");
        if(flag) return win;

        //Check diagonals
        if(pX == pY) {
            flag = true;
            for (int x = 0; x < Board.BOARD_WIDTH; x++) { //just using x as diagonal condition
                if (fields[x][x].value != Field.VALUE_X) {//because x=y;
                    flag = false;
                    break;
                }
            }
            if (flag) Log.v("X Node:", "win diag 1");
            if (flag) return win;
        }

        //check other diagonal
        flag = true;
        //TODO
//        int tmpX = pX;
//        int tmpY = pY;
//        for(int x = 0; x < Board.BOARD_WIDTH; x++) { //just using x as diagonal condition
//            tmpX = Math.abs(--tmpX);
//            tmpY = Math.abs(--tmpY);
//            if(fields[tmpY][tmpX].value != Field.VALUE_X) {
//                flag = false;
//                break;
//            }
//        }
        flag = (fields[0][2].value == Field.VALUE_X && fields[1][1].value == Field.VALUE_X && fields[2][0].value == Field.VALUE_X);
        if(flag) Log.v("X Node:", "win diag");
        if(flag) return win;

        //Check for all fields filled (draw if there are no win before)
        flag = true;
        for(int y = 0; y < Board.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Board.BOARD_WIDTH; x++) {
                if(fields[y][x].value == Field.VALUE_UNDEFINED) {
                    flag = false;
                    break;
                }
            }
        }

        if(flag) Log.v("X Node:", "draw");
        if(flag) return draw;

        ONode oNode = new ONode(new Board(fields), pX, pY, mLevel);
        children.add(oNode);

        Log.v("X Node:", "continue " + pX + "," + pY);

        return oNode.getValue();
    }
}
