package cc.takacs.php_codeverage_display.display;

import cc.takacs.php_codeverage_display.clover.FileCoverage;
import cc.takacs.php_codeverage_display.clover.LineCoverage;
import cc.takacs.php_codeverage_display.config.ConfigValues;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.editor.markup.*;

import java.awt.*;
import java.util.ArrayList;

/**
 * @author Zsolt Takacs <zsolt@takacs.cc>
 */
public class CoverageDisplay implements DocumentListener {
    private Editor editor;
    private FileCoverage fileCoverage;
    private ArrayList<RangeHighlighter> highlights;

    public CoverageDisplay(Editor editor) {
        this.editor = editor;
        fileCoverage = new FileCoverage();
        highlights = new ArrayList<RangeHighlighter>();
    }

    public void setFileCoverage(FileCoverage fileCoverage) {
        this.fileCoverage = fileCoverage;
    }

    public void beforeDocumentChange(DocumentEvent event) {
        clear();
    }

    public void documentChanged(DocumentEvent event) {
    }

    public synchronized void redraw() {
        clear();

        Document document = this.editor.getDocument();

        for (int lineNumber : fileCoverage.getKeys()) {
            LineCoverage lineCoverage = fileCoverage.getLine(lineNumber);

            if (lineCoverage.isExecuted()) {
                highlightLine(document, ConfigValues.getInstance().getCoveredColor(), lineNumber, lineCoverage.getExecuted());
            } else {
                highlightLine(document, ConfigValues.getInstance().getUncoveredColor(), lineNumber, lineCoverage.getExecuted());
            }
        }
    }

    private void highlightLine(Document document, final Color color, int line, int executed) {
        SideHighlighter sideHighlighter = new SideHighlighter();
        LineHighlighter lineHighlighter = new LineHighlighter();
        ErrorStripeMarkHighlighter errorStripeMarkHighlighter = new ErrorStripeMarkHighlighter();

        if (line <= document.getLineCount()) {
            TextAttributes attributes = new TextAttributes();

            RangeHighlighter highlighter = createRangeHighlighter(document, line, attributes);

            if (ConfigValues.getInstance().highlightLines) {
                lineHighlighter.highlight(highlighter, attributes, color, executed);
            }

            if (ConfigValues.getInstance().highlightSides) {
                sideHighlighter.highlight(highlighter, attributes, color, executed);
            }

            errorStripeMarkHighlighter.highlight(highlighter, attributes, color, executed);

            highlights.add(highlighter);
        }
    }

    private RangeHighlighter createRangeHighlighter(Document document, int line, TextAttributes attributes) {
        int lineStartOffset = document.getLineStartOffset(line - 1);
        int lineEndOffset = document.getLineEndOffset(line - 1);


        return this.editor.getMarkupModel().addRangeHighlighter(
                lineStartOffset, lineEndOffset, 3333, attributes, HighlighterTargetArea.LINES_IN_RANGE
        );
    }

    private void clear() {
        MarkupModel model = editor.getMarkupModel();

        for (RangeHighlighter rangeHighlighter : highlights) {
            model.removeHighlighter(rangeHighlighter);
        }

        highlights.clear();
    }
}