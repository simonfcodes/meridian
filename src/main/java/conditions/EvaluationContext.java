package conditions;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class EvaluationContext {
    private final Map<String, Object> context;

    public EvaluationContext(Map<String, Object> initValues) {
        this.context = Collections.unmodifiableMap(new HashMap<>(initValues));
    }

    public ContextValue getFieldValue(String fieldName) {
        if (!context.containsKey(fieldName)) {
            return new ContextValue("null", null, ContextValue.FieldLookupResult.MISSING);
        }
        Object fieldValue = context.get(fieldName);
        if (Objects.isNull(fieldValue)) {
            return new ContextValue("null", null, ContextValue.FieldLookupResult.FOUNDNULL);
        }
        String className = fieldValue.getClass().getName();
        return new ContextValue(className, fieldValue, ContextValue.FieldLookupResult.FOUND);

    }
}
