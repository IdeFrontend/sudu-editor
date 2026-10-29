package org.sudu.experiments.parser.typescript.parser.highlighting;

import org.antlr.v4.runtime.Token;
import org.sudu.experiments.parser.ecmascript.gen.LightEcmaScriptLexer;

import java.util.List;

import static org.sudu.experiments.parser.ParserConstants.TokenTypes.*;

public class LightTypeScriptHighlighting {

  public static void highlightTokens(List<Token> allTokens, int[] tokenTypes) {
    for (var token: allTokens) {
      int ind = token.getTokenIndex();
      int type = token.getType();
      if (isComment(type)) tokenTypes[ind] = COMMENT;
      else if (isNull(type)) tokenTypes[ind] = NULL;
      else if (isBoolean(type)) tokenTypes[ind] = BOOLEAN;
      else if (isNumeric(type)) tokenTypes[ind] = NUMERIC;
      else if (isKeyword(type)) tokenTypes[ind] = KEYWORD;
      else if (isString(type)) tokenTypes[ind] = STRING;
      else if (isSemi(type)) tokenTypes[ind] = SEMI;
    }
  }

  private static boolean isSemi(int tokenType) {
    return tokenType == LightEcmaScriptLexer.SemiColon;
  }

  private static boolean isString(int tokenType) {
    return tokenType == LightEcmaScriptLexer.StringLiteral;
  }

  private static boolean isKeyword(int tokenType) {
    return switch (tokenType) {
      case LightEcmaScriptLexer.Break,
           LightEcmaScriptLexer.Do,
           LightEcmaScriptLexer.Instanceof,
           LightEcmaScriptLexer.Typeof,
           LightEcmaScriptLexer.Case,
           LightEcmaScriptLexer.Else,
           LightEcmaScriptLexer.New,
           LightEcmaScriptLexer.Var,
           LightEcmaScriptLexer.Catch,
           LightEcmaScriptLexer.Finally,
           LightEcmaScriptLexer.Return,
           LightEcmaScriptLexer.Void,
           LightEcmaScriptLexer.Continue,
           LightEcmaScriptLexer.For,
           LightEcmaScriptLexer.Switch,
           LightEcmaScriptLexer.While,
           LightEcmaScriptLexer.Debugger,
           LightEcmaScriptLexer.Function_,
           LightEcmaScriptLexer.This,
           LightEcmaScriptLexer.With,
           LightEcmaScriptLexer.Default,
           LightEcmaScriptLexer.If,
           LightEcmaScriptLexer.Throw,
           LightEcmaScriptLexer.Delete,
           LightEcmaScriptLexer.In,
           LightEcmaScriptLexer.Try,
           LightEcmaScriptLexer.As,
           LightEcmaScriptLexer.From,
           LightEcmaScriptLexer.ReadOnly,
           LightEcmaScriptLexer.Async,
           LightEcmaScriptLexer.Await,
           LightEcmaScriptLexer.Yield,
           LightEcmaScriptLexer.YieldStar,
           LightEcmaScriptLexer.Class,
           LightEcmaScriptLexer.Enum,
           LightEcmaScriptLexer.Extends,
           LightEcmaScriptLexer.Super,
           LightEcmaScriptLexer.Const,
           LightEcmaScriptLexer.Export,
           LightEcmaScriptLexer.Import,
           LightEcmaScriptLexer.Implements,
           LightEcmaScriptLexer.Let,
           LightEcmaScriptLexer.Private,
           LightEcmaScriptLexer.Public,
           LightEcmaScriptLexer.Interface,
           LightEcmaScriptLexer.Package,
           LightEcmaScriptLexer.Protected,
           LightEcmaScriptLexer.Static,
           LightEcmaScriptLexer.Any,
           LightEcmaScriptLexer.Number,
           LightEcmaScriptLexer.Never,
           LightEcmaScriptLexer.Boolean,
           LightEcmaScriptLexer.String,
           LightEcmaScriptLexer.Unique,
           LightEcmaScriptLexer.Symbol,
           LightEcmaScriptLexer.Undefined,
           LightEcmaScriptLexer.Object,
           LightEcmaScriptLexer.Of,
           LightEcmaScriptLexer.KeyOf,
           LightEcmaScriptLexer.TypeAlias,
           LightEcmaScriptLexer.Constructor,
           LightEcmaScriptLexer.Namespace,
           LightEcmaScriptLexer.Require,
           LightEcmaScriptLexer.Module,
           LightEcmaScriptLexer.Declare,
           LightEcmaScriptLexer.Abstract,
           LightEcmaScriptLexer.Is -> true;
      default -> false;
    };
  }

  private static boolean isNumeric(int tokenType) {
    return tokenType == LightEcmaScriptLexer.DecimalLiteral
        || tokenType == LightEcmaScriptLexer.HexIntegerLiteral
        || tokenType == LightEcmaScriptLexer.OctalIntegerLiteral
        || tokenType == LightEcmaScriptLexer.OctalIntegerLiteral2
        || tokenType == LightEcmaScriptLexer.BinaryIntegerLiteral
        || tokenType == LightEcmaScriptLexer.BigHexIntegerLiteral
        || tokenType == LightEcmaScriptLexer.BigOctalIntegerLiteral
        || tokenType == LightEcmaScriptLexer.BigBinaryIntegerLiteral
        || tokenType == LightEcmaScriptLexer.BigDecimalIntegerLiteral;
  }

  private static boolean isBoolean(int tokenType) {
    return tokenType == LightEcmaScriptLexer.BooleanLiteral;
  }

  private static boolean isNull(int tokenType) {
    return tokenType == LightEcmaScriptLexer.NullLiteral;
  }

  public static boolean isComment(int tokenType) {
    return tokenType == LightEcmaScriptLexer.MultiLineComment
        || tokenType == LightEcmaScriptLexer.SingleLineComment
        || tokenType == LightEcmaScriptLexer.HtmlComment
        || tokenType == LightEcmaScriptLexer.CDataComment;
  }
}
