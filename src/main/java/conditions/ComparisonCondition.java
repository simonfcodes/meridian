package conditions;

public class ComparisonCondition implements Condition {
    private final String field; // the field we need to look for
    private final ComparisonOperator op; // the operator we're going to apply
    private final Object value; // what we're actually comparing against.

    public ComparisonCondition(String field, ComparisonOperator op, Object value) {
        this.field = field;
        this.op = op;
        this.value = value;
    }

    public boolean evaluate(EvaluationContext context) {
        ContextValue fieldValue = context.getFieldValue(field);
        if (fieldValue.result() == FieldLookupResult.MISSING) {
            throw new ComparisonException("Field '" + field + "' is missing from the evaluation context.");
        }
        ContextValue compareToValue = new ContextValue(
                value,
                FieldLookupResult.PROVIDED
        );
        return ComparisonEvaluator.doComparison(op, fieldValue, compareToValue);
    }
}
