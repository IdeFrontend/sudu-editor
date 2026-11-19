package org.sudu.experiments.parser.java.parser.highlighting;

import org.sudu.experiments.parser.common.Highlighting;
import org.sudu.experiments.parser.java.gen.JavaLexer;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class JavaLexerHighlighting extends Highlighting {

  public static final Highlighting INSTANCE = new JavaLexerHighlighting();

  private JavaLexerHighlighting() {
    addRules(
        // Keyword
        JavaLexer.ABSTRACT, JavaLexer.WHILE, KEYWORD,
        JavaLexer.YIELD, JavaLexer.NON_SEALED, KEYWORD,
        // Numeric
        JavaLexer.DECIMAL_LITERAL, JavaLexer.HEX_FLOAT_LITERAL, NUMERIC,
        // Boolean
        JavaLexer.BOOL_LITERAL, JavaLexer.BOOL_LITERAL, BOOLEAN,
        // String / Char
        JavaLexer.CHAR_LITERAL, JavaLexer.TEXT_BLOCK, STRING,
        // Null
        JavaLexer.NULL_LITERAL, JavaLexer.NULL_LITERAL, NULL,
        // Semi
        JavaLexer.SEMI, JavaLexer.COMMA, SEMI,
        // Annotation
        JavaLexer.AT, JavaLexer.AT, ANNOTATION,
        // Comment
        JavaLexer.COMMENT, JavaLexer.LINE_COMMENT, COMMENT,
        // Doc
        JavaLexer.JAVADOC, JavaLexer.JAVADOC, DOCUMENTATION
    );
    addErrorRule(JavaLexer.ERROR);
  }

  // Tokens from MODULE to VAR can be used as identifiers; highlight as keywords only in keyword positions.
  public static boolean isKeywordIdentifier(int type) {
    return type >= JavaLexer.MODULE && type <= JavaLexer.VAR;
  }
}
