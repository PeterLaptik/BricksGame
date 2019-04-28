package game.petruccio.bricksgame;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Game settings menu:
 * - sound / mute
 * - network colour
 */
public class MainSettings extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {
    private TextView txtClr;
    private Switch sndSwitch;
    private SeekBar clrSeek;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        clrSeek = (SeekBar) findViewById(R.id.clrSeekBar);
        sndSwitch = (Switch) findViewById(R.id.sndSwitch);
        txtClr = (TextView) findViewById(R.id.txtColorChange);
        sndSwitch.setChecked(!GameSettings.getInstance().getMuted());
        clrSeek.setProgress((int)((GameSettings.getInstance().getColour() - 0xFFFFFF)/Math.pow(16, 6)));
        txtClr.setTextColor(GameSettings.getInstance().getColour());
        clrSeek.setOnSeekBarChangeListener(this);
        sndSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        GameSettings.getInstance().saveSettings();
    }

    // Colour seekbar listeners
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        GameSettings.getInstance().setColour(progress);
        txtClr.setTextColor(GameSettings.getInstance().getColour());
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    // Sound switch listener
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        GameSettings.getInstance().setMuted(!isChecked);
    }
}
