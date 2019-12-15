package ch.yumeart;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    // mediaPlayer to play sounds
    private CustomMediaPlayer mediaPlayer;

    // switch if audio shoud be played after the game ended or not
    private boolean soundWanted = false;

    // total number of cards in the game
    private int gameSize;

    // gameMode: 0 = color, 1 = icons, 2 = paintings
    private int gameMode;

    // number of Columns used in the GridLayout
    private int numColumns;

    // number of Rows used in the GridLayout
    private int numRows;

    // counters for the statspanel
    private int pairsCounter = 0;
    private int attemptsCounter = 0;
    private double ratioCounter = 0.0;

    // text-fields for the statspanel
    private TextView textPairsCounter;
    private TextView textAttemptsCounter;
    private TextView textRatioCounter;

    // size of every card, depending on the size of the user's screen
    private int cardDimension;

    // delay in milliseconds, will be more in paintings version
    private int delayMillis;

    // id of the layout that will be used, depending on the gameSize
    private GridLayout gridLayout;

    // Buttons array that stores all the buttons
    private MemoryButtonImages[] buttons;

    // integer arrays that store the graphics and
    private int[] buttonGraphicLocations; // each buttons resource
    private int[] buttonGraphics; // stores the buttons id's

    // Buttons to store the currently selected buttons
    private MemoryButtonImages selectedButton1;
    private MemoryButtonImages selectedButton2;

    // boolean that is true if 2 cards are revealed (game will stop for a short time)
    private boolean isBusy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getting the soundWanted, gameMode and gameSize from MainActivity-class.
        // setting gamesize 16 cards + gamemode icons as default
        soundWanted = getIntent().getBooleanExtra("soundWanted", false);
        gameSize = getIntent().getIntExtra("gameSize", 16);
        gameMode = getIntent().getIntExtra("gameMode", 1);

        setLayout();

        // setting the number of columns and rows of the current layout
        numColumns = gridLayout.getColumnCount();
        numRows = gridLayout.getRowCount();


        setCardDimension();

        switch(gameMode) {
            case 0:
                loadButtonGraphicsColors();
                delayMillis = 600;
                break;
            case 1:
                loadButtonGraphicsIcons();
                delayMillis = 600;
                break;
            case 2:
                loadButtonGraphicsPaintings();
                delayMillis = 700;
                break;
            default:
                loadButtonGraphicsIcons();
        }

        shuffleButtonGraphics();

        createCards();

        setStats();

    }

    private void setStats() {
        textPairsCounter = findViewById(R.id.textPairsCounter);
        textAttemptsCounter = findViewById(R.id.textAttemptsCounter);
        textRatioCounter = findViewById(R.id.textRatioCounter);

        textPairsCounter.setText(String.valueOf(pairsCounter));
        textAttemptsCounter.setText(String.valueOf(attemptsCounter));
        textRatioCounter.setText(String.valueOf(ratioCounter));
    }

    private void setLayout() {
        // setting ID of the correct gridlayout, depending on the gameSize:
        switch(gameSize) {
            case 16:
                setContentView(R.layout.activity_game_activity4x4);
                gridLayout = findViewById(R.id.gridLayout4x4);
                break;
            case 24:
                setContentView(R.layout.activity_game_activity4x6);
                gridLayout = findViewById(R.id.gridLayout4x6);
                break;
            case 30:
                setContentView(R.layout.activity_game_activity5x6);
                gridLayout = findViewById(R.id.gridLayout5x6);
                break;
            case 40:
                setContentView(R.layout.activity_game_activity5x8);
                gridLayout = findViewById(R.id.gridLayout5x8);
                break;
        }
    }

    private void setCardDimension() {
        // setting the cardsize depending on the screensize and screenformat
        int tempCardWidth = MainActivity.getScreenWidth() / ((numColumns * 2) + 1);
        int tempCardHeight = MainActivity.getScreenHeight() / ((numRows * 2) + 3);
        if(tempCardHeight > tempCardWidth) {
            cardDimension = tempCardWidth;
        } else {
            cardDimension = tempCardHeight;
        }
    }

    private void loadButtonGraphicsColors() {
        buttons = new MemoryButtonImages[gameSize];

        buttonGraphics = new int[gameSize /2];
        for(int i = 0; i < (gameSize / 2); i++) {
            String iconName = "c" + i;
            buttonGraphics[i] = getResources().getIdentifier(iconName, "drawable", this.getPackageName());
        }
        buttonGraphicLocations = new int[gameSize];
    }

    private void loadButtonGraphicsIcons() {
        buttons = new MemoryButtonImages[gameSize];

        buttonGraphics = new int[gameSize /2];
        for(int i = 0; i < (gameSize / 2); i++) {
            String iconName = "i" + i;
            buttonGraphics[i] = getResources().getIdentifier(iconName, "drawable", this.getPackageName());
        }
        buttonGraphicLocations = new int[gameSize];
    }

    private void loadButtonGraphicsPaintings() {
        buttons = new MemoryButtonImages[gameSize];

        buttonGraphics = new int[gameSize /2];
        for(int i = 0; i < (gameSize / 2); i++) {
            String iconName = "p" + i;
            buttonGraphics[i] = getResources().getIdentifier(iconName, "drawable", this.getPackageName());
        }
        buttonGraphicLocations = new int[gameSize];
    }

    private void shuffleButtonGraphics() {
        Random rand = new Random();

        // filling the array with pairs of cards
        for (int i = 0; i < gameSize; i++) {
            buttonGraphicLocations[i] = i % (gameSize / 2);
        }

        // shuffling the cards within the array
        for (int i = 0; i < gameSize; i++) {
            int temp = buttonGraphicLocations[i];
            int swapIndex = rand.nextInt(gameSize);
            buttonGraphicLocations[i] = buttonGraphicLocations[swapIndex];
            buttonGraphicLocations[swapIndex] = temp;
        }
    }

    private void createCards() {
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numColumns; c++) {
                // converting the 2-dimensional location to 1 dimension:
                // The index of the required image
                MemoryButtonImages tempButton = new MemoryButtonImages(this, r, c,
                        buttonGraphics[buttonGraphicLocations[r * numColumns + c]], cardDimension);
                tempButton.setId(View.generateViewId()); // generate unique ID
                tempButton.setOnClickListener(this); // The class implements the OnClickListener so we can pass this
                if(soundWanted) {
                    tempButton.setSoundEffectsEnabled(true);
                } else {
                    tempButton.setSoundEffectsEnabled(false);
                }
                buttons[r * numColumns + c] = tempButton; // storing away the buttons for future uses
                gridLayout.addView(tempButton);
            }
        }
    }

    @Override
    public void onClick(View view) {

        if(isBusy) {
            return;
        }

        MemoryButtonImages button = (MemoryButtonImages) view;

        if(button.isMatched) {
            return;
        }

        if(selectedButton1 == null) {
            selectedButton1 = button;
            selectedButton1.flip();
            return;
        }

        if(selectedButton1.getId() == button.getId()) {
            return;
        }

        // the cards match
        if(selectedButton1.getFrontImageDrawableID() == button.getFrontImageDrawableID()) {
            button.flip();

            button.setMatched(true);
            selectedButton1.setMatched(true);

            selectedButton1.setEnabled(false);
            button.setEnabled(false);

            selectedButton1 = null;

            // increasing the stats values
            attemptsCounter++;
            pairsCounter++;
            textPairsCounter.setText(String.valueOf(pairsCounter));
            textAttemptsCounter.setText(String.valueOf(attemptsCounter));
            setRatio();

            endGame();

        } else { // the cards dont match
            selectedButton2 = button;
            selectedButton2.flip();
            isBusy = true;

            // increasing the stats values
            attemptsCounter++;
            textAttemptsCounter.setText(String.valueOf(attemptsCounter));
            setRatio();

            final Handler handler = new Handler();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectedButton2.flip();
                    selectedButton1.flip();
                    selectedButton2 = null;
                    selectedButton1 = null;
                    isBusy = false;
                }
            }, delayMillis);
        }
    }

    private void setRatio() {
        double unrounded = (double)pairsCounter / attemptsCounter;
        unrounded *= 2;
        BigDecimal bigDecimal = BigDecimal.valueOf(unrounded);
        bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);
        double rounded = bigDecimal.doubleValue();

        textRatioCounter.setText(String.valueOf(rounded));

        ratioCounter = rounded;
    }

    private void endGame() {
        if(pairsCounter == (gameSize / 2)) {
            Toast toastYouWon = Toast.makeText(this, R.string.toastMessageEndGame, Toast.LENGTH_SHORT);
            toastYouWon.setGravity(Gravity.CENTER, 0, 0);
            LinearLayout toastLayout = (LinearLayout) toastYouWon.getView();
            TextView toastTV = (TextView) toastLayout.getChildAt(0);
            toastTV.setTextSize(30);
            toastYouWon.show();


            if(soundWanted) {
                playSound();
            }
        }
    }

    private void playSound() {
        int sound;

        if(gameSize == 16 && ratioCounter >= 1.2
                || gameSize == 24 && ratioCounter >= 1.1
                || gameSize == 30 && ratioCounter >= 1.0
                || gameSize == 40 && ratioCounter >= 0.6) {
            sound = CustomMediaPlayer.sound_great_job;
        } else  if(gameSize == 16 && ratioCounter >= 1.05
                || gameSize == 24 && ratioCounter >= 0.9
                || gameSize == 30 && ratioCounter >= 0.8
                || gameSize == 40 && ratioCounter >= 0.5) {
            sound = CustomMediaPlayer.sound_well_done;
        } else  if(gameSize == 16 && ratioCounter >= 0.7
                || gameSize == 24 && ratioCounter >= 0.65
                || gameSize == 30 && ratioCounter >= 0.6
                || gameSize == 40 && ratioCounter >= 0.4) {
            sound = CustomMediaPlayer.sound_congrats;
        } else  if(gameSize == 16 && ratioCounter >= 0.5
                || gameSize == 24 && ratioCounter >= 0.45
                || gameSize == 30 && ratioCounter >= 0.4
                || gameSize == 40 && ratioCounter >= 0.3) {
            sound = CustomMediaPlayer.sound_finally;
        } else {
            sound = CustomMediaPlayer.sound_bad;
        }

//      mediaPlayer = new CustomMediaPlayer(this, CustomMediaPlayer.soundHappyDay);
        mediaPlayer = new CustomMediaPlayer(this, sound);
    }

    // Override method, so the music stops when leaving the game
    @Override
    protected void onPause() {
        if((mediaPlayer != null) && mediaPlayer.isPlaying) {
            mediaPlayer.stopPlaying();
        }
        super.onPause();
    }
}
