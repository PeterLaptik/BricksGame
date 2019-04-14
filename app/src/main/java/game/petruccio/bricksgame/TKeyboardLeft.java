package game.petruccio.bricksgame;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Implements tetris keyboard (left for the horizontal layout).
 * Connected to parent class via interface KeyboardCallback.
 */
public class TKeyboardLeft extends Fragment implements Button.OnClickListener{
    private Button btnLeft, btnDown;
    private KeyboardCallback parent;

    public TKeyboardLeft() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.tetris_keyboard_left, container, false);
        btnLeft = result.findViewById(R.id.btnVLeft);
        btnDown = result.findViewById(R.id.btnVDown);
        btnLeft.setOnClickListener(this);
        btnDown.setOnClickListener(this);
        return result;
    }

    public void setKeyboardListener(KeyboardCallback c){
        parent = c;
    }

    @Override
    public void onClick(View v) {
        if (parent==null)
            return;
        if(v==btnLeft)
            parent.pressedLeft();
        if(v==btnDown)
            parent.pressedDown();
    }
}
