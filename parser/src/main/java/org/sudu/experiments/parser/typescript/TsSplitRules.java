package org.sudu.experiments.parser.typescript;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.common.SplitRules;
import org.sudu.experiments.parser.ecmascript.gen.LightEcmaScriptLexer;
import org.sudu.experiments.parser.help.Helper;

import java.util.List;

public class TsSplitRules extends SplitRules {

  @Override
  public List<TokenSplitRule> getRules() {
    return List.of(
        makeRule(this::isStringLiteral, Helper::splitStringOrCharLiteral),
        makeRule(this::isMultiline, Helper::splitMultilineToken)
    );
  }

  private boolean isStringLiteral(Token token) {
    int type = token.getType();
    return type == LightEcmaScriptLexer.StringLiteral
        || type == LightEcmaScriptLexer.RegularExpressionLiteral;
  }

  private boolean isMultiline(Token token) {
    int type = token.getType();
    return type == LightEcmaScriptLexer.MultiLineComment
        || type == LightEcmaScriptLexer.HtmlComment
        || type == LightEcmaScriptLexer.CDataComment
        || type == LightEcmaScriptLexer.StringLiteral;
  }

}
