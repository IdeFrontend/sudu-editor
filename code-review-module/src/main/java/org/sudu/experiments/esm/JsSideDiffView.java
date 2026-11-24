package org.sudu.experiments.esm;

import org.sudu.experiments.js.JsArrayReader;
import org.teavm.jso.JSProperty;

public interface JsSideDiffView extends JsView {
  void setModel(JsITextDiffModel model);

  @JSProperty
  boolean getLeft();

  int getVScrollPos();
  void setVScrollPos(int vScrollPos);
  void setVScrollListener(JsIEditorView.VScrollListener listener);

  void highlightRanges(JsArrayReader<JsRange> ranges);
  void selectNextRange();
  void selectPrevRange();

  /** 1-based caret of this pane's document model. Shared by every pane on that model. */
  JsPosition getPosition();
}
