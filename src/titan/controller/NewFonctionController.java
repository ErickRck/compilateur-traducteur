package titan.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import titan.constants.ClassKind;
import titan.constants.Extension;
import titan.ui.DialogUtils;
import titan.utils.ClassManager;
import titan.utils.FileManager;
import titan.utils.Intent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewFonctionController implements Initializable {

    @FXML
    private TextField fonctionNameText;
    @FXML private ComboBox<String> kindComboBox;
    @FXML private Button createButton;
    @FXML private Button cancelButton;

    @FXML static NewFonctionController fonctionController;

    private String directoryPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fonctionController = this;
        bindComboBoxTypes();
        getValuesFromMainView();

        createButton.setOnAction(e -> createButtonAction());
        cancelButton.setOnAction(e -> cancelButtonAction());

    }

    private void getValuesFromMainView() {
        Intent intent = Intent.getIntent();
        directoryPath = intent.getStringValue("CLASS_PATH","");
    }

    private void bindComboBoxTypes() {
        kindComboBox.getItems().addAll("Class", "Interface", "Enum", "Annotation");
        kindComboBox.getSelectionModel().select(0);
    }

    private void createButtonAction() {
        String classType = kindComboBox.getSelectionModel().getSelectedItem();
        String className = fonctionNameText.getText();

        if(className.isEmpty()){
            String errorMessage = "Insert Fonction name first";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG,null,errorMessage);
            return;
        }

        String fullClassName = (className.endsWith(Extension.JAVA)) ? className : className.concat(Extension.JAVA);
        String newClassPath = directoryPath.concat(File.separator).concat(fullClassName);

        File source = FileManager.createNewFile(newClassPath);
        ClassKind classKind = ClassManager.getClassKind(classType);
        FileManager.updateContent(source, ClassManager.getDefaultValueText(className, classKind));

        cancelButtonAction();
    }

    private void cancelButtonAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public static NewFonctionController getInstance(){
        return fonctionController;
    }
}
