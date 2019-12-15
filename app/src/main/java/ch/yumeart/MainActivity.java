package ch.yumeart;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonStart;
    private RadioGroup radiobuttonGroupSize, radiobuttonGroupMode;
    private RadioButton rb16cards, rb24cards, rb30cards, rb40cards, rbModeColor, rbModeIcon, rbModePainting;
    private int gameSize = -1;
    private int gameMode = -1;
    private static int screenWidth;
    private static int screenHeight;

    private Switch soundSwitch;
    private boolean soundWanted = false; // decides if audio will be played after the game ended or not.

    public static int getScreenWidth() {
        return screenWidth;
    }
    public static int getScreenHeight() {
        return screenHeight;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createGUI();

        screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        screenHeight = Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void createGUI() {
        this.radiobuttonGroupSize = findViewById(R.id.radiobuttonGroupSize);
        this.rb16cards = findViewById(R.id.rb16cards);
        this.rb24cards = findViewById(R.id.rb24cards);
        this.rb30cards = findViewById(R.id.rb30cards);
        this.rb40cards = findViewById(R.id.rb40cards);

        this.radiobuttonGroupMode = findViewById(R.id.radiobuttonGroupMode);
        this.rbModeColor = findViewById(R.id.rbModeColor);
        this.rbModeIcon = findViewById(R.id.rbModeIcons);
        this.rbModePainting = findViewById(R.id.rbModePaintings);

        this.buttonStart = findViewById(R.id.buttonStartGame);
        this.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // intent takes contextclass (this) and an activity-class
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("soundWanted", soundWanted);
                intent.putExtra("gameSize", gameSize);
                intent.putExtra("gameMode", gameMode);
                // start the activity
                startActivity(intent);
            }
        });
        this.buttonStart.setEnabled(false);

        this.soundSwitch = findViewById(R.id.switchSound);

        // set initaial values:
        this.radiobuttonGroupSize.clearCheck();
        this.radiobuttonGroupMode.clearCheck();

        // disable sounds as default
        setSoundOnWidgets(false);

        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    soundWanted = true;
                    setSoundOnWidgets(true);
                } else {
                    soundWanted = false;
                    setSoundOnWidgets(false);
                }
            }
        });


        // adding listener to the radiobuttongroupSize:
        this.radiobuttonGroupSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                int checkedRadioID = radiobuttonGroupSize.getCheckedRadioButtonId();

                if (checkedRadioID == R.id.rb16cards) {
                    gameSize = 16;
                } else if (checkedRadioID == R.id.rb24cards) {
                    gameSize = 24;
                } else if (checkedRadioID == R.id.rb30cards) {
                    gameSize = 30;
                } else if (checkedRadioID == R.id.rb40cards) {
                    gameSize = 40;
                }

                // enable the button
                if(gameMode >=0 && gameSize >= 0) {
                    buttonStart.setEnabled(true);
                }
            }
        });

        // adding listener to the radiobuttongroupMode:
        this.radiobuttonGroupMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int checkedRadioID = radiobuttonGroupMode.getCheckedRadioButtonId();

                if (checkedRadioID == R.id.rbModeColor) {
                    gameMode = 0;
                }
                if (checkedRadioID == R.id.rbModeIcons) {
                    Toast toast = Toast.makeText(MainActivity.this, R.string.toastMessageIconsBy, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, -getScreenHeight()/8);
                    toast.show();
                    // <div>Icons made by <a href="https://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> from <a href="https://www.flaticon.com/" title="Flaticon">www.flaticon.com</a></div>
                    gameMode = 1;
                }
                if (checkedRadioID == R.id.rbModePaintings) {
                    Toast toast = Toast.makeText(MainActivity.this, R.string.toastPaintingsBy, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, -getScreenHeight()/8);
                    toast.show();
                    gameMode = 2;
                }
                // enable the button
                if(gameMode >=0 && gameSize >= 0) {
                    buttonStart.setEnabled(true);
                }
            }
        });
    }

    private void setSoundOnWidgets(Boolean boolValue) {
        this.buttonStart.setSoundEffectsEnabled(boolValue);
        this.rb16cards.setSoundEffectsEnabled(boolValue);
        this.rb24cards.setSoundEffectsEnabled(boolValue);
        this.rb30cards.setSoundEffectsEnabled(boolValue);
        this.rb40cards.setSoundEffectsEnabled(boolValue);
        this.rbModeColor.setSoundEffectsEnabled(boolValue);
        this.rbModeIcon.setSoundEffectsEnabled(boolValue);
        this.rbModePainting.setSoundEffectsEnabled(boolValue);
        this.soundSwitch.setSoundEffectsEnabled(boolValue);
    }
}
