package org.sudu.experiments.parser.json.parser;

import org.sudu.experiments.parser.common.Highlighting;
import org.sudu.experiments.parser.json.gen.JsonLexer;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class JsonHighlight extends Highlighting {

  public static final Highlighting INSTANCE = new JsonHighlight();

  private JsonHighlight() {
    addRules(
        // String
        JsonLexer.STRING, JsonLexer.STRING, STRING,
        // Numeric
        JsonLexer.NUMBER, JsonLexer.NUMBER, NUMERIC,
        // Boolean
        JsonLexer.BOOLEAN, JsonLexer.BOOLEAN, BOOLEAN,
        // Null
        JsonLexer.NULL, JsonLexer.NULL, NULL,
        // Semi
        JsonLexer.COMMA, JsonLexer.COLON, SEMI,
        // Comment
        JsonLexer.COMMENT, JsonLexer.LINE_COMMENT, COMMENT
    );
    addErrorRule(JsonLexer.ERROR);
  }
}
