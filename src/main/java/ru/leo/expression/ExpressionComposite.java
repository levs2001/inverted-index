package ru.leo.expression;

public abstract class ExpressionComposite extends Expression {
    private final Expression[] expressions;

    public Expression[] getExpressions() {
        return expressions;
    }

    public ExpressionComposite(BoolOperation operation, Expression[] expressions) {
        super(operation);
        this.expressions = expressions;
    }
}
