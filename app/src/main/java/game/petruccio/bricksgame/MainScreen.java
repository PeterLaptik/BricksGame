package game.petruccio.bricksgame;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Main screen of the game.
 * Starts first.
 */
public class MainScreen extends AppCompatActivity implements TextView.OnClickListener {
    private final int COUNTS = 10;
    private TextView btnStart, btnScores, btnSettings, btnCredits, hText;
    private ImageView image;
    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Configuration conf = getResources().getConfiguration();

        if(conf.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_main_screen);
            btnStart = (TextView) findViewById(R.id.btnStartGame);
            btnScores = (TextView) findViewById(R.id.btnScores);
            btnSettings = (TextView) findViewById(R.id.btnSettings);
            btnCredits = (TextView) findViewById(R.id.btnCredits);
            hText = (TextView)findViewById(R.id.hiddenText);
            image = findViewById(R.id.imageViewLogo);
        } else {
            setContentView(R.layout.activity_main_screen_hor);
            btnStart = (TextView) findViewById(R.id.btnStartGameH);
            btnScores = (TextView) findViewById(R.id.btnScoresH);
            btnSettings = (TextView) findViewById(R.id.btnSettingsH);
            btnCredits = (TextView) findViewById(R.id.btnCreditsH);
            hText = (TextView)findViewById(R.id.hiddenTextH);
            image = findViewById(R.id.imageViewLogoH);
        }

        btnStart.setOnClickListener(this);
        btnScores.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnCredits.setOnClickListener(this);
        image.setOnClickListener(this);
        ScoreKeeper.getInstance().setContext(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if(v==btnStart){
            GlassNetArray.getInstance().clear();
            Intent intent = new Intent(MainScreen.this, MainActivity.class);
            startActivity(intent);
            counter = 0;
        }
        if(v==btnScores){
            Intent intent = new Intent(MainScreen.this, MainScores.class);
            startActivity(intent);
            counter = 0;
        }
        if(v==btnSettings){
            Intent intent = new Intent(MainScreen.this, MainSettings.class);
            startActivity(intent);
            counter = 0;
        }
        if(v==btnCredits){
            Intent intent = new Intent(MainScreen.this, MainAbout.class);
            startActivity(intent);
            counter = 0;
        }
        if(v==image){
            counter++;
            if(counter>=COUNTS)
                hText.setText("CRACKED BY BILL GILBERT");
        }
    }
}
