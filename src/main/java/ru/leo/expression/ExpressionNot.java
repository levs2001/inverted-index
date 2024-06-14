package ru.leo.expression;

public class ExpressionNot extends ExpressionComposite {
    public ExpressionNot(Expression[] expressions) {
        super(BoolOperation.NOT, expressions);
    }


    @Override
    public boolean accept(IExpressionVisitor visitor) {
        return visitor.visitNot(this);
    }
}
