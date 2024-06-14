package ru.leo.expression;

public class ExpressionOr extends ExpressionComposite {
    public ExpressionOr(Expression[] expressions) {
        super(BoolOperation.OR, expressions);
    }

    @Override
    public boolean accept(IExpressionVisitor visitor) {
        return visitor.visitOr(this);
    }
}
