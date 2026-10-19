package types;

import java.util.Objects;

public sealed interface TypeDescriptor {
    record ScalarType(DataType dataType) implements TypeDescriptor {
        public ScalarType {
            Objects.requireNonNull(dataType, "Data type cannot be null");
        }
    }
    record CollectionType(TypeDescriptor elementType) implements TypeDescriptor {
        public CollectionType {
            Objects.requireNonNull(elementType, "Element type cannot be null");
        }
    }
}
