package titan.controller;

import javafx.event.ActionEvent;
import titan.analysis.AnalyseLexicale.Categorie;
import titan.analysis.AnalyseLexicale.Scanner;
import titan.analysis.AnalyseLexicale.UniteLexicale;
import titan.analysis.AnalyseSyntaxique.Production;
import titan.analysis.Traducteur.Cible;
import titan.analysis1.AnalyseLexicale.*;
import titan.constants.Extension;
import titan.constants.Icons;
import titan.model.Source;
import titan.service.FileService;
import titan.service.ProjectWatcher;
import titan.ui.DialogUtils;
import titan.utils.*;
import com.google.common.io.Files;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.io.IOException;


import java.net.URL;

import java.nio.charset.Charset;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class MainController implements Initializable {



    public String codeExec;



    //FX Views
    @FXML private TextArea resultTextArea;
    @FXML private TabPane codeAreaLayout;
    @FXML private SplitPane mainSplitPane;
    @FXML private SplitPane codeSplitPane;

    //FX Views Group

    @FXML private TreeView<Source> filesTreeView;

    //New Menu Items
    @FXML private MenuItem newFileMenuItem;
    @FXML private MenuItem newClassMenuItem1;
    @FXML private MenuItem newProjectMenuItem;

    //File Menu Items
    @FXML private MenuItem openFileMenuItem;
    @FXML private MenuItem openFolderMenuItem;
    @FXML private MenuItem closeMenuItem;
    @FXML private MenuItem exitMenuItem;

    //View Menu Items
    @FXML private MenuItem showFilesMenuAction;
    @FXML private MenuItem showResultMenuAction;

    //Tool Bar
    @FXML private ComboBox<String> LanguageComboBox1;
    @FXML private ComboBox<String> LanguageComboBox;
    @FXML private Button btn_executor;
    @FXML private Button btn_open_folder;
    @FXML private Button btn_save_edit_file;

    //Controllers
    @FXML static MainController mainController;

    private CodeArea currentCodeArea;

    private static Logger debugger;
    private ProjectWatcher projectWatcherService;

    private ExecutorService executorService;

    private boolean isFilesPaneVisible = true;
    private boolean isResultPaneVisible = true;


    private static final int FILE_PANE_INDEX = 0;
    private static final int RESULT_PANE_INDEX = 0;
    private static final int NOT_FOUND_INDEX = -1;


    private static final String DEBUG_TAG = MainController.class.getSimpleName();
    private static final int THREAD_AVAILABLE_NUMBER = Runtime.getRuntime().availableProcessors();
    private final Image PROJECT_DIR = new Image(getClass().getResourceAsStream("/titan/res/icons/folder/main_folder.png"));

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainController = this;
        executorService = Executors.newFixedThreadPool(THREAD_AVAILABLE_NUMBER);
        debugger = Logger.getLogger(DEBUG_TAG);
        //comboBoxSettings();
        onMenuItemsActions();
       // openedListSettings();
        LanguageComboBoxTypes();


        resultTextArea.setEditable(false);
        btn_save_edit_file.setDisable(true);
        btn_executor.setDisable(true);

        codeAreaLayout.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);
        //codeAreaLayout.setOnDragDropped(this::onCodeLayoutDragDropped);
        codeAreaLayout.setOnDragOver(this::onCodeLayoutDragOver);
        codeAreaLayout.getSelectionModel().selectedItemProperty().addListener(onTabSelectChangeListener);

        filesTreeView.getSelectionModel().selectedItemProperty().addListener(onFileSelectChangeListener);
    }

    private void onMenuItemsActions() {
        //File Menu
        openFileMenuItem.setOnAction(event -> onOpenFileMenuAction());
        openFolderMenuItem.setOnAction(event -> onOpenFolderMenuAction());
        closeMenuItem.setOnAction(event -> onCloseMenuAction());
        exitMenuItem.setOnAction(event -> onExitMenuAction());

        //New SubMenu
        newFileMenuItem.setOnAction(event -> showNewFileDialog());
        newClassMenuItem1.setOnAction(event -> showNewClassDialog());
        newProjectMenuItem.setOnAction(event -> showProjectFileDialog());

        //View Menu
        showFilesMenuAction.setOnAction(event -> onShowFilesMenuAction());
        showResultMenuAction.setOnAction(event -> onShowResultMenuAction());
    }

    private void showNewFileDialog() {
        if (Objects.nonNull(filesTreeView.getTreeItem(0))) {
            int directoryIndex = filesTreeView.getSelectionModel().getSelectedIndex();
            if (directoryIndex != NOT_FOUND_INDEX) {
                TreeItem<Source> directory = filesTreeView.getSelectionModel().getSelectedItem();
                Source dirSource = directory.getValue();
                File dirFile = dirSource.getFile();
                String dirPath = "";
                if (dirFile.isDirectory() && dirFile.canWrite()) {
                    dirPath = dirFile.getPath();
                } else if (dirFile.getParentFile().canWrite()) {
                    dirPath = dirFile.getParentFile().getPath();
                }
                Intent intent = Intent.getIntent();
                intent.addStringValue("CLASS_PATH", dirPath);
                String title = "New File";
                String viewPath = "/titan/views/new_file.fxml";
                String stylePath = "/titan/styles/new_create_style.css";
                intent.showAnotherView(viewPath, title, stylePath);
            } else {
                String warnMessage = "Please Select Directory First";
                //Debugging warning
                debugger.warning(warnMessage);
                //UI warning
                DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, warnMessage);
            }
        } else {
            String warnMessage = "Please Create or Open Project First";
            //Debugging warning
            debugger.warning(warnMessage);
            //UI warning
            DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, warnMessage);
        }
    }

    private void showNewClassDialog() {
        if (Objects.nonNull(filesTreeView.getTreeItem(0))) {
            int directoryIndex = filesTreeView.getSelectionModel().getSelectedIndex();
            if (directoryIndex != NOT_FOUND_INDEX) {
                TreeItem<Source> directory = filesTreeView.getSelectionModel().getSelectedItem();
                Source dirSource = directory.getValue();
                File dirFile = dirSource.getFile();
                String dirPath = "";
                if (dirFile.isDirectory() && dirFile.canWrite()) {
                    dirPath = dirFile.getPath();
                } else if (dirFile.getParentFile().canWrite()) {
                    dirPath = dirFile.getParentFile().getPath();
                }
                Intent intent = Intent.getIntent();
                intent.addStringValue("CLASS_PATH", dirPath);
                String title = "New Fonction";
                String viewPath = "/titan/views/new_class.fxml";
                String stylePath = "/titan/styles/new_create_style.css";
                intent.showAnotherView(viewPath, title, stylePath);
            } else {
                String warnMessage = "Please Select Directory First";
                //Debugging warning
                debugger.warning(warnMessage);
                //UI warning
                DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, warnMessage);
            }
        } else {
            String warnMessage = "Please Create or Open Project First";
            //Debugging warning
            debugger.warning(warnMessage);
            //UI warning
            DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, warnMessage);
        }
    }

    private void showProjectFileDialog() {
        String title = "Create New Project";
        String viewPath = "/titan/views/new_project.fxml";
        String stylePath = "/titan/styles/new_create_style.css";

        Intent intent = Intent.getIntent();
        intent.showAnotherView(viewPath, title, stylePath);
    }

    private void onCodeLayoutDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.MOVE);
        }
    }

    private void onOpenFileMenuAction() {
        File outputFile = FileManager.openSourceFile("Open File");
        if (outputFile != null)
            if (outputFile.getName().endsWith(Extension.JAVA))
                executorService.execute(() -> openSourceInTab(outputFile));
            else if (outputFile.getName().endsWith(Extension.TEXT))
                executorService.execute(() -> openTextInTab(outputFile));
            else if (outputFile.getName().endsWith(Extension.MD))
                executorService.execute(() -> openTextInTab(outputFile));
            else if (outputFile.getName().endsWith(Extension.PASCAL))
                executorService.execute(() -> openTextInTab(outputFile));
            else if (outputFile.getName().endsWith(Extension.ALGO))
                executorService.execute(() -> openTextInTab(outputFile));
    }

    private void onOpenFolderMenuAction() {
        File sourceFolder = FileManager.openSourceDir("Open Project Folder");
        if (Objects.nonNull(sourceFolder)) updateFilesTreeView(sourceFolder);
    }

    /**
     * appel la mathode d'ouverture d'un dossier
     * @param event
     */
    public void OpenFolder(ActionEvent event){
        onOpenFolderMenuAction();
    }
    private void onCloseMenuAction() {
        codeAreaLayout.getTabs().clear();
        resultTextArea.clear();
        filesTreeView.setRoot(null);
        if (Objects.nonNull(projectWatcherService)) projectWatcherService.stopWatcher();
    }

    private void onExitMenuAction() {
        Platform.exit();
        System.exit(0);
    }

    //TODO : Issue when User Change Titan IDE Size
    private void onShowFilesMenuAction() {
        if (isFilesPaneVisible) {
            mainSplitPane.setDividerPosition(FILE_PANE_INDEX, 0);
            showResultMenuAction.setText("Show Output");
            isFilesPaneVisible = false;
        } else {
            mainSplitPane.setDividerPosition(FILE_PANE_INDEX, .18);
            showResultMenuAction.setText("Hide Files");
            isFilesPaneVisible = true;
        }
    }

    /**
     * action sur le bouton d"execution du programme
     * dans cette methode on fait un test du langage qui sera interperété soit "algo" soit "pascal"
     * @param event
     */
    public void ExecutorCode(ActionEvent event){

        String LanguageSelect = LanguageComboBox.getSelectionModel().getSelectedItem();
        if (LanguageSelect=="Algorithmique"){
            Scanner anaLex=new Scanner(codeExec);
            //Scanner anaLex=new Scanner("test2.txt");
            Production parser=new Production();
            Cible cib=new Cible();
            parser.lireProduction();
            parser.setNullable();
            parser.Premiercalcul();
            parser.calculSuivant();
            parser.calculPremierRegles();
            parser.remplirTableAnalyse();
            parser.afficher();
            parser.initialiserPile();
            System.out.println(anaLex);
            UniteLexicale ul=null;
            String valeurC=null;
            int i=0;
            boolean b=true;
            while(b){
                ul=anaLex.lexemeSuivant();

                if(ul.getulmc().equals(Categorie.EOF)){
                    System.out.println(" l'élément avant d'entree dans le parser est :"+ul.getulmc());
                    anaLex.lexemeSuivant();
                    System.out.println("affiche les éléments de ul rencontré");
                    System.out.println("le fichier touche à sa fin et la syntaxe est correcte !!");
                    b=false;
                }
                else	{
                    //System.out.println(" l'élément avant d'entree dans le parser est :"+ul.getulmc());
                    parser.analyserSyntaxe(ul.getulmc());
                    System.out.println("affiche les éléments de ul rencontrés !!");
                    System.out.println(ul.getulmc());
                    //System.out.println("c'est le tours de lexeme"+ul.getlexem());

                    cib.tablelexmc(ul.getlexem().toString());

                }
            }

            cib.cl_valeur();
            valeurC=cib.cpc();
            cib.affichage("affiche ça aussi  ");
            System.out.println("l'équivalent de l'algo en c est : ");
            System.out.println("............................................ ");
            System.out.println(valeurC);

            resultTextArea.setText(valeurC);

        }else {
            System.out.println("Pascal*********************************************Pascal");
            if (LanguageSelect=="Pascal"){
                Scanners anaLex=new Scanners(codeExec);
                Parsers parser=new Parsers();
                cible cib=new cible();
                parser.lireProduction();
                parser.setNullable();
                parser.Premiercalcul();
                parser.calculSuivant();
                parser.calculPremierRegles();
                parser.remplirTableAnalyse();
                parser.afficher();
                parser.initialiserPile();
                System.out.println(anaLex);
                Lexique ul=null;
                String valeurC=null;
                int i=0;
                boolean b=true;
                while(b){
                    ul=anaLex.lexemeSuivant();

                    if(ul.getulmc().equals(UnitesLexicalesMotsCles.EOF)){
                        System.out.println(" l'element avant d'entree dans le parser est :"+ul.getulmc());
                        anaLex.lexemeSuivant();
                        System.out.println("affiche les elements de ul rencontre");
                        System.out.println("le fichier touche a sa fin et la syntaxe est correcte !!");
                        b=false;
                    }
                    else	{
                        //System.out.println(" l'�l�ment avant d'entree dans le parser est :"+ul.getulmc());
                        parser.analyserSyntaxe(ul.getulmc());
                        System.out.println("affiche les elements de ul rencontres 1");
                        System.out.println(ul.getulmc());
                        //System.out.println("c'est le tours de lexeme"+ul.getlexem());

                        cib.tablelexmc(ul.getlexem().toString());

                    }
                }

                cib.cl_valeur();
                valeurC=cib.cpc();
                cib.affichage("affiche sa aussi  ");
                System.out.println("l'equivalent pascal en c est : ");
                System.out.println("............................................ ");
                System.out.println(valeurC);

                resultTextArea.setText(valeurC);
            }
        }


    }

    private void LanguageComboBoxTypes() {
        LanguageComboBox.getItems().addAll("Pascal","Algorithmique");
        LanguageComboBox.getSelectionModel().select(0);
        LanguageComboBox1.getItems().addAll("C");
        LanguageComboBox1.getSelectionModel().select(0);

    }

    //TODO : Issue when User Change Titan IDE Size
    private void onShowResultMenuAction() {
        if (isResultPaneVisible) {
            codeSplitPane.setDividerPosition(RESULT_PANE_INDEX, 1);
            showResultMenuAction.setText("Show Output");
            isResultPaneVisible = false;
        } else {
            codeSplitPane.setDividerPosition(RESULT_PANE_INDEX, .82);
            showResultMenuAction.setText("Hide Output");
            isResultPaneVisible = true;
        }
    }
