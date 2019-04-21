package game.petruccio.bricksgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Popup dialog for a player name input.
 */
public class DialogInputName extends DialogFragment implements View.OnClickListener {
    private final String DEF_NAME = "PLAYER";
    private int scores = 0;
    private EditText inputName;

    public DialogInputName() {
    }

    public void setScores(int s){
        scores = s;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog dialog;
        View view;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        TextView title = new TextView(getContext());
        title.setText(R.string.strEnterName);
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(18);
        builder.setCustomTitle(title);
        view = inflater.inflate(R.layout.dialog_name, null);
        builder.setView(view);
        dialog =  builder.create();

        dialog.setCanceledOnTouchOutside(false);
        ((TextView)view.findViewById(R.id.txtDialScores))
                .setText(getActivity().getResources().getString(R.string.strPlayerScores) + Integer.toString(scores));
        ((TextView)view.findViewById(R.id.txtDialScores)).setGravity(Gravity.CENTER);
        ((Button)view.findViewById(R.id.btnNameOK)).setOnClickListener(this);
        inputName = (EditText)view.findViewById(R.id.textNameInput);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        String name;
        name = inputName.getText().toString();
        if((name==null) || (name.length()<1))
            name = DEF_NAME;
        ScoreKeeper.getInstance().insertScores(name.toUpperCase(), scores);
        this.dismiss();
    }
}
