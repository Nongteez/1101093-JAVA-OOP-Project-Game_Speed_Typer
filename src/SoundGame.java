import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class SoundGame {
	
	private Clip clip;

    public SoundGame() {
        try {
            // Select an audio clip
            clip = AudioSystem.getClip();
        } catch (Exception ex) {
            System.out.println("Error" + ex.getMessage());
        }
    }

    void GameSound() {
        try {
            // Stop the previous game sound if it's still running
            if (clip != null && clip.isRunning()) {
                clip.stop();
            }

            // Load the game sound file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("snd/8bit_sndTrack.wav").getAbsoluteFile());

            // Play the game sound
            clip.open(audioInputStream);

            // Get the volume control for the clip
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Reduce the volume by 10 decibels
            gainControl.setValue(-20.0f);

            clip.start();
        } catch (Exception ex) {
            System.out.println("Error" + ex.getMessage());
        }
    }
    
    void playRemoveWordSound() {
        try {
            // Load the sound file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("snd/correct.wav").getAbsoluteFile());

            // Select an audio clip
            Clip clip = AudioSystem.getClip();
            // Play the sound
            clip.open(audioInputStream);
         // Get the volume control for the clip
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Reduce the volume by 10 decibels
            gainControl.setValue(-10.0f);
            
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error" + ex.getMessage());
        }
    }
    
    void playGameOverSound() {
        try {
            // Load the game over sound file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("snd/gameOver.wav").getAbsoluteFile());

            // Select an audio clip
            Clip clip = AudioSystem.getClip();
            // Play the game over sound
            clip.open(audioInputStream);
         // Get the volume control for the clip
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Reduce the volume by 10 decibels
            gainControl.setValue(-10.0f);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error" + ex.getMessage());
        }
    }
}
