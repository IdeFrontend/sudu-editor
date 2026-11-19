package org.sudu.experiments.parser.python.parser.highlighting;

import org.sudu.experiments.parser.common.Highlighting;
import org.sudu.experiments.parser.python.gen.PythonLexer;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class PythonHighlighting extends Highlighting {

  public static final Highlighting INSTANCE = new PythonHighlighting();

  private PythonHighlighting() {
    addRules(
        // Keyword
        PythonLexer.DEF, PythonLexer.EXEC, KEYWORD,
        // Boolean
        PythonLexer.TRUE, PythonLexer.FALSE, BOOLEAN,
        // Operator
        PythonLexer.DOT, PythonLexer.IDIV_ASSIGN, OPERATOR,
        // String
        PythonLexer.STRING, PythonLexer.MULTILINE_STRING, STRING,
        // Numeric
        PythonLexer.DECIMAL_INTEGER, PythonLexer.FLOAT_NUMBER, NUMERIC,
        // Comment
        PythonLexer.COMMENT, PythonLexer.COMMENT, COMMENT
    );
    addErrorRule(PythonLexer.ERROR);
  }
}
