import * as editorApi from "../src/codereview.js";

const threadPool = await editorApi.newWorkerPool("../src/worker.js", 3);

const textL =
`package org.sudu.experiments.diff;

import org.sudu.experiments.WebGLError;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsCodeReview implements JsCodeReviewView {

  public final WebWindow window;
  private FileDiffWindow w;
  private JsFileDiffViewController controller;
  private IDiffSizeChangeCallback sizeListener;
  private int eventNumLines, eventLineHeight;
  private float eventCssLineHeight;

  public JsCodeReview(
      FileDiffWindow fileDiff, WebWindow ww,
      EditArgs args
  ) {
    this.window = ww;
    this.w = fileDiff;
    controller = new JsFileDiffViewController0(w);
    if (args.hasTheme()) setTheme(args.getTheme());
    if (args.hasReadonly())
      setReadonly(args.getReadonly(), args.getReadonly());

    w.rootView.setOnDocumentSizeChange(this::onDocumentSizeChange);
  }

  @Override
  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    JsHelper.consoleInfo("JsCodeDiff0.setReadonly");
    w.rootView.setReadonly(leftReadonly, rightReadonly);
  }

  @Override
  public void setTheme(JSObject theme) {
    var t = ThemeImport.fromJs(theme);
    if (t != null)
      w.applyTheme(t);
    window.repaint();
  }

  @Override
  public void setModel(JsITextModel modelL, JsITextModel modelR) {
    if (!(modelL instanceof JsTextModel jsModelL)) {
      boolean undefined = JSObjects.isUndefined(modelL);
      throw new IllegalArgumentException(
          undefined ? "left model is undefined" : "bad left model");
    }

    if (!(modelR instanceof JsTextModel jsModelR)) {
      boolean undefined = JSObjects.isUndefined(modelR);
      throw new IllegalArgumentException(
          undefined ? "right model is undefined" : "bad right model");
    }

    w.rootView.setModel(jsModelL.javaModel, jsModelR.javaModel);
  }

  @Override
  public JsITextModel getLeftModel() {
    return JsTextModel.fromJava(w.rootView.getLeftModel());
  }

  @Override
  public JsITextModel getRightModel() {
    return JsTextModel.fromJava(w.rootView.getRightModel());
  }
}
`;

const textR =
`package org.sudu.experiments.diff;

import org.sudu.experiments.WebGLError;
import org.sudu.experiments.WebWindow;
import org.sudu.experiments.esm.*;
import org.sudu.experiments.js.*;
import org.teavm.jso.JSObject;
import org.teavm.jso.core.JSObjects;
import org.teavm.jso.core.JSString;

public class JsCodeReview implements JsCodeReviewView {

  public final WebWindow window;
  private FileDiffWindow w;
  private JsFileDiffViewController controller;
  private IDiffSizeChangeCallback sizeListener;
  private int eventNumLines, eventLineHeight;
  private float eventCssLineHeight;

  public JsCodeReview(
      FileDiffWindow fileDiff, WebWindow ww,
      EditArgs args
  ) {
    this.window = ww;
    this.w = fileDiff;
    controller = new JsFileDiffViewController0(w);
    if (args.hasTheme()) setTheme(args.getTheme());
    if (args.hasReadonly())
      setReadonly(args.getReadonly(), args.getReadonly());

    w.rootView.setOnDocumentSizeChange(this::onDocumentSizeChange);
  }

  @Override
  public void setReadonly(boolean leftReadonly, boolean rightReadonly) {
    w.rootView.setReadonly(leftReadonly, rightReadonly);
  }

  @Override
  public void enableMergeButtons(boolean leftToRight, boolean rightToLeft) {
    w.rootView.enableMergeButtons(leftToRight, rightToLeft);
  }

  @Override
  public void setTheme(JSObject theme) {
    var t = ThemeImport.fromJs(theme);
    if (t != null)
      w.applyTheme(t);
    window.repaint();
  }

  @Override
  public void setModel(JsITextDiffModel model) {
    if (!(model instanceof JsTextDiffModel jsModel)) {
      boolean undefined = JSObjects.isUndefined(model);
      throw new IllegalArgumentException(undefined ? "Model is undefined" : "Bad model: " + JsHelper.stringify(model));
    }
    w.rootView.setModel(jsModel.javaModel);
  }

  @Override
  public JsITextModel getLeftModel() {
    return JsTextModel.fromJava(w.rootView.getLeftModel());
  }

  @Override
  public JsITextModel getRightModel() {
    return JsTextModel.fromJava(w.rootView.getRightModel());
  }
}
`;

const model = editorApi.newDiffModel(
    threadPool, textL, textR,
    "classL.java", "classR.java",
    "java"
);

const leftView1 = editorApi.newSideDiff({
    containerId: "left1",
    workers: threadPool,
    readonly: true
}, true);

const leftView2 = editorApi.newSideDiff({
    containerId: "left2",
    workers: threadPool,
    readonly: true
}, true);

const rightView1 = editorApi.newSideDiff({
    containerId: "right1",
    workers: threadPool,
    readonly: true
}, false);

