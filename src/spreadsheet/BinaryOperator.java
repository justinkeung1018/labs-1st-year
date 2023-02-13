package spreadsheet;

import common.api.CellLocation;
import common.api.EvaluationContext;
import common.api.Expression;
import common.lexer.Token.Kind;
import java.util.Map;
import java.util.Set;

public class BinaryOperator implements Expression {
  private final Expression leftExpression;
  private final Expression rightExpression;
  private final Kind operator;
  private static final Map<Kind, String> OPERATORS = Map.of(
      Kind.PLUS, "+",
      Kind.MINUS, "-",
      Kind.STAR, "*",
      Kind.SLASH, "/",
      Kind.CARET, "^"
  );

  public BinaryOperator(Expression leftExpression, Expression rightExpression, Kind operator) {
    assert leftExpression != null;
    assert rightExpression != null;
    assert OPERATORS.containsKey(operator);
    this.leftExpression = leftExpression;
    this.rightExpression = rightExpression;
    this.operator = operator;
  }

  public Kind getOperator() {
    return operator;
  }

  @Override
  public double evaluate(EvaluationContext context) {
    return 0;
  }

  @Override
  public void findCellReferences(Set<CellLocation> dependencies) {

  }

  @Override
  public String toString() {
    return "(" + leftExpression + " " + OPERATORS.get(operator) + " " + rightExpression + ")";
  }
}
