package game.petruccio.bricksgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Implements tetris keyboard (right for the horizontal layout).
 * Connected to parent class via interface KeyboardCallback.
 */
public class TKeyboardRight extends Fragment implements Button.OnClickListener {
    private Button btnRight, btnRotate;
    private KeyboardCallback parent;

    public TKeyboardRight() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.tetris_keyboard_right, container, false);
        btnRight = result.findViewById(R.id.btnVRight);
        btnRotate = result.findViewById(R.id.btnVRotate);
        btnRight.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
        return result;
    }

    public void setKeyboardListener(KeyboardCallback c){
        parent = c;
    }

    @Override
    public void onClick(View v) {
        if (parent==null)
            return;
        if(v==btnRight)
            parent.pressedRight();
        if(v==btnRotate)
            parent.pressedRotate();
    }
}
