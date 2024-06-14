package ru.leo.expression;

public abstract class Expression {
    private final BoolOperation operation;

    protected Expression(BoolOperation operation) {
        this.operation = operation;
    }

    public BoolOperation getOperation() {
        return operation;
    }

    public abstract boolean accept(IExpressionVisitor visitor);

    public static ExpressionAnd and(Expression... expressions) {
        return new ExpressionAnd(expressions);
    }

    public static Expression or(Expression... expressions) {
        return new ExpressionOr(expressions);
    }

    public static Expression not(Expression... expressions) {
        return new ExpressionNot(expressions);
    }

    public static ExpressionTerm term(String term) {
        return new ExpressionTerm(term);
    }

    public enum BoolOperation {
        TERM,
        AND,
        OR,
        NOT
    }
}