/*
    private void openSourceInTab(File sourceFile) {
        Tab javaTab = new Tab(sourceFile.getName());
        javaTab.setUserData(sourceFile.getPath());
       // javaTab.setOnClosed(event -> onTabCloseAction(javaTab));
        javaTab.setGraphic(ImageUtils.buildImageView(Icons.codeIconImage));

        CodeArea codeTextArea = new CodeArea();
        EditorController editorController = new EditorController(codeTextArea, resultTextArea);
        editorController.editorSettings();

        try {
            StringBuilder code = new StringBuilder();
            Files.readLines(sourceFile, Charset.defaultCharset()).forEach(s -> code.append(s).append("\n"));
            codeTextArea.replaceText(0, 0, code.toString());
            javaTab.setContent(new VirtualizedScrollPane<>(codeTextArea));
            //Update On UI Thread
            Platform.runLater(() -> {
                codeAreaLayout.getTabs().add(javaTab);
                //openedFilesList.getItems().add(new Source(sourceFile));
            });
            editorController.updateSourceFile(sourceFile);
        } catch (IOException e) {
            String warnMessage = "Can't Open File in Tab pane";
            //Debugging warning
            debugger.warning(warnMessage);
            //UI warning
            DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, warnMessage);
        }
    }
*/
private void openSourceInTab(File sourceFile) {
    Tab sourceTab = new Tab(sourceFile.getName());
    sourceTab.setUserData(sourceFile.getPath());
    sourceTab.setGraphic(ImageUtils.buildImageView(Icons.codeIconImage));

    CodeArea codeTextArea = new CodeArea();
    EditorController editorController = new EditorController(codeTextArea, resultTextArea);
    editorController.editorSettings();

    try {
        StringBuilder code = new StringBuilder();
        Files.readLines(sourceFile, Charset.defaultCharset()).forEach(s -> code.append(s).append("\n"));
        codeTextArea.replaceText(0, 0, code.toString());
        sourceTab.setContent(new VirtualizedScrollPane<>(codeTextArea));
        //Update On UI Thread
        Platform.runLater(() -> {
            codeAreaLayout.getTabs().add(sourceTab);
            //openedFilesList.getItems().add(new Source(sourceFile));

        });
        editorController.updateSourceFile(sourceFile);

    } catch (IOException e) {
        String warnMessage = "Can't Open File in Tab pane";
        //Debugging warning
        debugger.warning(warnMessage);
        //UI warning
        DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, warnMessage);
    }
}

     public void openTextInTab(File textFile) {
        Tab textTab = new Tab(textFile.getName());
        textTab.setUserData(textFile.getPath());
        //textTab.setOnClosed(event -> onTabCloseAction(textTab));
        textTab.setGraphic(ImageUtils.buildImageView(Icons.textIconImage));

        CodeArea textArea = new CodeArea();
        EditorController editorController = new EditorController(textArea, resultTextArea);
        editorController.editorSettings();

        try {
            StringBuilder code = new StringBuilder();
            Files.readLines(textFile, Charset.defaultCharset()).forEach(s -> code.append(s).append("\n"));
            textArea.replaceText(0, 0, code.toString());
            textTab.setContent(textArea);
            //Update On UI Thread
            Platform.runLater(() -> {
                codeAreaLayout.getTabs().add(textTab);
                //openedFilesList.getItems().add(new Source(textFile));

                btn_save_edit_file.setDisable(false);
                btn_executor.setDisable(false);
                    codeExec=(textFile.getPath());

                    System.out.println(codeExec);


            });
            editorController.updateSourceFile(textFile);
        } catch (IOException e) {
            String warnMessage = "Can't Open File in Tab pane";
            //Debugging warning
            debugger.warning(warnMessage);
            //UI warning
            DialogUtils.createWarningDialog(DialogUtils.WARNING_DIALOG, null, warnMessage);
        }
    }
    private ChangeListener<Tab> onTabSelectChangeListener = (observable, oldValue, newValue) -> {
        if (Objects.nonNull(newValue)) {
            if (newValue.getText().endsWith(Extension.JAVA)) {
                currentCodeArea = (CodeArea) ((Parent) newValue.getContent()).getChildrenUnmodifiable().get(0);
            }
        }
    };

    //TODO : Make Class to make easy to support all types of files extension
    private ChangeListener<TreeItem> onFileSelectChangeListener = (observable, oldValue, newValue) -> {
        if (Objects.nonNull(newValue)) {
            String fileName = newValue.getValue().toString();
            if (fileName.endsWith(Extension.JAVA)) {
                Source javaSource = (Source) newValue.getValue();
                openSourceInTab(javaSource.getFile());
            } else if (fileName.endsWith(Extension.TEXT)) {
                Source textSource = (Source) newValue.getValue();
                openTextInTab(textSource.getFile());
            } else if (fileName.endsWith(Extension.MD)) {
                Source textSource = (Source) newValue.getValue();
                openTextInTab(textSource.getFile());
            }else if (fileName.endsWith(Extension.PASCAL)) {
                Source textSource = (Source) newValue.getValue();
                openTextInTab(textSource.getFile());
            }else if (fileName.endsWith(Extension.ALGO)) {
                Source textSource = (Source) newValue.getValue();
                openTextInTab(textSource.getFile());
            }
        }
    };

    public  void SaveEditFile(ActionEvent event){

    }

    void updateFilesTreeView(File sourceFolder) {
        if (Objects.nonNull(sourceFolder)) {
            if (projectWatcherService != null) {
                projectWatcherService.stopWatcher();
            }
            FileCrawler crawler = new FileCrawler();
            TreeItem<Source> sourceTreeItem = crawler.getFilesForDirectory(sourceFolder);
            sourceTreeItem.setGraphic(new ImageView(PROJECT_DIR));
            executorService.submit(() -> Platform.runLater(() -> filesTreeView.setRoot(sourceTreeItem)));
            projectWatcherService = FileService.setFileService(filesTreeView, sourceFolder);
        }
    }

    public static MainController getInstance() {
        return mainController;
    }
}
