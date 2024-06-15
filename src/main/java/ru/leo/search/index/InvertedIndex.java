package ru.leo.search.index;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import ru.leo.search.expression.Expression;
import ru.leo.search.expression.ExpressionAnd;
import ru.leo.search.expression.ExpressionNot;
import ru.leo.search.expression.ExpressionOr;
import ru.leo.search.expression.ExpressionTerm;
import ru.leo.search.expression.IExpressionVisitor;
import ru.leo.search.list.IPostingList;

/**
 * Not thread safe!!!
 */
public class InvertedIndex implements IInvertedIndex {
    private final Map<String, IPostingList> postingLists;

    public InvertedIndex(Map<String, IPostingList> postingLists) {
        this.postingLists = postingLists;
    }

    // TODO: Now works only term and 'and'
    @Override
    public List<Long> find(Expression expression) {
        ExpressionVisitor visitor = new ExpressionVisitor();
        expression.accept(visitor);
        List<String> join = visitor.getJoinList();

        List<IPostingList> postings = new ArrayList<>();
        for (String term : join) {
            IPostingList postingList = postingLists.get(term);
            if (postingLists.get(term) == null) {
                return new ArrayList<>();
            }
            postings.add(postingList);
        }

        postings.forEach(IPostingList::reset);
        return join(postings);
    }

    private static List<Long> join(List<IPostingList> postings) {
        List<IPostingList> other = new ArrayList<>(postings);
        other.sort(Comparator.comparingInt(IPostingList::size));
        IPostingList shortest = other.getFirst();
        other.removeFirst();

        List<Long> result = new ArrayList<>();
        while (shortest.hasNext()) {
            long cur = shortest.next();
            boolean joined = true;
            for (IPostingList o : other) {
                if (o.current() == cur) {
                    continue;
                }

                if (!o.hasNext()) {
                    return result;
                }

                long curO = o.advance(cur);
                if (curO != cur) {
                    joined = false;
                    break;
                }
            }

            if (joined) {
                result.add(cur);
            }
        }
        return result;

    }

    // TODO: Now works only term and 'and'
    public static class ExpressionVisitor implements IExpressionVisitor {
        private final List<String> join = new ArrayList<>();

        public List<String> getJoinList() {
            return join;
        }

        @Override
        public boolean visitAnd(ExpressionAnd expressionAnd) {
            for (Expression expression : expressionAnd.getExpressions()) {
                expression.accept(this);
            }
            return true;
        }

        @Override
        public boolean visitOr(ExpressionOr expressionOr) {
            return false;
        }

        @Override
        public boolean visitNot(ExpressionNot expressionNot) {
            return false;
        }

        @Override
        public boolean visitTerm(ExpressionTerm expressionTerm) {
            join.add(expressionTerm.term());
            return true;
        }
    }
}
