package types;

import java.util.List;

public record TypeValidationResult(
        NormalizedValue normalizedValue,
        List<TypeValidationError> errors
) {
    public boolean isValid() {
        return errors.isEmpty();
    }
}
