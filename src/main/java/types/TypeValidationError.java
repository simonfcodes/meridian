package types;

public record TypeValidationError(
        String path,
        TypeDescriptor expectedTypeDescriptor,
        String actualDataType,
        String message
) {
}
