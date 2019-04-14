package game.petruccio.bricksgame;

public class ScoreKeeper {
    private ScoreKeeper instance = null;
    private final int TABLE_SIZE = 10;
    private final String DEFAULT_NAME = "Player";
    private int[] scores = new int[TABLE_SIZE];
    private String[] names = new String[TABLE_SIZE];

    private ScoreKeeper(){

    }

    public ScoreKeeper getInstance(){
        if(instance==null)
            instance = new ScoreKeeper();
        return instance;
    }

    /**
     * Checks if the scores are to be added in the scores table.
     * @param newScores - scores value to check.
     * @return - add scores to table or not.
     */
    public boolean isNewRecord(int newScores){
        for(int i=0; i<TABLE_SIZE; i++){
            if(scores[i]<newScores)
                return true;
        }
        return false;
    }
}
