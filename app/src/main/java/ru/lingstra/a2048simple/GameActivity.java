package ru.lingstra.a2048simple;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends Activity implements View.OnTouchListener {

    private ConstraintLayout mScreen;
    private int[][] mOLDNumbersINT = new int[4][4];
    private int[][] mNumbersINT = new int[4][4];
    private int Score = 0;
    private int OldScore = 0;
    private int mRecord;
    private TextView mRecordView;
    private TextView mScoreView;
    private TextView[][] mNumbersTextView = new TextView[4][4];
    private final Random random = new Random();
    private float StartX, StartY;
    boolean mCheckUP = true;
    public static final String APP_PREFERENCES = "mySettings";
    private SharedPreferences mSettings;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRandomNumbers();
        setRandomNumbers();
        bindStringArray();
        setNumbersToTextView();
        mScreen.setOnTouchListener(this);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt("APP_PREFERENCES_NUMBERS00", mNumbersINT[0][0]);
        editor.putInt("APP_PREFERENCES_NUMBERS01", mNumbersINT[0][1]);
        editor.putInt("APP_PREFERENCES_NUMBERS02", mNumbersINT[0][2]);
        editor.putInt("APP_PREFERENCES_NUMBERS03", mNumbersINT[0][3]);
        editor.putInt("APP_PREFERENCES_NUMBERS10", mNumbersINT[1][0]);
        editor.putInt("APP_PREFERENCES_NUMBERS11", mNumbersINT[1][1]);
        editor.putInt("APP_PREFERENCES_NUMBERS12", mNumbersINT[1][2]);
        editor.putInt("APP_PREFERENCES_NUMBERS13", mNumbersINT[1][3]);
        editor.putInt("APP_PREFERENCES_NUMBERS20", mNumbersINT[2][0]);
        editor.putInt("APP_PREFERENCES_NUMBERS21", mNumbersINT[2][1]);
        editor.putInt("APP_PREFERENCES_NUMBERS22", mNumbersINT[2][2]);
        editor.putInt("APP_PREFERENCES_NUMBERS23", mNumbersINT[2][3]);
        editor.putInt("APP_PREFERENCES_NUMBERS30", mNumbersINT[3][0]);
        editor.putInt("APP_PREFERENCES_NUMBERS31", mNumbersINT[3][1]);
        editor.putInt("APP_PREFERENCES_NUMBERS32", mNumbersINT[3][2]);
        editor.putInt("APP_PREFERENCES_NUMBERS33", mNumbersINT[3][3]);
        editor.putInt("myRecord", mRecord);
        editor.putInt("myScore", Score);
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //getting the value of record
        if (mSettings.contains("myRecord")) {
            // Получаем число из настроек
            mRecord = mSettings.getInt("myRecord", 0);
        }
        //getting the value of Score
        if (mSettings.contains("myScore")){
            Score = mSettings.getInt("myScore", 0);
        }
        //getting the value of numbers array
        if (mSettings.contains("APP_PREFERENCES_NUMBERS00")){
            mNumbersINT[0][0] = mSettings.getInt("APP_PREFERENCES_NUMBERS00", 0);
            mNumbersINT[0][1] = mSettings.getInt("APP_PREFERENCES_NUMBERS01", 0);
            mNumbersINT[0][2] = mSettings.getInt("APP_PREFERENCES_NUMBERS02", 0);
            mNumbersINT[0][3] = mSettings.getInt("APP_PREFERENCES_NUMBERS03", 0);
            mNumbersINT[1][0] = mSettings.getInt("APP_PREFERENCES_NUMBERS10", 0);
            mNumbersINT[1][1] = mSettings.getInt("APP_PREFERENCES_NUMBERS11", 0);
            mNumbersINT[1][2] = mSettings.getInt("APP_PREFERENCES_NUMBERS12", 0);
            mNumbersINT[1][3] = mSettings.getInt("APP_PREFERENCES_NUMBERS13", 0);
            mNumbersINT[2][0] = mSettings.getInt("APP_PREFERENCES_NUMBERS20", 0);
            mNumbersINT[2][1] = mSettings.getInt("APP_PREFERENCES_NUMBERS21", 0);
            mNumbersINT[2][2] = mSettings.getInt("APP_PREFERENCES_NUMBERS22", 0);
            mNumbersINT[2][3] = mSettings.getInt("APP_PREFERENCES_NUMBERS23", 0);
            mNumbersINT[3][0] = mSettings.getInt("APP_PREFERENCES_NUMBERS30", 0);
            mNumbersINT[3][1] = mSettings.getInt("APP_PREFERENCES_NUMBERS31", 0);
            mNumbersINT[3][2] = mSettings.getInt("APP_PREFERENCES_NUMBERS32", 0);
            mNumbersINT[3][3] = mSettings.getInt("APP_PREFERENCES_NUMBERS33", 0);
        }
        // Выводим на экран данные из настроек
        setNumbersToTextView();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent e) {
        float THRESHOLD = 130, deltaX, deltaY;
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                StartX = e.getRawX();
                StartY = e.getRawY();
                mCheckUP = true; //checking if the touch ended
                break;
            case MotionEvent.ACTION_MOVE:
                deltaX = Math.abs(e.getRawX() - StartX);
                deltaY = Math.abs(e.getRawY() - StartY);
                if ((deltaX > THRESHOLD) || (deltaY > THRESHOLD)) {
                    switch (whereSlideIS(e.getRawX() - StartX, e.getRawY() - StartY)) {
                        case 1:
                            if (mCheckUP) {
                                saveOLDNumbers();
                                moveTopComplicated(true);
                                outputToScreen();
                                mCheckUP = false;
                                return true;
                            } else return false;
                        case 2:
                            if (mCheckUP) {
                                saveOLDNumbers();
                                moveBottomComplicated(true);
                                outputToScreen();
                                mCheckUP = false;
                                return true;
                            } else return false;
                        case 3:
                            if (mCheckUP) {
                                saveOLDNumbers();
                                moveRightComplicated(true);
                                outputToScreen();
                                mCheckUP = false;
                                return true;
                            } else return false;
                        case 4:
                            if (mCheckUP) {
                                saveOLDNumbers();
                                moveLeftComplicated(true);
                                outputToScreen();
                                mCheckUP = false;
                                return true;
                            } else return false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mCheckUP = true;
                break;
        }
        return true;
    }

    private boolean checkIfChangesCanBeMade() {
        int[][] mNextNumbersINT = new int[4][4];
        boolean CheckIfChangesWereMade = false;

        for (int i = 0; i < 4; i++)
            System.arraycopy(mNumbersINT[i], 0, mNextNumbersINT[i], 0, 4);
        moveTopComplicated(false);
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (mNumbersINT[i][j] != mNextNumbersINT[i][j])
                    CheckIfChangesWereMade = true;

        for (int i = 0; i < 4; i++)
            System.arraycopy(mNextNumbersINT[i], 0, mNumbersINT[i], 0, 4);
        if (!CheckIfChangesWereMade) {
            moveBottomComplicated(false);
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    if (mNumbersINT[i][j] != mNextNumbersINT[i][j])
                        CheckIfChangesWereMade = true;
        }

        for (int i = 0; i < 4; i++)
            System.arraycopy(mNextNumbersINT[i], 0, mNumbersINT[i], 0, 4);
        if (!CheckIfChangesWereMade) {
            moveRightComplicated(false);
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    if (mNumbersINT[i][j] != mNextNumbersINT[i][j])
                        CheckIfChangesWereMade = true;
        }

        for (int i = 0; i < 4; i++)
            System.arraycopy(mNextNumbersINT[i], 0, mNumbersINT[i], 0, 4);
        if (!CheckIfChangesWereMade) {
            moveLeftComplicated(false);
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 4; j++)
                    if (mNumbersINT[i][j] != mNextNumbersINT[i][j])
                        CheckIfChangesWereMade = true;
        }
        for (int i = 0; i < 4; i++)
            System.arraycopy(mNextNumbersINT[i], 0, mNumbersINT[i], 0, 4);
        return CheckIfChangesWereMade;
    }

    private void outputToScreen() {
        boolean CheckIfChangesWereMade = false;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (mNumbersINT[i][j] != mOLDNumbersINT[i][j])
                    CheckIfChangesWereMade = true;
        if (CheckIfChangesWereMade)
            setRandomNumbers();
        setNumbersToTextView();
    }

    private int whereSlideIS(float DeltaX, float DeltaY) {
        if (Math.abs(DeltaX) > Math.abs(DeltaY)) {
            if (DeltaX == Math.abs(DeltaX)) {
                return 3;
            } else {
                return 4;
            }
        } else {
            if (DeltaY == Math.abs(DeltaY)) {
                return 2;
            } else {
                return 1;
            }
        }
    }

    private void bindStringArray() {
        mNumbersTextView[0][0] = findViewById(R.id.number10);
        mNumbersTextView[0][1] = findViewById(R.id.number11);
        mNumbersTextView[0][2] = findViewById(R.id.number12);
        mNumbersTextView[0][3] = findViewById(R.id.number13);

        mNumbersTextView[1][0] = findViewById(R.id.number20);
        mNumbersTextView[1][1] = findViewById(R.id.number21);
        mNumbersTextView[1][2] = findViewById(R.id.number22);
        mNumbersTextView[1][3] = findViewById(R.id.number23);

        mNumbersTextView[2][0] = findViewById(R.id.number30);
        mNumbersTextView[2][1] = findViewById(R.id.number31);
        mNumbersTextView[2][2] = findViewById(R.id.number32);
        mNumbersTextView[2][3] = findViewById(R.id.number33);

        mNumbersTextView[3][0] = findViewById(R.id.number40);
        mNumbersTextView[3][1] = findViewById(R.id.number41);
        mNumbersTextView[3][2] = findViewById(R.id.number42);
        mNumbersTextView[3][3] = findViewById(R.id.number43);

        mScreen = findViewById(R.id.TouchActivity);
        mScoreView = findViewById(R.id.textViewCurrentScore);
        mRecordView = findViewById(R.id.textViewRecord);
    }

    private void setNumbersToTextView() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (mNumbersINT[i][j] == 0) {
                    mNumbersTextView[i][j].setText(R.string.EmptyNumber);
                    mNumbersTextView[i][j].setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                    mNumbersTextView[i][j].setTextColor(getResources().getColor(R.color.colorBlack));
                    mNumbersTextView[i][j].setTextSize(getResources().getDimension(R.dimen.app_2048_numbers_size));
                } else {
                    mNumbersTextView[i][j].setText(String.valueOf(mNumbersINT[i][j]));
                    if (mNumbersINT[i][j] == 2) {
                        mNumbersTextView[i][j].setBackgroundColor(getResources().getColor(R.color.colorLightGrey));
                        mNumbersTextView[i][j].setTextColor(getResources().getColor(R.color.colorBlack));
                        mNumbersTextView[i][j].setTextSize(getResources().getDimension(R.dimen.app_2048_numbers_size));
                    } else if (mNumbersINT[i][j] == 4) {
                        mNumbersTextView[i][j].setBackgroundColor(getResources().getColor(R.color.colorPearl));
                        mNumbersTextView[i][j].setTextColor(getResources().getColor(R.color.colorBlack));
                        mNumbersTextView[i][j].setTextSize(getResources().getDimension(R.dimen.app_2048_numbers_size));
                    } else if (mNumbersINT[i][j] == 8) {
                        mNumbersTextView[i][j].setBackgroundColor(getResources().getColor(R.color.colorOrange));
                        mNumbersTextView[i][j].setTextColor(getResources().getColor(R.color.colorWhite));
                        mNumbersTextView[i][j].setTextSize(getResources().getDimension(R.dimen.app_2048_numbers_size));
                    } else if (mNumbersINT[i][j] == 16) {
                        mNumbersTextView[i][j].setBackgroundColor(getResources().getColor(R.color.colorDarkOrange));
                        mNumbersTextView[i][j].setTextColor(getResources().getColor(R.color.colorWhite));
                        mNumbersTextView[i][j].setTextSize(getResources().getDimension(R.dimen.app_2048_numbers_size));
                    } else if (mNumbersINT[i][j] == 32) {
                        mNumbersTextView[i][j].setBackgroundColor(getResources().getColor(R.color.colorVeryDarkOrange));
                        mNumbersTextView[i][j].setTextColor(getResources().getColor(R.color.colorWhite));
                        mNumbersTextView[i][j].setTextSize(getResources().getDimension(R.dimen.app_2048_numbers_size));
                    } else if (mNumbersINT[i][j] == 64) {
                        mNumbersTextView[i][j].setBackgroundColor(getResources().getColor(R.color.colorRed));
                        mNumbersTextView[i][j].setTextColor(getResources().getColor(R.color.colorWhite));
                        mNumbersTextView[i][j].setTextSize(getResources().getDimension(R.dimen.app_2048_numbers_size));
                    } else if ((mNumbersINT[i][j] == 128) || (mNumbersINT[i][j] == 256)) {
                        mNumbersTextView[i][j].setBackgroundColor(getResources().getColor(R.color.colorDarkYellow));
                        mNumbersTextView[i][j].setTextColor(getResources().getColor(R.color.colorWhite));
                        mNumbersTextView[i][j].setTextSize(getResources().getDimension(R.dimen.app_2048_mid_numbers_size));
                    } else if (mNumbersINT[i][j] == 512) {
                        mNumbersTextView[i][j].setBackgroundColor(getResources().getColor(R.color.colorYellow));
                        mNumbersTextView[i][j].setTextColor(getResources().getColor(R.color.colorRed));
                        mNumbersTextView[i][j].setTextSize(getResources().getDimension(R.dimen.app_2048_mid_numbers_size));
                    } else if ((mNumbersINT[i][j] == 1024) || (mNumbersINT[i][j] == 2048)) {
                        mNumbersTextView[i][j].setBackgroundColor(getResources().getColor(R.color.colorYellow));
                        mNumbersTextView[i][j].setTextColor(getResources().getColor(R.color.colorRed));
                        mNumbersTextView[i][j].setTextSize(getResources().getDimension(R.dimen.app_2048_big_numbers_size));
                    } else if (mNumbersINT[i][j] > 2048) {
                        mNumbersTextView[i][j].setBackgroundColor(getResources().getColor(R.color.colorBlack));
                        mNumbersTextView[i][j].setTextColor(getResources().getColor(R.color.colorGreen));
                        mNumbersTextView[i][j].setTextSize(getResources().getDimension(R.dimen.app_2048_big_numbers_size));
                    }
                }
            }
        }
        mScoreView.setText(String.valueOf(Score));
        if (Score > mRecord){
            mRecord = Score;
        }
        mRecordView.setText(String.valueOf(mRecord));
        if (!checkIfChangesCanBeMade()) {
            outGameOver();
        }
    }

    private void outGameOver() {
        mScoreView.setText(getResources().getText(R.string.GameOver));
        mScoreView.setTextColor(getResources().getColor(R.color.colorRed));
        mScoreView.setBackgroundColor(getResources().getColor(R.color.colorBlack));
    }

    private boolean checkIfEmptyNumbersExists() {
        boolean Check = false;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (mNumbersINT[i][j] == 0)
                    Check = true;
        return Check;
    }

    private void setRandomNumbers() {
        boolean Check = checkIfEmptyNumbersExists();
        int i, j, r;
        while (Check) {
            i = random.nextInt(4);
            j = random.nextInt(4);
            if (mNumbersINT[i][j] == 0) {
                r = random.nextInt(6);
                if (r < 5)
                    mNumbersINT[i][j] = 2;
                else
                    mNumbersINT[i][j] = 4;
                Check = false;
            }
        }
    }

    private void moveTopSimple() {
        for (int a = 0; a < 4; a++)
            for (int i = 3; i > 0; i--)
                for (int j = 0; j < 4; j++)
                    if (mNumbersINT[i][j] == 0) {
                        mNumbersINT[i][j] = mNumbersINT[i - 1][j];
                        mNumbersINT[i - 1][j] = 0;
                    }
    }

    private void sumTop(int i, boolean s) {
        for (int j = 0; j < 4; j++) {
            if ((mNumbersINT[i][j] == mNumbersINT[i - 1][j]) && (mNumbersINT[i - 1][j] != 0)) {
                mNumbersINT[i][j] = mNumbersINT[i][j] * 2;
                if (s)
                    Score = Score + mNumbersINT[i][j];
                mNumbersINT[i - 1][j] = 0;
            }
        }
    }

    private void moveTopComplicated(boolean s) {
        for (int i = 3; i > 0; i--) {
            moveTopSimple();
            sumTop(i, s);
        }
    }

    private void moveBottomSimple() {
        for (int a = 0; a < 4; a++)
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 4; j++)
                    if (mNumbersINT[i][j] == 0) {
                        mNumbersINT[i][j] = mNumbersINT[i + 1][j];
                        mNumbersINT[i + 1][j] = 0;
                    }
    }

    private void sumBottom(int i, boolean s) {
        for (int j = 0; j < 4; j++) {
            if ((mNumbersINT[i][j] == mNumbersINT[i + 1][j]) && (mNumbersINT[i + 1][j] != 0)) {
                mNumbersINT[i][j] = mNumbersINT[i][j] * 2;
                if (s)
                    Score = Score + mNumbersINT[i][j];
                mNumbersINT[i + 1][j] = 0;
            }
        }
    }

    private void moveBottomComplicated(boolean s) {
        for (int i = 0; i < 3; i++) {
            moveBottomSimple();
            sumBottom(i, s);
        }
    }

    private void moveLeftSimple() {
        for (int a = 0; a < 4; a++)
            for (int i = 0; i < 4; i++)
                for (int j = 0; j < 3; j++)
                    if (mNumbersINT[i][j] == 0) {
                        mNumbersINT[i][j] = mNumbersINT[i][j + 1];
                        mNumbersINT[i][j + 1] = 0;
                    }
    }

    private void sumLeft(int i, boolean s) {
        for (int j = 0; j < 4; j++) {
            if ((mNumbersINT[j][i] == mNumbersINT[j][i + 1]) && (mNumbersINT[j][i + 1] != 0)) {
                mNumbersINT[j][i] = mNumbersINT[j][i] * 2;
                if (s)
                    Score = Score + mNumbersINT[j][i];
                mNumbersINT[j][i + 1] = 0;
            }
        }
    }

    private void moveLeftComplicated(boolean s) {
        for (int i = 0; i < 3; i++) {
            moveLeftSimple();
            sumLeft(i, s);
        }
    }

    private void moveRightSimple() {
        for (int a = 0; a < 4; a++)
            for (int i = 0; i < 4; i++)
                for (int j = 3; j > 0; j--)
                    if (mNumbersINT[i][j] == 0) {
                        mNumbersINT[i][j] = mNumbersINT[i][j - 1];
                        mNumbersINT[i][j - 1] = 0;
                    }
    }

    private void sumRight(int i, boolean s) {
        for (int j = 0; j < 4; j++) {
            if ((mNumbersINT[j][i] == mNumbersINT[j][i - 1]) && (mNumbersINT[j][i - 1] != 0)) {
                mNumbersINT[j][i] = mNumbersINT[j][i] * 2;
                if (s)
                    Score = Score + mNumbersINT[j][i];
                mNumbersINT[j][i - 1] = 0;
            }
        }
    }

    private void moveRightComplicated(boolean s) {
        for (int i = 3; i > 0; i--) {
            moveRightSimple();
            sumRight(i, s);
        }
    }

    private void saveOLDNumbers() {
        for (int i = 0; i < 4; i++)
            System.arraycopy(mNumbersINT[i], 0, mOLDNumbersINT[i], 0, 4);
        OldScore = Score;
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buttonStartGame:
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        mNumbersINT[i][j] = 0;
                        mOLDNumbersINT[i][j] = 0;
                        Score = 0;
                    }
                }
                setRandomNumbers();
                setRandomNumbers();
                setNumbersToTextView();
                mScoreView.setTextColor(getResources().getColor(R.color.colorBlack));
                mScoreView.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                break;
            case R.id.buttonReverseMove:
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        mNumbersINT[i][j] = mOLDNumbersINT[i][j];
                        Score = OldScore;
                    }
                }
                setNumbersToTextView();
                mScoreView.setTextColor(getResources().getColor(R.color.colorBlack));
                mScoreView.setBackgroundColor(getResources().getColor(R.color.colorYellow));
                break;
        }
    }

    public void some(){}

}
