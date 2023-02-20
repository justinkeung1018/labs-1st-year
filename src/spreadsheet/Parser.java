package spreadsheet;

import common.api.Expression;
import common.lexer.InvalidTokenException;
import common.lexer.Lexer;
import common.lexer.Token;
import common.lexer.Token.Kind;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Parser {
  private record OperatorProperties(int precedence, boolean isLeftAssociative) {
  }

  private static final Map<Kind, OperatorProperties> OPERATOR_PROPERTIES = Map.of(
      Kind.PLUS, new OperatorProperties(1, true),
      Kind.MINUS, new OperatorProperties(1, true),
      Kind.STAR, new OperatorProperties(2, true),
      Kind.SLASH, new OperatorProperties(2, true),
      Kind.CARET, new OperatorProperties(3, false)
  );

  /**
   * Parse a string into an Expression.
   *
   * <p>DO NOT CHANGE THE SIGNATURE. The test suite depends on this.
   */
  static Expression parse(String input) throws InvalidSyntaxException, InvalidTokenException {
    Lexer lexer = new Lexer(input);
    List<Token> tokens = new ArrayList<>();
    while (true) {
      Token token = lexer.nextToken();
      if (token == null) {
        break;
      }
      tokens.add(token);
    }

    Stack<Expression> operands = new Stack<>();
    Stack<Kind> operators = new Stack<>();
    for (Token token : tokens) {
      switch (token.kind) {
        case NUMBER:
          operands.push(new Number(token.numberValue));
          break;
        case CELL_LOCATION:
          operands.push(new CellReference(token.cellLocationValue));
          break;
        default:
          if (OPERATOR_PROPERTIES.containsKey(token.kind)) {
            if (!operators.isEmpty()) {
              int currentPrecedence = OPERATOR_PROPERTIES.get(token.kind).precedence();
              OperatorProperties topOperatorProperties = OPERATOR_PROPERTIES.get(operators.peek());
              while (topOperatorProperties.precedence() > currentPrecedence
                  || topOperatorProperties.precedence() == currentPrecedence
                  && topOperatorProperties.isLeftAssociative()) {
                Kind topOperator = operators.pop();
                assert operands.size() >= 2;
                Expression rightExpression = operands.pop();
                Expression leftExpression = operands.pop();
                operands.push(new BinaryOperator(leftExpression, rightExpression, topOperator));
                if (operators.isEmpty()) {
                  break;
                }
                topOperatorProperties = OPERATOR_PROPERTIES.get(operators.peek());
              }
            }
            operators.push(token.kind);
          } else {
            throw new InvalidSyntaxException("Invalid syntax");
          }
      }
    }

    while (!operators.isEmpty()) {
      Kind operator = operators.pop();
      assert operands.size() >= 2;
      Expression rightExpression = operands.pop();
      Expression leftExpression = operands.pop();
      operands.push(new BinaryOperator(leftExpression, rightExpression, operator));
    }

    return operands.pop();
  }
}
