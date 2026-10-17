package conditions;

import java.util.List;
import java.util.Objects;

public class ConditionGroup implements Condition {
    private final LogicalOperator op;
    private final List<Condition> conditions;

    public ConditionGroup(LogicalOperator op, List<Condition> conditions) {
        if (conditions.isEmpty()) {
            throw new ComparisonException("A condition group must contain at least one condition to evaluate.");
        }
        this.op = Objects.requireNonNull(op);
        if (op == LogicalOperator.NOT && conditions.size() > 1) {
            throw new ComparisonException("NOT operator can only have one condition to evaluate.");
        }
        this.conditions = List.copyOf(conditions);
    }

    public boolean evaluate(EvaluationContext context) {
        boolean acc = op == LogicalOperator.AND;

        for (Condition condition : conditions) {
            boolean result = condition.evaluate(context);
            if (op == LogicalOperator.AND) {
                acc = acc && result;
                if (!acc) {
                    return false; // short-circuit for AND
                }
            } else if (op == LogicalOperator.OR) {
                acc = acc || result;
                if (acc) {
                    return true; // short-circuit for OR
                }
            } else if (op == LogicalOperator.NOT) {
                acc = !result;
            }
        }
        return acc;
    }
}
