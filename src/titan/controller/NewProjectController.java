package titan.controller;

import titan.ui.DialogUtils;
import titan.utils.FileManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class NewProjectController implements Initializable {

    @FXML private TextField projectNameText;
    @FXML private TextField projectPathText;

    @FXML private Button findPathButton;
    @FXML private Button createButton;
    @FXML private Button cancelButton;

    @FXML static NewProjectController projectController;

    private File projectPathFile;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        projectController = this;
        findPathButton.setOnAction(e->setProjectPathText());
        createButton.setOnAction(e->createNewProject());
        cancelButton.setOnAction(e->cancelButtonAction());
    }

    private void setProjectPathText(){
        projectPathFile = FileManager.openSourceDir("Select Project Path");
        if(Objects.nonNull(projectPathFile))projectPathText.setText(projectPathFile.getPath());
    }

    private void createNewProject(){
        String projectName = projectNameText.getText().trim();
        String projectPath = projectPathText.getText().trim();

        boolean isProjectNameInvalid = projectName.isEmpty();
        boolean isProjectPathInvalid = !(projectPath.isEmpty() || projectPathFile.canWrite());

        if(isProjectNameInvalid && isProjectPathInvalid){
            String errorMessage = "Invalid Project Name and Path";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG,null,errorMessage);
            return;
        }else if(isProjectNameInvalid){
            String errorMessage = "Invalid Project Name";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG,null,errorMessage);
            return;
        }else if(isProjectPathInvalid){
            String errorMessage = "Invalid Project Path";
            DialogUtils.createErrorDialog(DialogUtils.ERROR_DIALOG,null,errorMessage);
            return;
        }

        String projectFullPath = projectPath.concat(File.separator).concat(projectName);
        String projectSrcPath = projectPath.concat(File.separator).concat(projectName).concat(File.separator).concat("src");
        String projectOutPath = projectPath.concat(File.separator).concat(projectName).concat(File.separator).concat("out");
        File projectDir = FileManager.createNewFolder(projectFullPath);
        File projectSrc = FileManager.createNewFolder(projectSrcPath);
        File projectOut = FileManager.createNewFolder(projectOutPath);
        System.out.println(projectDir+"**********************");
        System.out.println(projectSrc+"**********************");
        System.out.println(projectOut+"**********************");
        MainController.getInstance().updateFilesTreeView(projectDir);

        cancelButtonAction();
    }

    private void cancelButtonAction(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public static NewProjectController getInstance(){
        return projectController;
    }
}
