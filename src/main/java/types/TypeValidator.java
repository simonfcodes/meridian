package types;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class TypeValidator {
    public static TypeValidationResult validate(TypeDescriptor expectedType, Object suppliedValue) {
        switch (expectedType) {
            case TypeDescriptor.ScalarType scalarType -> {
                switch (scalarType.dataType()) {
                    case STRING -> {
                        if (suppliedValue instanceof String || Objects.isNull(suppliedValue)) { // null is acceptable for comparison
                            return new TypeValidationResult(createNormalizedValue(expectedType, suppliedValue), List.of());
                        }
                        return new TypeValidationResult(null, List.of(generateError(expectedType, suppliedValue)));
                    }
                    case BOOLEAN -> {
                        if (suppliedValue instanceof Boolean) {
                            return new TypeValidationResult(createNormalizedValue(expectedType, suppliedValue), List.of());
                        }
                        if (Objects.isNull(suppliedValue)) {
                            return new TypeValidationResult(null, List.of(generateError(expectedType, "null", "Expected Boolean value but received null. Booleans must be either true or false.")));
                        }
                        return new TypeValidationResult(null, List.of(generateError(expectedType, suppliedValue)));
                    }
                    case DECIMAL -> {
                        if (suppliedValue instanceof BigDecimal || Objects.isNull(suppliedValue)) {
                            return new TypeValidationResult(createNormalizedValue(expectedType, suppliedValue), List.of());
                        }
                        if (suppliedValue instanceof Integer) {
                            return new TypeValidationResult(createNormalizedValue(expectedType, new BigDecimal(suppliedValue.toString())), List.of());
                        }
                        if (suppliedValue instanceof Long) {
                            return new TypeValidationResult(createNormalizedValue(expectedType, BigDecimal.valueOf((Long) suppliedValue)), List.of());
                        }
                        if (suppliedValue instanceof Double) {
                            return new TypeValidationResult(createNormalizedValue(expectedType, BigDecimal.valueOf((Double) suppliedValue)), List.of());
                        }
                        if (suppliedValue instanceof String) {
                            try {
                                BigDecimal decimalValue = new BigDecimal((String) suppliedValue);
                                return new TypeValidationResult(createNormalizedValue(expectedType, decimalValue), List.of());
                            } catch (NumberFormatException e) {
                                return new TypeValidationResult(null, List.of(generateError(expectedType, suppliedValue)));
                            }
                        }
                    }
                    case DATE -> {}
                }
            }
            case TypeDescriptor.CollectionType collectionType -> {

            }
        }
        return new TypeValidationResult(null, List.of(generateError(expectedType, "Unknown received", "not sure how we got this far.")));
    }

    private static TypeValidationError generateError(TypeDescriptor type, String actualReceived, String customMessage) {
        return new TypeValidationError(
                "This is a path, how do I get this in?",
                type,
                actualReceived,
                customMessage
        );
    }

    private static TypeValidationError generateError(TypeDescriptor type, Object suppliedValue) {
        return new TypeValidationError(
                "This is a path, how do I get this in?",
                type,
                suppliedValue.getClass().getName(),
                "Expected " + type.toString() + ", received " + suppliedValue.getClass().getName()
        );
    }

    private static NormalizedValue createNormalizedValue(TypeDescriptor type, Object value) {
        return new NormalizedValue(type, value);
    }
}
