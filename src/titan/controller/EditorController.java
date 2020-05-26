package titan.controller;


import com.google.googlejavaformat.FormatterDiagnostic;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import titan.syntax.PunctuationComplete;
import titan.syntax.SpaceSensors;
import titan.syntax.SyntaxUtils;
import titan.ui.DialogUtils;
import titan.utils.DesktopUtils;
import titan.utils.FileManager;

import java.io.File;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntFunction;
import java.util.regex.Matcher;

class EditorController {

    private CodeArea codeTextArea;
    private TextArea resultTextArea;

    private File sourceFile;
    private ExecutorService taskExecutor;

    EditorController(CodeArea codeArea, TextArea resultArea) {
        taskExecutor = Executors.newSingleThreadExecutor();
        codeTextArea = codeArea;
        resultTextArea = resultArea;
    }

    void editorSettings() {
        codeAreaHighlighter();
        codeAreaAddLineNumber();
        bindContextMenu();

        //Event Listener
        codeTextArea.setOnKeyPressed(onSpaceSensors);
        codeTextArea.setOnKeyReleased(onKeyReleased);

    }

    private void codeAreaHighlighter() {
        codeTextArea.multiPlainChanges()
                .successionEnds(Duration.ofMillis(500))
                .supplyTask(this::computeHighlightingAsync)
                .awaitLatest(codeTextArea.multiPlainChanges())
                .filterMap(tryTask -> {
                    if (tryTask.isSuccess()) {
                        return Optional.of(tryTask.get());
                    } else {
                        tryTask.getFailure().printStackTrace();
                        return Optional.empty();
                    }
                })
                .subscribe(this::applyHighlighting);
    }

    private void codeAreaAddLineNumber() {
        IntFunction<Node> lineNumberFactory = LineNumberFactory.get(codeTextArea);
        codeTextArea.setParagraphGraphicFactory(lineNumberFactory);
    }

    private ContextMenu getContextMenu() {
        //Create Menu Items
        MenuItem run = new MenuItem("Run");
        MenuItem format = new MenuItem("Format");
        MenuItem save = new MenuItem("Save");
        MenuItem openFolder = new MenuItem("Show in Explorer");
        MenuItem openTerminal = new MenuItem("Open Terminal");
        MenuItem openTerminalHere = new MenuItem("Open Terminal Here");

        //Add Event Handler
        format.setOnAction(this::formatSourceCode);
        save.setOnAction(this::saveSourceCode);
        openFolder.setOnAction(this::openCurrentFolder);


        //Add Menu items in Context Menu
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getItems().add(run);
        contextMenu.getItems().add(format);
        contextMenu.getItems().add(save);
        contextMenu.getItems().add(openFolder);
        contextMenu.getItems().add(openTerminal);
        contextMenu.getItems().add(openTerminalHere);

        
        return contextMenu;
    }

    private void bindContextMenu(){
        ContextMenu contextMenu = getContextMenu();
        codeTextArea.setContextMenu(contextMenu);
    }

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeTextArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() {
                return computeHighlighting(text);
            }
        };
        taskExecutor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeTextArea.setStyleSpans(0, highlighting);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = SyntaxUtils.PATTERN.matcher(text);
        int lastKeywordEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();

        while (matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("PAREN") != null ? "paren" :
                    matcher.group("BRACE") != null ? "brace" :
                    matcher.group("BRACKET") != null ? "bracket" :
                    matcher.group("SEMICOLON") != null ? "semicolon" :
                    matcher.group("CAST") != null ? "cast" :
                    matcher.group("STRING") != null ? "string" :
                    matcher.group("TODO") != null ? "todo" :
                    matcher.group("WARN") != null ? "warn" :
                    matcher.group("COMMENT") != null ? "comment" :
                    matcher.group("ANNOTATION") != null ? "annotation" :
                    matcher.group("OPERATION") != null ? "operation" :
                    matcher.group("HEX") != null ? "hex" :
                    matcher.group("NUMBER") != null ? "number" :
                    matcher.group("METHOD") != null ? "method" :
                    null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKeywordEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKeywordEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKeywordEnd);
        return spansBuilder.create();
    }

    void updateSourceFile(File source) {
        sourceFile = source;
    }

    private void formatSourceCode(ActionEvent event) {
        resultTextArea.clear();
        try {
            String formattedSource = new Formatter().formatSource(codeTextArea.getText());
            if (!codeTextArea.getText().equals(formattedSource)) {
                codeTextArea.replaceText(0, codeTextArea.getLength(), formattedSource);
            }
        } catch (FormatterException errors) {
            List<FormatterDiagnostic> errorList = errors.diagnostics();
            StringBuilder errorBuilder = new StringBuilder();
            for (FormatterDiagnostic err : errorList) {
                errorBuilder.append(err.toString()).append("\n");
            }
            resultTextArea.setText(errorBuilder.toString());
        }
    }

    private   void saveSourceCode(ActionEvent event) {
        String sourceCode = codeTextArea.getText();
        if (sourceFile.canWrite()) {
            FileManager.updateContent(sourceFile, sourceCode);
        } else {
            //If File is deleted re create file then update content again
            String confirmMessage = "Save Current File ?!";
            DialogUtils.createConfirmDialog(DialogUtils.CONFIRM_DIALOG,null,confirmMessage,()->{
                FileManager.createNewFile(sourceFile.getPath());
                FileManager.updateContent(sourceFile, sourceCode);
            });
        }
    }



    private void openCurrentFolder(ActionEvent event) {
        if (sourceFile != null)
            if (sourceFile.getParentFile() != null)
                DesktopUtils.openFolderExplore(sourceFile.getParentFile());
    }


    private final EventHandler<KeyEvent> onKeyReleased = event -> {
        if (!event.getCode().equals(KeyCode.SHIFT) &&
                !event.getCode().equals(KeyCode.ALT) &&
                !event.getCode().equals(KeyCode.CONTROL) &&
                !event.getCode().equals(KeyCode.TAB) &&
                !event.getCode().equals(KeyCode.CAPS) &&
                !event.getCode().equals(KeyCode.BACK_SPACE) &&
                !event.getCode().isArrowKey()) {
            int cursorPosition = codeTextArea.getCaretPosition();
            if (cursorPosition > 0) {
                char bracket = codeTextArea.getText(cursorPosition - 1, cursorPosition).charAt(0);
                PunctuationComplete.onPunctuationComplete(codeTextArea, bracket, cursorPosition);
            }
        }
    };

    private final EventHandler<KeyEvent> onSpaceSensors = event -> {
        if (event.getCode().equals(KeyCode.ENTER)) {
            SpaceSensors.onWhiteSpaceSensors(codeTextArea);
        }
    };

}