const rightView2 = editorApi.newSideDiff({
    containerId: "right2",
    workers: threadPool,
    readonly: true
}, false);

leftView1.setModel(model);
rightView1.setModel(model);
leftView2.setModel(model);
rightView2.setModel(model);

// One-way for the duration of the callback. A partner with a shorter document
// clamps the position; writing that clamp back would pull the pane that just
// centered off its match.
let syncingScroll = false;
function syncTo(target) {
  return (pos) => {
    if (syncingScroll) return;
    syncingScroll = true;
    try {
      target.setVScrollPos(pos);
    } finally {
      syncingScroll = false;
    }
  };
}

leftView1.setVScrollListener(syncTo(rightView1));
leftView2.setVScrollListener(syncTo(rightView2));
rightView1.setVScrollListener(syncTo(leftView1));
rightView2.setVScrollListener(syncTo(leftView2));

leftView1.focus();

console.log("SideDiff left.left =", leftView1.left, "right.left =", rightView1.left);

// ---------------------------------------------------------------------------
// highlightRanges
//
// Each side of the diff is one document model. Both left panes share it, and
// both right panes share the other, so each side has one highlight list and
// one caret. The panes are readonly: SideDiffView has no getText, so an edit
// would leave these ranges pointing at text that is no longer in the document.
// One highlightRanges call per side paints every pane on that model.
// selectNextRange / selectPrevRange move that caret and scroll every pane
// attached to the model. Each pane's listener then pixel-scrolls its partner
// and does not select the partner's next match. The sync flag stops the
// partner from writing a clamped position back.
//
// Ranges and getPosition are 1-based. Case-folding is ASCII-only so the
// needle length stays equal to the span in the document. The status is
// `index/count` once the caret sits on a match, and the count alone when it
// does not. A next/prev at the end leaves the index unchanged.
//
// Enter / Shift+Enter step the left document. Alt+Enter / Alt+Shift+Enter
// step the right document.
// ---------------------------------------------------------------------------

const searchInput = document.getElementById("search-input");
const clearButton = document.getElementById("clear-highlights");
const searchStatus = document.getElementById("search-status");

/** 1-based ranges, the shape highlightRanges accepts. */
function foldAscii(text) {
  let folded = "";
  for (let i = 0; i < text.length; i++) {
    const code = text.charCodeAt(i);
    folded += code >= 65 && code <= 90 ? String.fromCharCode(code + 32) : text[i];
  }
  return folded;
}

function findMatches(text, query) {
  if (query === "") return [];

  const needle = foldAscii(query);
  const matches = [];
  const lines = text.split("\n");

  for (let lineIndex = 0; lineIndex < lines.length; lineIndex++) {
    const line = lines[lineIndex];
    const haystack = foldAscii(line);
    let at = haystack.indexOf(needle);
    while (at !== -1) {
      matches.push({
        startLineNumber: lineIndex + 1,
        startColumn: at + 1,
        endLineNumber: lineIndex + 1,
        endColumn: at + 1 + needle.length,
      });
      at = haystack.indexOf(needle, at + needle.length);
    }
  }

  return matches;
}

let leftMatches = [];
let rightMatches = [];

function matchIndex(matches, view) {
  const pos = view.getPosition();
  return matches.findIndex(
    (m) => m.startLineNumber === pos.lineNumber && m.startColumn === pos.column
  );
}

function formatSide(label, matches, view) {
  if (matches.length === 0) return `${label} 0`;
  const index = matchIndex(matches, view);
  return index < 0 ? `${label} ${matches.length}` : `${label} ${index + 1}/${matches.length}`;
}

function renderStatus() {
  searchStatus.textContent = searchInput.value === ""
    ? ""
    : `${formatSide("left", leftMatches, leftView1)} · ${formatSide("right", rightMatches, rightView1)}`;
}

function applyHighlight(query) {
  leftMatches = findMatches(textL, query);
  rightMatches = findMatches(textR, query);
  // null clears; an empty array clears as well. One call per side: the model
  // repaints every pane attached to it.
  leftView1.highlightRanges(query === "" ? null : leftMatches);
  rightView1.highlightRanges(query === "" ? null : rightMatches);
  renderStatus();
}

function stepSide(view, next) {
  if (searchInput.value === "") return;
  if (next) view.selectNextRange();
  else view.selectPrevRange();
  view.focus();
  renderStatus();
}

document.getElementById("left-next").addEventListener("click", () => stepSide(leftView1, true));
document.getElementById("left-prev").addEventListener("click", () => stepSide(leftView1, false));
document.getElementById("right-next").addEventListener("click", () => stepSide(rightView1, true));
document.getElementById("right-prev").addEventListener("click", () => stepSide(rightView1, false));
clearButton.addEventListener("click", () => {
  searchInput.value = "";
  applyHighlight("");
});

searchInput.addEventListener("input", () => applyHighlight(searchInput.value));
searchInput.addEventListener("keydown", (event) => {
  if (event.key !== "Enter") return;
  event.preventDefault();
  const view = event.altKey ? rightView1 : leftView1;
  stepSide(view, !event.shiftKey);
});

applyHighlight(searchInput.value);
