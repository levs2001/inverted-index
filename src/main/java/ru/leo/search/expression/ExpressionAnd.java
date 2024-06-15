package ru.leo.search.expression;

public class ExpressionAnd extends ExpressionComposite {
    public ExpressionAnd(Expression[] expressions) {
        super(BoolOperation.AND, expressions);
    }

    @Override
    public boolean accept(IExpressionVisitor visitor) {
        return visitor.visitAnd(this);
    }
}
