
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BestScore {
	int getBestScore() {
        int bestScore = 0;
        File scoreFile = new File("score/best_score.txt");

        try {
            // Check if the file exists
            if (scoreFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(scoreFile));
                String line = reader.readLine();
                if (line != null) {
                    bestScore = Integer.parseInt(line);
                }
                reader.close();
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace(); // Handle file IO or parsing errors
        }

        return bestScore;
    }

	void saveBestScore(int bestScore) {
	    File scoreFile = new File("score/best_score.txt");

	    try {
	        FileWriter writer = new FileWriter(scoreFile);
	        writer.write(String.valueOf(bestScore));
	        writer.close();
	    } catch (IOException e) {
	        e.printStackTrace(); // Handle file IO errors
	    }
	}
}
