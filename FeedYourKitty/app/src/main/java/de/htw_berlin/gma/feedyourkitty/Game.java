package de.htw_berlin.gma.feedyourkitty;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Game extends Activity
{
    private Intent          intentMenu;

    private TextView        pointDisplay;
    private RelativeLayout  layoutGame;

    private ImageButton     button00;
    private ImageButton     button01;
    private ImageButton     button02;
    private ImageButton     button03;
    private ImageButton     button04;
    private ImageButton     button05;
    private ImageButton     button06;
    private ImageButton     button07;
    private ImageButton     button08;

    private ImageButton[]   buttons;
    private boolean[]       buttonStates;                   //holds good/bad states for each button
    private float[]         buttonResetRoutines;
    private float[]         buttonTurnGoodRoutines;

    private int             points;
    private float           timeRemaining = 30.0f;          //time per round
    private float           backgroundTime = 0.0f;
    private final float     backgroundTimeDuration = 1.5f;  //time the background-change is visible
    private boolean         gameIsRunning = true;           //boolean to stop timers on level end

    private Random          randomGenerator;
    private Timer           timeGame;                       //main game timer
    private Timer           timerBackground;

    private Bitmap          foodGood;
    private Bitmap          foodBad;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //find layout
        layoutGame = (RelativeLayout) findViewById(R.id.layout_game);
        pointDisplay = (TextView) findViewById(R.id.point_display);
        foodGood = BitmapFactory.decodeResource(getResources(), R.drawable.food_good_01);
        foodBad = BitmapFactory.decodeResource(getResources(), R.drawable.food_bad_01);

        randomGenerator = new Random();

        //get all buttons
        button00 = (ImageButton) findViewById(R.id.game_button_00);
        button01 = (ImageButton) findViewById(R.id.game_button_01);
        button02 = (ImageButton) findViewById(R.id.game_button_02);
        button03 = (ImageButton) findViewById(R.id.game_button_03);
        button04 = (ImageButton) findViewById(R.id.game_button_04);
        button05 = (ImageButton) findViewById(R.id.game_button_05);
        button06 = (ImageButton) findViewById(R.id.game_button_06);
        button07 = (ImageButton) findViewById(R.id.game_button_07);
        button08 = (ImageButton) findViewById(R.id.game_button_08);

        buttons = new ImageButton[9];

        buttons[0] = (ImageButton) button00;
        buttons[1] = (ImageButton) button01;
        buttons[2] = (ImageButton) button02;
        buttons[3] = (ImageButton) button03;
        buttons[4] = (ImageButton) button04;
        buttons[5] = (ImageButton) button05;
        buttons[6] = (ImageButton) button06;
        buttons[7] = (ImageButton) button07;
        buttons[8] = (ImageButton) button08;

        //create buttonStates - each button is either true (good) or false (bad)
        buttonStates = new boolean[9];
        for(int i = 0; i < buttons.length; i++)
        {
            buttonStates[i] = randomGenerator.nextBoolean();
            SetButtonStatus(i, buttonStates[i]);
        }

        //create buttonResetRoutines. each button has its own routine
        buttonResetRoutines = new float[9];
        for(int i = 0; i < buttonResetRoutines.length; i++)
            buttonResetRoutines[i] = 0.0f;

        //create buttonTurnGoodRoutines. each button has its own routine
        buttonTurnGoodRoutines = new float[9];
        for(int i = 0; i < buttonTurnGoodRoutines.length; i++)
            buttonTurnGoodRoutines[i] = 0.0f;

        //create intent
        intentMenu = new Intent(this, MainMenu.class);

        //display the score once on startup, then every time it changes
        DisplayScore();

        //start game-timer
        StartGameTimer();
        StartBackgroundTimer();

        //activate turnGoodRoutines for all "bad" buttons
        for(int i = 0; i < buttons.length; i++)
            if(!buttonStates[i])
                ButtonTurnGoodRoutine(i);
    }

    private void StartBackgroundTimer()
    {
        timerBackground = new Timer();
        timerBackground.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        backgroundTime -= 0.1f;

                        if(!gameIsRunning)
                            timerBackground.cancel();

                        if(backgroundTime <= 0.0f)
                        {
                            backgroundTime = 0.0f;

                            //set background back to neutral image
                            layoutGame.setBackgroundResource(R.drawable.bg_neutral);
                        }
                    }
                });
            }
        }, 0, 100);
    }

    private void StartGameTimer()
    {
        timeGame = new Timer();
        timeGame.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        TextView timerDisplay = (TextView) findViewById(R.id.timer_display);
                        timeRemaining -= 0.1f;

                        if(timeRemaining <= 0.0f)
                        {
                            timeRemaining = 0.0f;
                            timeGame.cancel();
                            LoadEnterName();
                        }

                        timerDisplay.setText("Time: " + RoundToTwoDecimalPoints(timeRemaining));
                    }
                });
            }
        }, 0, 100);
    }

    private float RoundToTwoDecimalPoints(float inputFloat)
    {
        return (float) Math.round(inputFloat * 100.0f) / 100.0f;
    }

    private ImageButton GetButton(int buttonID)
    {
        //calling buttons from array & setting image doesn't work for some reason
        if(buttonID == 0)
            return button00;
        else if(buttonID == 1)
            return button01;
        else if(buttonID == 2)
            return button02;
        else if(buttonID == 3)
            return button03;
        else if(buttonID == 4)
            return button04;
        else if(buttonID == 5)
            return button05;
        else if(buttonID == 6)
            return button06;
        else if(buttonID == 7)
            return button07;
        else
            return button08;
    }

    //sets the texture used on the button
    private void SetButtonStatus(int buttonID, boolean state)
    {
        Bitmap newImage = foodGood;
        if(!state)
            newImage = foodBad;

        GetButton(buttonID).setImageBitmap(newImage);
    }

    private void DisableButton(int buttonID)
    {
        if(buttons[buttonID].isEnabled())
        {
            buttons[buttonID].setClickable(false);
            buttons[buttonID].setVisibility(View.INVISIBLE);
            buttons[buttonID].setEnabled(false);
        }
    }

    private void EnableButton(int buttonID)
    {
        if(!buttons[buttonID].isEnabled())
        {
            buttons[buttonID].setClickable(true);
            buttons[buttonID].setVisibility(View.VISIBLE);
            buttons[buttonID].setEnabled(true);

            //assign new buttonState
            buttonStates[buttonID] = randomGenerator.nextBoolean();
            SetButtonStatus(buttonID, buttonStates[buttonID]);

            //if "bad" button, start turnGoodRoutine
            if(!buttonStates[buttonID])
                ButtonTurnGoodRoutine(buttonID);
        }
    }

    public void ButtonClicked00(View view){ButtonClicked(0);}
    public void ButtonClicked01(View view){ButtonClicked(1);}
    public void ButtonClicked02(View view){ButtonClicked(2);}
    public void ButtonClicked03(View view){ButtonClicked(3);}
    public void ButtonClicked04(View view){ButtonClicked(4);}
    public void ButtonClicked05(View view){ButtonClicked(5);}
    public void ButtonClicked06(View view){ButtonClicked(6);}
    public void ButtonClicked07(View view){ButtonClicked(7);}
    public void ButtonClicked08(View view){ButtonClicked(8);}

    public void ButtonClicked(int buttonID)
    {
        //once button is clicked disable it and start resetRoutine
        ResetButtonRoutine(buttonID);

        //if good button add points, if bad button remove points. negative points are possible
        if(buttonStates[buttonID])
            points += 1;
        else
            points -= 1;

        //set kitty-background
        SetBackground(buttonStates[buttonID]);

        //points have changed, adapt the score-display
        DisplayScore();
    }

    private void SetBackground(boolean state)
    {
        if(state)   //set positive kitty
            layoutGame.setBackgroundResource(R.drawable.bg_happy);
        else        //set negative kitty
            layoutGame.setBackgroundResource(R.drawable.bg_sad);

        //set timer to 1.5f seconds, after which background returns to neutral;
        backgroundTime = backgroundTimeDuration;
    }

    private void ButtonTurnGoodRoutine(int buttonID)
    {
        final int id = buttonID;

        //reset local timer to 0 before beginning counting
        buttonTurnGoodRoutines[id] = 0.0f;

        //activation-length is random per call
        final float routineLength = randomGenerator.nextInt(4) + 1f;

        final Timer timerButton = new Timer();
        timerButton.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        buttonTurnGoodRoutines[id] += 1f;

                        if(!gameIsRunning)
                            timerButton.cancel();

                        if(buttonTurnGoodRoutines[id] >= routineLength)
                        {
                            timerButton.cancel();
                            SetButtonStatus(id, true);
                            buttonStates[id] = true;
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private void ResetButtonRoutine(int buttonID)
    {
        final int id = buttonID;
        DisableButton(id);
        buttonResetRoutines[id] = 0.0f;
        final float routineLength = 2.0f;

        final Timer timerButton = new Timer();
        timerButton.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        buttonResetRoutines[id] += 1f;

                        if(!gameIsRunning)
                            timerButton.cancel();

                        if(buttonResetRoutines[id] >= routineLength)
                        {
                            timerButton.cancel();
                            EnableButton(id);
                        }
                    }
                });
            }
        }, 0, 1000);
    }

    private void DisplayScore()
    {
        pointDisplay.setText("Score: " + points);
    }

    private void LoadEnterName()
    {
        gameIsRunning = false;

        Intent intentEnterName = new Intent (this, EnterName.class);
        intentEnterName.putExtra("points", points);
        startActivity(intentEnterName);
    }

    public void LoadMenu(View view)
    {
        startActivity(intentMenu);
    }
}