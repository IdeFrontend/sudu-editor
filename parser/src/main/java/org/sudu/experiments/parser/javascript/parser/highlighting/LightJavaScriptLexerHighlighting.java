package org.sudu.experiments.parser.javascript.parser.highlighting;

import org.sudu.experiments.parser.common.Highlighting;
import org.sudu.experiments.parser.ecmascript.gen.LightEcmaScriptLexer;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class LightJavaScriptLexerHighlighting extends Highlighting {

  public static final Highlighting INSTANCE = new LightJavaScriptLexerHighlighting();

  private LightJavaScriptLexerHighlighting() {
    addRules(
        // Comment
        LightEcmaScriptLexer.MULTI_LINE_COMMENT, LightEcmaScriptLexer.SINGLE_LINE_COMMENT, COMMENT,
        LightEcmaScriptLexer.HTML_COMMENT, LightEcmaScriptLexer.C_DATA_COMMENT, COMMENT,
        // Null
        LightEcmaScriptLexer.NULL_LITERAL, LightEcmaScriptLexer.NULL_LITERAL, NULL,
        // Boolean
        LightEcmaScriptLexer.BOOLEAN_LITERAL, LightEcmaScriptLexer.BOOLEAN_LITERAL, BOOLEAN,
        // Numeric
        LightEcmaScriptLexer.DECIMAL_LITERAL, LightEcmaScriptLexer.COLOR, NUMERIC,
        // Keyword (skip TS-only AS/FROM/READ_ONLY and YIELD_STAR between shared tokens)
        LightEcmaScriptLexer.BREAK, LightEcmaScriptLexer.TRY, KEYWORD,
        LightEcmaScriptLexer.ASYNC, LightEcmaScriptLexer.YIELD, KEYWORD,
        LightEcmaScriptLexer.CLASS, LightEcmaScriptLexer.STATIC, KEYWORD,
        // String
        LightEcmaScriptLexer.STRING_LITERAL, LightEcmaScriptLexer.BACK_TICK, STRING,
        // Semi
        LightEcmaScriptLexer.SEMI_COLON, LightEcmaScriptLexer.COMMA, SEMI
    );
    addErrorRule(LightEcmaScriptLexer.ERROR);
  }
}
