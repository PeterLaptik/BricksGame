package game.petruccio.bricksgame;

import android.content.Intent;
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
    private TextView btnStart, btnScores, btnSettings, btnCredits;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        btnStart = (TextView) findViewById(R.id.btnStartGame);
        btnScores = (TextView) findViewById(R.id.btnScores);
        btnSettings = (TextView) findViewById(R.id.btnSettings);
        btnCredits = (TextView) findViewById(R.id.btnCredits);
        btnStart.setOnClickListener(this);
        btnScores.setOnClickListener(this);
        btnSettings.setOnClickListener(this);
        btnCredits.setOnClickListener(this);
        image = findViewById(R.id.imageView);
        ScoreKeeper.getInstance().setContext(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        if(v==btnStart){
            GlassNetArray.getInstance().clear();
            Intent intent = new Intent(MainScreen.this, MainActivity.class);
            startActivity(intent);
        }
        if(v==btnScores){
            Intent intent = new Intent(MainScreen.this, MainScores.class);
            startActivity(intent);
        }
        if(v==btnSettings){
            Intent intent = new Intent(MainScreen.this, MainSettings.class);
            startActivity(intent);
        }
    }
}
