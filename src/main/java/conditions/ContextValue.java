package conditions;

public class ContextValue {
    private final FieldLookupResult result;
    private final String className;
    private final Object fieldValue;

    public ContextValue(String className, Object fieldValue, FieldLookupResult result) {
        this.className = className;
        this.fieldValue = fieldValue;
        this.result = result;
    }

    public String getClassName() {
        return className;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public FieldLookupResult getResult() {
        return result;
    }

    public enum FieldLookupResult {
        FOUND,
        FOUNDNULL,
        MISSING,
        PROVIDED
    }
}
