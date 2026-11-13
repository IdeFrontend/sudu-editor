package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ModelMultiEditorTest {

  @Test
  void documentEventsReachEveryAttachedEditor() {
    Model model = new Model(new String[]{"a"}, Languages.TEXT, null);
    CountingEditor first = new CountingEditor();
    CountingEditor second = new CountingEditor();
    model.attachEditor(first, null);
    model.attachEditor(second, null);

    model.document.insertLines(0, 1, new String[]{"x"});

    assertEquals(1, first.onDiffMade);
    assertEquals(1, second.onDiffMade);
    assertEquals(1, first.updateModelOnDiff);
    assertEquals(1, second.updateModelOnDiff);

    model.detachEditor(first);
    assertSame(second, model.editor);
    model.document.insertLines(0, 2, new String[]{"y"});
    assertEquals(1, first.onDiffMade);
    assertEquals(2, second.onDiffMade);
  }

  @Test
  void detachRestoresExecutorOfALiveEditor() {
    Model model = new Model(new String[]{"a"}, Languages.TEXT, null);
    CountingEditor first = new CountingEditor();
    CountingEditor second = new CountingEditor();
    NamedExecutor workerA = new NamedExecutor();
    NamedExecutor workerB = new NamedExecutor();
    model.attachEditor(first, workerA);
    model.attachEditor(second, workerB);

    assertSame(first, model.editor);
    assertSame(workerA, model.executor);

    model.detachEditor(second);
    assertSame(first, model.editor);
    assertSame(workerA, model.executor);

    model.attachEditor(second, workerB);
    model.detachEditor(first);
    assertSame(second, model.editor);
    assertSame(workerB, model.executor);
  }

  static final class CountingEditor implements Model.EditorToModel {
    int onDiffMade;
    int updateModelOnDiff;

    public void useDocumentHighlightProvider(int line, int column) {}
    public void fireFileLexed() {}
    public void fireFileIterativeParsed(int start, int stop) {}
    public void updateModelOnDiff(Diff diff, boolean isUndo) { updateModelOnDiff++; }
    public void onDiffMade() { onDiffMade++; }
    public boolean isDisableParser() { return true; }
    public CodeLineColorScheme getColorScheme() { return null; }
  }

  static final class NamedExecutor implements WorkerJobExecutor {
    public void sendToWorker(
        boolean priority, Consumer<Object[]> handler, String method, Object... args
    ) {}
  }
}
