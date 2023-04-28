package br.arruda.autenticador;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

@SpringBootApplication
public class AutenticadorApplication extends Application {

	private static final Logger logger = LoggerFactory.getLogger(AutenticadorApplication.class);

	private ConfigurableApplicationContext springContext;
	private Parent rootNode;
	private final String TITLE = "Autenticador";
	private final String FONT = "https://fonts.googleapis.com/css?family=Montserrat:bold";
	//private final String ICON = "/images/icone.png";
	private final String MAIN_PANE = "/fxml/Main.fxml";

	public static void main(final String[] args) {
		Application.launch(args);
	}

	@Override
	public void init() throws IOException {
		springContext = SpringApplication.run(AutenticadorApplication.class);
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(MAIN_PANE));
		fxmlLoader.setControllerFactory(springContext::getBean);
		rootNode = fxmlLoader.load();
	}

	@Override
	public void start(Stage stage) {
		//stage.getIcons().add(new Image(ICON));
		stage.setTitle(TITLE);
		Scene scene = new Scene(rootNode);
		scene.getStylesheets().add(FONT);
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void stop() {
		logger.info("Fechando aplicacao...");
		springContext.close();
	}

}