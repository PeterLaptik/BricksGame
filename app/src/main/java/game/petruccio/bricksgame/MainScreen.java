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
    private TextView btnStart;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        btnStart = (TextView) findViewById(R.id.btnStartGame);
        btnStart.setOnClickListener(this);
        image = findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        if(v==btnStart){
            GlassNetArray.getInstance().clear();
            Intent intent = new Intent(MainScreen.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
