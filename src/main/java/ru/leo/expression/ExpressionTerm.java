package ru.leo.expression;

public class ExpressionTerm extends Expression {
    private final String term;

    public ExpressionTerm(String term) {
        super(BoolOperation.TERM);
        this.term = term;
    }

    public String term() {
        return term;
    }

    @Override
    public boolean accept(IExpressionVisitor visitor) {
        return visitor.visitTerm(this);
    }
}
