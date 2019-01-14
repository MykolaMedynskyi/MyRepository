package bin;

import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class LabelGame {

    public LabelGame(){}

    public void visibleLable(Label label, String text, int time){
        label.setVisible(true);
        label.setText(text);
        PauseTransition visiblePause = new PauseTransition(
                Duration.seconds(time)
        );
        visiblePause.setOnFinished(
                event -> label.setVisible(false)
        );
        visiblePause.play();
    }

}
