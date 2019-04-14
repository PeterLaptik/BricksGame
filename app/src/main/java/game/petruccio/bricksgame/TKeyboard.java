package game.petruccio.bricksgame;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Implements tetris keyboard.
 * Connected to parent class via interface KeyboardCallback.
 */
public class TKeyboard extends Fragment implements Button.OnClickListener{
    Button btnLeft, btnRight, btnDown, btnRotate;
    KeyboardCallback parent = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        View keyboard;
        keyboard = inflater.inflate(R.layout.tetris_keyboard, container, false);
        btnLeft = (Button) keyboard.findViewById(R.id.buttonLeft);
        btnRight = (Button) keyboard.findViewById(R.id.buttnonRight);
        btnDown = (Button) keyboard.findViewById(R.id.buttonDown);
        btnRotate = (Button) keyboard.findViewById(R.id.buttonRotate);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        btnDown.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
        return keyboard;
    }

    public void setKeyboardListener(KeyboardCallback c){
        parent = c;
    }

    @Override
    public void onClick(View v) {
        if (parent==null)
            return;
        if (v==btnLeft)
            parent.pressedLeft();
        if(v==btnRight)
            parent.pressedRight();
        if(v==btnRotate)
            parent.pressedRotate();
        if(v==btnDown)
            parent.pressedDown();
    }
}
