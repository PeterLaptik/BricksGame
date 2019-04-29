package game.petruccio.bricksgame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainAbout extends AppCompatActivity implements View.OnClickListener {
    private TextView txtAbout;
    private ImageView imgAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        txtAbout = (TextView)findViewById(R.id.aboutText);
        imgAbout = (ImageView) findViewById(R.id.aboutLogo);
        txtAbout.setOnClickListener(this);
        imgAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/PeterLaptik/BricksGame"));
        startActivity(webIntent);
    }
}
