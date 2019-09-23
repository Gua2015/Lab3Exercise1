package com.missouristate.guadagnano.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button[] [] buttons;
    private TicTacToe tttGame;
    private TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tttGame = new TicTacToe();
        buildGUIByCode();
    }

    private void buildGUIByCode() {
        //Get screen width
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int w = size.x/TicTacToe.SIDE;

        //Create layout mngr as GridLayout
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(TicTacToe.SIDE);
        gridLayout.setRowCount(TicTacToe.SIDE + 1);

        //Create buttons and add them to GridLayout
        buttons= new Button[TicTacToe.SIDE][TicTacToe.SIDE];
        ButtonHandler bh = new ButtonHandler();

        for (int row = 0; row < TicTacToe.SIDE; row++){
            for (int col = 0; col < TicTacToe.SIDE; col++){
                buttons[row][col] = new Button(this);
                buttons[row][col].setTextSize((int)(w *.2));
                buttons[row][col].setOnClickListener(bh);
                gridLayout.addView(buttons[row][col], w, w);
            }
        }

        status = new TextView(this);
        GridLayout.Spec rowSpec = GridLayout.spec(TicTacToe.SIDE, 1);
        GridLayout.Spec columnSpec = GridLayout.spec(0, TicTacToe.SIDE);
        GridLayout.LayoutParams lpStatus = new GridLayout.LayoutParams(rowSpec, columnSpec);
        status.setLayoutParams(lpStatus);

        status.setWidth(TicTacToe.SIDE * w);
        status.setHeight(w);
        status.setGravity(Gravity.CENTER);
        status.setBackgroundColor(Color.GREEN);
        status.setTextSize((int)(w * .15));
        status.setText(tttGame.result());

        gridLayout.addView(status);


        //Set GridLayout as View
        setContentView(gridLayout);
    }

    private class ButtonHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.w("MainActivity", "Inside onClick, v=" + v);
            for (int row = 0; row < TicTacToe.SIDE; row++)
                for (int column = 0; column < TicTacToe.SIDE; column++)
                    if (v == buttons[row][column])
                        update(row,column);
        }
    }

    private void update(int row, int column){
        Log.w("MainActivity", "Inside update : " + row + ", " + column);
        int play = tttGame.play(row, column);
        if (play == 1)
            buttons[row][column].setText("X");
        else if (play == 2)
            buttons[row][column].setText("O");
        if (tttGame.isGameOver()){
            status.setBackgroundColor(Color.RED);
            enableButtons(false);
            status.setText(tttGame.result());
            showNewGameDialog();
        }
    }

    private void showNewGameDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("This is fun");
        alert.setMessage("Play again?");
        PlayDialog playAgain = new PlayDialog();
        alert.setPositiveButton("Yes", playAgain);
        alert.setNegativeButton("NO", playAgain);
        alert.show();
    }

    private void enableButtons(boolean b) {
        for (int row = 0; row < TicTacToe.SIDE; row++)
            for (int col = 0; col < TicTacToe.SIDE; col++)
                buttons[row][col].setEnabled(b);
    }

    private class PlayDialog implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int id) {
            if (id == -1){
                tttGame.resetGame();
                enableButtons(true);
                resetButtons();
            }
            else if (id == -2)
                MainActivity.this.finish();
        }
    }

    private void resetButtons() {
        for (int row = 0; row < TicTacToe.SIDE; row++)
            for (int col = 0; col < TicTacToe.SIDE; col++)
                buttons[row][col].setText("");
    }
}
