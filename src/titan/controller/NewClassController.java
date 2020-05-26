package titan.controller;

import titan.constants.ClassKind;
import titan.constants.Extension;
import titan.ui.DialogUtils;
import titan.utils.ClassManager;
import titan.utils.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import titan.utils.Intent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewClassController implements Initializable {

    @FXML private TextField classNameText;
    @FXML private ComboBox<String> kindComboBox;
    @FXML private Button createButton;
    @FXML private Button cancelButton;
    @FXML static NewClassController fonctionController;

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
        kindComboBox.getItems().addAll("Pascal", "Algorithme");
        kindComboBox.getSelectionModel().select(0);
    }

    private void createButtonAction() {
        String classType = kindComboBox.getSelectionModel().getSelectedItem();
        String className = classNameText.getText();

        if(className.isEmpty()){
            String errorMessage = "Insert Fonction name first";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG,null,errorMessage);
            return;
        }
        else if (classType.equals("Algorithme")){
            String fullClassName = (className.endsWith(Extension.ALGO)) ? className : className.concat(Extension.ALGO);
            String newClassPath = directoryPath.concat(File.separator).concat(fullClassName);

            File source = FileManager.createNewFile(newClassPath);
            ClassKind classKind = ClassManager.getClassKind(classType);
            FileManager.updateContent(source, ClassManager.getDefaultValueText(className, classKind));
            MainController.getInstance().openTextInTab(source);

        }else {
            String fullClassName = (className.endsWith(Extension.PASCAL)) ? className : className.concat(Extension.PASCAL);
            String newClassPath = directoryPath.concat(File.separator).concat(fullClassName);

            File source = FileManager.createNewFile(newClassPath);
            ClassKind classKind = ClassManager.getClassKind(classType);
            FileManager.updateContent(source, ClassManager.getDefaultValueText(className, classKind));
            MainController.getInstance().openTextInTab(source);
        }

        cancelButtonAction();

    }

    private void cancelButtonAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public static NewClassController getInstance(){
        return fonctionController;
    }
}
