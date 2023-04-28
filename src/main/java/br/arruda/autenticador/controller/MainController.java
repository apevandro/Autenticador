package br.arruda.autenticador.controller;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Controller;

import br.arruda.autenticador.service.key.KeyService;
import br.arruda.autenticador.service.totp.TotpService;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

	@NonNull private final KeyController keyController;
	@NonNull private final KeyService keyService;
	@NonNull private final TotpService totpService;

	@FXML
	private BorderPane rootPane;

	@FXML
    private TextField chave;

	@FXML
    private Label token;

	@FXML
    private ProgressBar barra;

	private Timeline timer;

	@PostConstruct
    public void init() {
		keyController.setMainController(this);
    }

	@FXML
    private void ativarChave(final Event event) {
		String token = totpService.gerarToken(chave.getText());
		if (Integer.valueOf(token) != 0) {
			keyService.putKey(chave.getText());
			this.token.setText(token);
			startAnimation();
		}
    }

	@FXML
    private void ultimasChaves(final Event event) throws IOException {
    	keyController.init(rootPane);
    }

	public void startOver() {
		String encodedKey = keyService.getEncodedKey();
		if (!encodedKey.isEmpty()) {
			chave.setText(encodedKey);
			String token = totpService.gerarToken(keyService.getEncodedKey());
            this.token.setText(token);
            startAnimation();
		}
	}

	public void startAnimation() {
		if (Status.STOPPED == getStatus()) {
			start();
		} else {
			timer.stop();
			start();
		}
	}

	private Status getStatus() {
		return timer == null ? Status.STOPPED : timer.getStatus();
	}

	private void start() {
		long timeStep = totpService.getTimeStep().getSeconds();
	    LongProperty seconds = new SimpleLongProperty(timeStep);

        barra.progressProperty().bind(seconds.divide(timeStep * 1.0).subtract(1).multiply(-1));

        timer = new Timeline(new KeyFrame(Duration.seconds(timeStep + 1), e -> {
            String token = totpService.gerarToken(keyService.getEncodedKey());
            this.token.setText(token);
        }));

        timer.getKeyFrames().add(new KeyFrame(Duration.seconds(timeStep + 1), new KeyValue(seconds, 0)));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.playFromStart();
	}

}