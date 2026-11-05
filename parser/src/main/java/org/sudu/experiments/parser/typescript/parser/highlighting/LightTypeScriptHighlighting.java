package org.sudu.experiments.parser.typescript.parser.highlighting;

import org.sudu.experiments.parser.common.Highlighting;
import org.sudu.experiments.parser.ecmascript.gen.LightEcmaScriptLexer;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class LightTypeScriptHighlighting extends Highlighting {

  public static final Highlighting INSTANCE = new LightTypeScriptHighlighting();

  private LightTypeScriptHighlighting() {
    addRules(
        // Comment
        LightEcmaScriptLexer.MULTI_LINE_COMMENT, LightEcmaScriptLexer.SINGLE_LINE_COMMENT, COMMENT,
        LightEcmaScriptLexer.HTML_COMMENT, LightEcmaScriptLexer.C_DATA_COMMENT, COMMENT,
        // Null
        LightEcmaScriptLexer.NULL_LITERAL, LightEcmaScriptLexer.NULL_LITERAL, NULL,
        // Boolean
        LightEcmaScriptLexer.BOOLEAN_LITERAL, LightEcmaScriptLexer.BOOLEAN_LITERAL, BOOLEAN,
        // Numeric
        LightEcmaScriptLexer.DECIMAL_LITERAL, LightEcmaScriptLexer.BIG_DECIMAL_INTEGER_LITERAL, NUMERIC,
        // Keyword
        LightEcmaScriptLexer.BREAK, LightEcmaScriptLexer.IS, KEYWORD,
        // String
        LightEcmaScriptLexer.STRING_LITERAL, LightEcmaScriptLexer.STRING_LITERAL, STRING,
        // Semi
        LightEcmaScriptLexer.SEMI_COLON, LightEcmaScriptLexer.SEMI_COLON, SEMI
    );
    addErrorRule(LightEcmaScriptLexer.ERROR);
  }
}
