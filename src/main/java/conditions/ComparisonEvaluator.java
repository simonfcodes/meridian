package conditions;

import java.math.BigDecimal;
import java.util.Objects;

public class ComparisonEvaluator {
    public static boolean doComparison(ComparisonOperator op, ContextValue a, ContextValue b) {
        if (!(a.getClassName().equals(b.getClassName()))) {
            throw new ComparisonException("Cannot compare values of different types: " + a.getClassName() + " and " + b.getClassName());
        }
        switch (op) {
            case EQUALS -> {
                return equals(a, b);
            }
            case GREATER_THAN -> {
                return greaterThan(a, b);
            }
            case LESS_THAN -> {
                return lessThan(a, b);
            }
            default -> throw new ComparisonException("Cannot evaluate");
        }
    }

    private static boolean equals(ContextValue a, ContextValue b) {
        if (a.getResult() == ContextValue.FieldLookupResult.FOUNDNULL) {
            return Objects.isNull(b.getFieldValue());
        }
        if (a.getClassName().equals("java.math.BigDecimal")) {
            BigDecimal numA = new BigDecimal(a.getFieldValue().toString());
            BigDecimal numB = new BigDecimal(b.getFieldValue().toString());
            return numA.compareTo(numB) == 0;
        }
        return a.getFieldValue().equals(b.getFieldValue());
    }

    private static boolean greaterThan(ContextValue a, ContextValue b) {
        switch (a.getClassName()) {
            case "java.math.BigDecimal" -> {
                BigDecimal numA = new BigDecimal(a.getFieldValue().toString());
                BigDecimal numB = new BigDecimal(b.getFieldValue().toString());
                return numA.compareTo(numB) > 0;
            }
            case "Integer", "int" -> {
                Integer numA = (Integer) a.getFieldValue();
                Integer numB = (Integer) b.getFieldValue();
                return numA > numB;
            }
            case "Double", "double" -> {
                Double numA = (Double) a.getFieldValue();
                Double numB = (Double) b.getFieldValue();
                return numA > numB;
            }
            case "String" -> {
                String strA = (String) a.getFieldValue();
                String strB = (String) b.getFieldValue();
                return strA.compareTo(strB) > 0;
            }
            default -> throw new ComparisonException("Cannot compare values of type: " + a.getClassName());
        }
    }

    private static boolean lessThan(ContextValue a, ContextValue b) {
        switch (a.getClassName()) {
            case "java.math.BigDecimal" -> {
                BigDecimal numA = new BigDecimal(a.getFieldValue().toString());
                BigDecimal numB = new BigDecimal(b.getFieldValue().toString());
                return numA.compareTo(numB) < 0;
            }
            case "Integer", "int" -> {
                Integer numA = (Integer) a.getFieldValue();
                Integer numB = (Integer) b.getFieldValue();
                return numA < numB;
            }
            case "Double", "double" -> {
                Double numA = (Double) a.getFieldValue();
                Double numB = (Double) b.getFieldValue();
                return numA < numB;
            }
            case "String" -> {
                String strA = (String) a.getFieldValue();
                String strB = (String) b.getFieldValue();
                return strA.compareTo(strB) < 0;
            }
            default -> throw new ComparisonException("Cannot compare values of type: " + a.getClassName());
        }
    }
}
