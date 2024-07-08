package com.example.tic_tac_toe_android_game;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private enum Player {
        NONE, PLAYER_X, PLAYER_O
    }

    private Player currentPlayer = Player.PLAYER_X;
    private Player[][] board = new Player[3][3];
    private boolean gameActive = true;
    private TextView statusTextView;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = findViewById(R.id.status);
        gridLayout = findViewById(R.id.gridLayout);

        for (int i = 0; i < gridLayout.getChildCount(); i++) {
            final int index = i;
            Button button = (Button) gridLayout.getChildAt(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGridButtonClick(index);
                }
            });
        }

        findViewById(R.id.restartButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPlayerChoiceDialog();
            }
        });

        resetBoard(Player.PLAYER_X);
    }

    private void resetBoard(Player startingPlayer) {
        currentPlayer = startingPlayer;
        gameActive = true;
        statusTextView.setText((startingPlayer == Player.PLAYER_X ? "Player X's" : "Player O's") + " Turn");

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = Player.NONE;
                ((Button) gridLayout.getChildAt(i * 3 + j)).setText("");
            }
        }
    }

    private void onGridButtonClick(int index) {
        if (!gameActive) return;

        int row = index / 3;
        int col = index % 3;

        if (board[row][col] == Player.NONE) {
            board[row][col] = currentPlayer;
            ((Button) gridLayout.getChildAt(index)).setText(currentPlayer == Player.PLAYER_X ? "X" : "O");

            if (checkWin()) {
                gameActive = false;
                statusTextView.setText((currentPlayer == Player.PLAYER_X ? "Player X" : "Player O") + " Wins!");
            } else if (isBoardFull()) {
                gameActive = false;
                statusTextView.setText("Draw!");
            } else {
                currentPlayer = (currentPlayer == Player.PLAYER_X) ? Player.PLAYER_O : Player.PLAYER_X;
                statusTextView.setText((currentPlayer == Player.PLAYER_X ? "Player X's" : "Player O's") + " Turn");

                if (currentPlayer == Player.PLAYER_O) {
                    cpuMove();
                }
            }
        }
    }

    private void cpuMove() {
        // Simple CPU move logic: pick the first available cell
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == Player.NONE) {
                    board[i][j] = Player.PLAYER_O;
                    ((Button) gridLayout.getChildAt(i * 3 + j)).setText("O");
                    if (checkWin()) {
                        gameActive = false;
                        statusTextView.setText("Player O Wins!");
                    } else if (isBoardFull()) {
                        gameActive = false;
                        statusTextView.setText("Draw!");
                    } else {
                        currentPlayer = Player.PLAYER_X;
                        statusTextView.setText("Player X's Turn");
                    }
                    return;
                }
            }
        }
    }

    private boolean checkWin() {
        // Check rows, columns, and diagonals for a win
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == currentPlayer && board[i][1] == currentPlayer && board[i][2] == currentPlayer)
                return true;
            if (board[0][i] == currentPlayer && board[1][i] == currentPlayer && board[2][i] == currentPlayer)
                return true;
        }
        if (board[0][0] == currentPlayer && board[1][1] == currentPlayer && board[2][2] == currentPlayer)
            return true;
        if (board[0][2] == currentPlayer && board[1][1] == currentPlayer && board[2][0] == currentPlayer)
            return true;

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == Player.NONE)
                    return false;
            }
        }
        return true;
    }

    private void showPlayerChoiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Starting Player");

        builder.setItems(new CharSequence[]{"Player X", "Player O"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    resetBoard(Player.PLAYER_X);
                } else {
                    resetBoard(Player.PLAYER_O);
                    cpuMove();
                }
            }
        });

        builder.show();
    }
}
