package org.sudu.experiments.editor;

import org.junit.jupiter.api.Test;
import org.sudu.experiments.diff.DiffTypes;
import org.sudu.experiments.diff.LineDiff;
import org.sudu.experiments.editor.ui.colors.CodeLineColorScheme;
import org.sudu.experiments.editor.worker.diff.DiffInfo;
import org.sudu.experiments.editor.worker.diff.DiffRange;
import org.sudu.experiments.editor.worker.diff.FileDiffModel;
import org.sudu.experiments.math.V2i;
import org.sudu.experiments.parser.common.Pair;
import org.sudu.experiments.parser.common.Pos;
import org.sudu.experiments.worker.WorkerJobExecutor;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileDiffModelOncePerEditTest {

  @Test
  void oneInsertShiftsRangesOnceWithTwoEditorsOnTheSameSide() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel diff = new FileDiffModel(new NoopExecutor(), left, right);
    diff.diffModel = new DiffInfo(
        new LineDiff[3],
        new LineDiff[3],
        new DiffRange[] {
            new DiffRange(0, 1, 0, 1, DiffTypes.DEFAULT),
            new DiffRange(1, 2, 1, 2, DiffTypes.EDITED)
        }
    );

    CountingView view1 = new CountingView();
    CountingView view2 = new CountingView();
    diff.addView(view1);
    diff.addView(view2);

    CountingEditor editor1 = new CountingEditor();
    CountingEditor editor2 = new CountingEditor();
    left.attachEditor(editor1, null);
    left.attachEditor(editor2, null);

    left.document.insertLines(0, 0, new String[]{"x", "y"});

    assertEquals(2, diff.diffModel.ranges[1].fromL);
    assertEquals(2, diff.diffModel.ranges[0].lenL);
    assertEquals(1, view1.updateModelOnDiff);
    assertEquals(1, view2.updateModelOnDiff);
    assertEquals(1, editor1.updateModelOnDiff);
    assertEquals(1, editor2.updateModelOnDiff);
  }

  @Test
  void twoFileDiffModelsOnTheSameDocumentsBothShiftOnce() {
    Model left = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    Model right = new Model(new String[]{"a", "b", "c"}, Languages.TEXT, null);
    FileDiffModel first = new FileDiffModel(new NoopExecutor(), left, right);
    FileDiffModel second = new FileDiffModel(new NoopExecutor(), left, right);
    first.diffModel = new DiffInfo(
        new LineDiff[3], new LineDiff[3],
        new DiffRange[] {
            new DiffRange(0, 1, 0, 1, DiffTypes.DEFAULT),
            new DiffRange(1, 2, 1, 2, DiffTypes.EDITED)
        }
    );
    second.diffModel = new DiffInfo(
        new LineDiff[3], new LineDiff[3],
        new DiffRange[] {
            new DiffRange(0, 1, 0, 1, DiffTypes.DEFAULT),
            new DiffRange(1, 2, 1, 2, DiffTypes.EDITED)
        }
    );
    CountingView firstView = new CountingView();
    CountingView secondView = new CountingView();
    first.addView(firstView);
    second.addView(secondView);

    left.document.insertLines(0, 0, new String[]{"x", "y"});

    assertEquals(2, first.diffModel.ranges[1].fromL);
    assertEquals(2, second.diffModel.ranges[1].fromL);
    assertEquals(1, firstView.updateModelOnDiff);
    assertEquals(1, secondView.updateModelOnDiff);

    first.removeView(firstView);
    left.document.insertLines(0, 0, new String[]{"p", "q"});

    assertEquals(2, first.diffModel.ranges[1].fromL);
    assertEquals(3, second.diffModel.ranges[1].fromL);
  }

  static final class CountingView implements FileDiffModel.ViewToModel {
    int updateModelOnDiff;

    public int[] getSyncPoints(boolean left) { return new int[0]; }
    public void updateModelOnDiff(DiffInfo diffModel) { updateModelOnDiff++; }
    public void setDiffModel(DiffInfo diffModel) {}
    public void restoreSelection(boolean left, V2i caret, Pair<Pos, Pos> selection) {}
  }

  static final class CountingEditor implements Model.EditorToModel {
    int updateModelOnDiff;

    public void useDocumentHighlightProvider(int line, int column) {}
    public void fireFileLexed() {}
    public void fireFileIterativeParsed(int start, int stop) {}
    public void updateModelOnDiff(Diff diff, boolean isUndo) { updateModelOnDiff++; }
    public void onDiffMade() {}
    public boolean isDisableParser() { return true; }
    public CodeLineColorScheme getColorScheme() { return null; }
  }

  static final class NoopExecutor implements WorkerJobExecutor {
    public void sendToWorker(
        boolean priority, Consumer<Object[]> handler, String method, Object... args
    ) {}
  }
}
