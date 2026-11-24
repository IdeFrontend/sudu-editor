package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EditorHighlightRevealTest {
  private static final int LINE_HEIGHT = 20;
  private static final int VIEW_HEIGHT = 100;

  @Test
  void selectNextRangeCentersEveryEditorAndKeepsTheCaretMoving() {
    String[] lines = new String[40];
    Arrays.fill(lines, "alpha");
    Model model = new Model(lines, Languages.TEXT, null);
    EditorComponent first = editor(model, LINE_HEIGHT, VIEW_HEIGHT);
    EditorComponent second = editor(model, LINE_HEIGHT, VIEW_HEIGHT);
    // Has not laid out a font. Must not throw or stop the others from scrolling.
    EditorComponent unpainted = editor(model, 0, VIEW_HEIGHT);

    Range near = new Range(8, 1, 8, 5);
    Range far = new Range(30, 1, 30, 4);
    model.highlightRanges = new Range[]{near, far};
    model.caretLine = 0;
    model.caretCharPos = 0;

    int[] pulls = {0};
    first.setVScrollObserver(pos -> {
      pulls[0]++;
      // A shorter partner clamps and writes the clamped value back, and also
      // onto the sibling that shares this model.
      first.setVScrollPos(0);
      second.setVScrollPos(0);
    });

    first.selectNextRange();

    assertEquals(near.startLineNumber, model.caretLine);
    assertEquals(near.startColumn, model.caretCharPos);
    assertEquals(near.startLineNumber, model.selection.startPos.line);
    assertEquals(near.startColumn, model.selection.startPos.charPos);
    assertEquals(near.endLineNumber, model.selection.endPos.line);
    assertEquals(near.endColumn, model.selection.endPos.charPos);
    int nearScroll = centeredScroll(near.startLineNumber);
    assertTrue(nearScroll > 0);
    assertEquals(nearScroll, first.getVScrollPos());
    assertEquals(nearScroll, second.getVScrollPos());
    assertEquals(0, unpainted.getVScrollPos());
    assertTrue(pulls[0] >= 1);

    // The caret selectAndReveal wrote is the start of `near`, so the next
    // call must move on to `far` rather than select `near` again.
    second.selectNextRange();

    assertEquals(far.startLineNumber, model.caretLine);
    assertEquals(far.startColumn, model.caretCharPos);
    assertEquals(far.startLineNumber, model.selection.startPos.line);
    assertEquals(far.endColumn, model.selection.endPos.charPos);
    int farScroll = centeredScroll(far.startLineNumber);
    assertTrue(farScroll > nearScroll);
    assertEquals(farScroll, first.getVScrollPos());
    assertEquals(farScroll, second.getVScrollPos());
  }

  /** Editor with no window. {@code lineHeight} must be positive before a reveal scrolls. */
  private static EditorComponent editor(Model model, int lineHeight, int viewHeight) {
    EditorComponent editor = new EditorComponent(null);
    editor.model = model;
    editor.docToView = new CodeLineMapping.Id(model);
    editor.lineHeight = lineHeight;
    editor.size.y = viewHeight;
    model.attachEditor(editor, null);
    return editor;
  }

  /** Same formula as {@code EditorComponent.revealLineInCenter}. */
  private static int centeredScroll(int viewLine) {
    return LINE_HEIGHT * (viewLine - (VIEW_HEIGHT / (LINE_HEIGHT * 2)) - 1);
  }
}
