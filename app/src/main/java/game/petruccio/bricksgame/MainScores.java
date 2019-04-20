package game.petruccio.bricksgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class MainScores extends AppCompatActivity {
    private RecyclerView scoreTable;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);
        scoreTable = (RecyclerView)findViewById(R.id.resScoreTable);

        RecyclerView.LayoutManager mgr = new LinearLayoutManager(this);
        scoreTable.setLayoutManager(mgr);

        adapter = new ScoreAdapter(ScoreKeeper.getInstance().getNames(), ScoreKeeper.getInstance().getScores());
        scoreTable.setAdapter(adapter);
    }
}
