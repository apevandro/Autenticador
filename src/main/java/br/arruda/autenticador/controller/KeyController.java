package br.arruda.autenticador.controller;

import static br.arruda.autenticador.service.file.FileService.SIZE;

import java.io.IOException;
import java.util.Iterator;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import br.arruda.autenticador.service.key.KeyService;
import br.arruda.autenticador.service.message.MessageService;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Controller
@RequiredArgsConstructor
public class KeyController {

	@NonNull private final ConfigurableApplicationContext springContext;
	@NonNull private final KeyService keyService;
	@NonNull private final MessageService messageService;

	@Setter private MainController mainController;

	@FXML
	private GridPane grid;

	@FXML
	private Button selecionar0;

	@FXML
	private Button selecionar1;

	@FXML
	private Button selecionar2;

	@FXML
	private Button cancelar;

	private BorderPane rootPane;
	private Button[] arrayButtons;

	private final String KEY_PANE = "/fxml/Key.fxml";
	private final String MAIN_PANE = "/fxml/Main.fxml";

	public void init(BorderPane rootPane) throws IOException {
		if (keyService.isEmpty()) {
			showEmptyKeyInformation();
			return;
		}

		this.rootPane = rootPane;
		showPane(KEY_PANE);

		int pos = 0;
		Iterator<String> iterator = keyService.getDatasDeAcesso().iterator();
		while (iterator.hasNext()) {
			Label label = new Label();
			label.setText(iterator.next());
			grid.add(label, 0, pos++);
		}

		pos = 0;
		iterator = keyService.getChaves().iterator();
		while (iterator.hasNext()) {
			Label label = new Label();
			label.setText(iterator.next());
			grid.add(label, 1, pos++);
		}

		initButtons();
		for (int i = 0; i < keyService.getChaves().size(); i++) {
			arrayButtons[i].setVisible(true);
		}
	}

	@FXML
	private void selecionar0(final Event event) throws IOException {
		selecionar(0);
	}

	@FXML
	private void selecionar1(final Event event) throws IOException {
		selecionar(1);
	}

	@FXML
	private void selecionar2(final Event event) throws IOException {
		selecionar(2);
	}

	private void selecionar(final int index) throws IOException {
		keyService.selecionarChave(index);
		showPane(MAIN_PANE);
		mainController.startOver();
	}

	@FXML
	private void cancelar(final Event event) throws IOException {
		showPane(MAIN_PANE);
		mainController.startOver();
	}

	private void showPane(String pane) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(pane));
		fxmlLoader.setControllerFactory(springContext::getBean);
		BorderPane borderPane = fxmlLoader.load();
		rootPane.getChildren().setAll(borderPane);
	}

	private void initButtons() {
		arrayButtons = new Button[SIZE];
		arrayButtons[0] = selecionar0;
		arrayButtons[1] = selecionar1;
		arrayButtons[2] = selecionar2;
	}

	private void showEmptyKeyInformation() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText(messageService.getMessage("chave.ausente"));
		alert.showAndWait();
	}

}