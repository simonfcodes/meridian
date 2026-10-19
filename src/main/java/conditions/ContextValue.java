package conditions;

import java.util.Objects;

public record ContextValue(
        Object value,
        FieldLookupResult result
) {
    public ContextValue(Object value, FieldLookupResult result) {
        if (Objects.isNull(value) && result == FieldLookupResult.FOUND) {
            throw new IllegalArgumentException("Cannot have a null value with a FOUND result.");
        }
        if (!(Objects.isNull(value)) && result == FieldLookupResult.MISSING) {
            throw new IllegalArgumentException("Cannot have a non-null value with a MISSING result.");
        }
        this.value = value;
        this.result = result;
    }
}
