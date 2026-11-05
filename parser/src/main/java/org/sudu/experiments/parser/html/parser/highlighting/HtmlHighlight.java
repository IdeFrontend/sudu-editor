package org.sudu.experiments.parser.html.parser.highlighting;

import org.sudu.experiments.parser.common.Highlighting;
import org.sudu.experiments.parser.html.gen.HTMLLexer;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class HtmlHighlight extends Highlighting {

  public static final Highlighting INSTANCE = new HtmlHighlight();

  private HtmlHighlight() {
    addRules(
        // Comment
        HTMLLexer.HTML_COMMENT, HTMLLexer.SCRIPTLET, COMMENT,
        // Tag / annotation
        HTMLLexer.SCRIPT_OPEN, HTMLLexer.TAG_OPEN, ANNOTATION,
        HTMLLexer.TAG_CLOSE, HTMLLexer.TAG_SLASH, ANNOTATION,
        HTMLLexer.TAG_NAME, HTMLLexer.TAG_NAME, ANNOTATION,
        // Operator
        HTMLLexer.TAG_EQUALS, HTMLLexer.TAG_EQUALS, OPERATOR,
        // String
        HTMLLexer.ATTVALUE_VALUE, HTMLLexer.ATTRIBUTE, STRING
    );
    addErrorRule(HTMLLexer.ERROR);
  }
}
