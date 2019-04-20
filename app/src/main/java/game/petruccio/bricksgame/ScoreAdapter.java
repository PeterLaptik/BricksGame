package game.petruccio.bricksgame;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;

/**
 * An adapter for score table layout.
 */
public class ScoreAdapter extends RecyclerView.Adapter {
    String[] dataSetNames;
    int[] dataSetScores;

    public ScoreAdapter(String[] names, int[] scores){
        dataSetNames = names;
        dataSetScores = scores;
    }

    @Override
    public ScoreAdapter.ScoreViewHolder onCreateViewHolder(ViewGroup parent, int ViewType){
        View result = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ScoreViewHolder(result);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        ((ScoreViewHolder)holder).txtName.setText(dataSetNames[position]);
        ((ScoreViewHolder)holder).txtScores.setText(Integer.toString(dataSetScores[position]));
        ((ScoreViewHolder)holder).txtNum.setText(Integer.toString(position+1));
    }

    @Override
    public int getItemCount(){
        return dataSetNames.length;
    }

    // Viewholder for list item
    public static class ScoreViewHolder  extends RecyclerView.ViewHolder{
        public TextView txtName;
        public TextView txtScores;
        public TextView txtNum;

        public ScoreViewHolder(View v){
            super(v);
            txtName = v.findViewById(R.id.scoreItemName);
            txtScores = v.findViewById(R.id.scoreItemScores);
            txtNum = v.findViewById(R.id.textNum);
        }
    }
}
