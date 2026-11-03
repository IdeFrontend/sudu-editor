package org.sudu.experiments.parser.java.parser.highlighting;

import org.sudu.experiments.parser.common.Highlighting;
import org.sudu.experiments.parser.java.gen.JavaLexer;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class JavaLexerHighlighting extends Highlighting {

  public static final Highlighting INSTANCE = new JavaLexerHighlighting();
  public static final Highlighting COMMENT_INSTANCE = new JavaCommentHighlighting();

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

  // Tokens from MODULE to VAR can be used as identifiers
  public static boolean isKeyword(int type) {
    return (type >= JavaLexer.ABSTRACT && type <= JavaLexer.WHILE)
        || (type >= JavaLexer.YIELD && type <= JavaLexer.NON_SEALED);
  }

  public static boolean isKeywordIdentifier(int type) {
    return type >= JavaLexer.MODULE && type <= JavaLexer.VAR;
  }

  public static boolean isNumeric(int type) {
    return type >= JavaLexer.DECIMAL_LITERAL && type <= JavaLexer.HEX_FLOAT_LITERAL;
  }

  public static boolean isBooleanLiteral(int type) {
    return type == JavaLexer.BOOL_LITERAL;
  }

  public static boolean isStringOrChar(int type) {
    return type >= JavaLexer.CHAR_LITERAL && type <= JavaLexer.TEXT_BLOCK;
  }

  public static boolean isNull(int type) {
    return type == JavaLexer.NULL_LITERAL;
  }

  public static boolean isSemi(int type) {
    return type == JavaLexer.SEMI || type == JavaLexer.COMMA;
  }

  public static boolean isAT(int type) {
    return type == JavaLexer.AT;
  }

  private static class JavaCommentHighlighting extends Highlighting {
    private JavaCommentHighlighting() {
      addRules(
          JavaLexer.JAVADOC, JavaLexer.JAVADOC, DOCUMENTATION,
          JavaLexer.COMMENT, JavaLexer.LINE_COMMENT, COMMENT
      );
      addErrorRule(JavaLexer.ERROR);
    }
  }
}
