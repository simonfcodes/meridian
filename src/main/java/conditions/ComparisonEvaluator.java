package conditions;

import java.math.BigDecimal;
import java.util.Objects;

public class ComparisonEvaluator {
    public static boolean doComparison(ComparisonOperator op, ContextValue a, ContextValue b) {

        switch (op) {
            case EQUALS -> {
                return equals(a, b);
            }
            case GREATER_THAN, LESS_THAN -> {
                return gtOrLt(a, b, op);
            }
            case GREATER_THAN_OR_EQUAL, LESS_THAN_OR_EQUAL -> {
                return gteOrLte(a, b, op);
            }
            default -> throw new ComparisonException("Cannot evaluate");
        }
    }

    private static boolean equals(ContextValue a, ContextValue b) {
        if (eitherAreNull(a, b)) {
            return Objects.equals(a.value(), b.value());
        }
        if (a.value().getClass().getName().equals("java.math.BigDecimal")) {
            BigDecimal numA = new BigDecimal(a.value().toString());
            BigDecimal numB = new BigDecimal(b.value().toString());
            return numA.compareTo(numB) == 0;
        }
        return a.value().equals(b.value());
    }

    private static boolean gtOrLt(ContextValue a, ContextValue b, ComparisonOperator op) {
        if (eitherAreNull(a, b)) {
            throw new ComparisonException("Cannot compare by " + op.name() + " when one of the values is null.");
        }
        switch (a.value().getClass().getName()) {
            case "java.math.BigDecimal" -> {
                BigDecimal numA = new BigDecimal(a.value().toString());
                BigDecimal numB = new BigDecimal(b.value().toString());
                return op == ComparisonOperator.GREATER_THAN ? numA.compareTo(numB) > 0 : numA.compareTo(numB) < 0;
            }
            case "java.lang.Integer", "int" -> {
                int numA = Integer.parseInt(a.value().toString());
                int numB = Integer.parseInt(b.value().toString());
                return op == ComparisonOperator.GREATER_THAN ? numA > numB : numA < numB;
            }
            case "java.lang.Double", "double" -> {
                double numA = Double.parseDouble(a.value().toString());
                double numB = Double.parseDouble(b.value().toString());
                return op == ComparisonOperator.GREATER_THAN ? numA > numB : numA < numB;
            }
            default -> throw new ComparisonException("Cannot compare values of type: " + a.value().getClass().getName());
        }
    }

    private static boolean gteOrLte(ContextValue a, ContextValue b, ComparisonOperator op) {
        if (eitherAreNull(a, b)) {
            throw new ComparisonException("Cannot compare by " + op.name() + " when one of the values is null.");
        }
        switch (a.value().getClass().getName()) {
            case "java.math.BigDecimal" -> {
                BigDecimal numA = new BigDecimal(a.value().toString());
                BigDecimal numB = new BigDecimal(b.value().toString());
                return op == ComparisonOperator.GREATER_THAN_OR_EQUAL ? numA.compareTo(numB) >= 0 : numA.compareTo(numB) <= 0;
            }
            case "java.lang.Integer", "int" -> {
                int numA = Integer.parseInt(a.value().toString());
                int numB = Integer.parseInt(b.value().toString());
                return op == ComparisonOperator.GREATER_THAN_OR_EQUAL ? numA >= numB : numA <= numB;
            }
            case "java.lang.Double", "double" -> {
                double numA = Double.parseDouble(a.value().toString());
                double numB = Double.parseDouble(b.value().toString());
                return op == ComparisonOperator.GREATER_THAN_OR_EQUAL ? numA >= numB : numA <= numB;
            }
            default -> throw new ComparisonException("Cannot compare values of type: " + a.value().getClass().getName());
        }
    }

    private static boolean eitherAreNull(ContextValue a, ContextValue b) {
        return (Objects.isNull(a.value()) || Objects.isNull(b.value()));
    }
}
