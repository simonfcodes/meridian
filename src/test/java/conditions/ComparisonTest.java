package conditions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ComparisonTest {

    @Test
    void test_andShouldMatchWhenAllConditionsAreTrue() {
        ConditionGroup cdg = TestConditionFactory.getCdg_andWithTwoConditions_countryEqAndRevenueGt();

        Map<String, Object> facts = Map.of(
                "customer.country", "GB",
                "customer.annualRevenue", new BigDecimal("125000000")
        );

        EvaluationContext ctx = new EvaluationContext(facts);

        boolean result = cdg.evaluate(ctx);
        Assertions.assertTrue(result, "Evaluation should return true when all facts match the conditions.");
    }
    @Test
    void test_andShouldNotMatchWhenCountryIsDifferent() {
        ConditionGroup cdg = TestConditionFactory.getCdg_andWithTwoConditions_countryEqAndRevenueGt();

        Map<String, Object> facts = Map.of(
                "customer.country", "US",
                "customer.annualRevenue", new BigDecimal("125000000")
        );

        EvaluationContext ctx = new EvaluationContext(facts);

        boolean result = cdg.evaluate(ctx);
        Assertions.assertFalse(result, "Evaluation should return false when country does not match the condition.");
    }
    @Test
    void test_andShouldNotMatchWhenRevenueIsTooLow() {
        ConditionGroup cdg = TestConditionFactory.getCdg_andWithTwoConditions_countryEqAndRevenueGt();
        Map<String, Object> facts = Map.of(
                "customer.country", "GB",
                "customer.annualRevenue", new BigDecimal("75000000")
        );

        EvaluationContext ctx = new EvaluationContext(facts);

        boolean result = cdg.evaluate(ctx);
        Assertions.assertFalse(result, "Evaluation should return false when the revenue is lower than the condition requirement.");
    }

    @Test
    void test_orShouldMatchWhenOneConditionIsTrue() {
        ConditionGroup cdg = TestConditionFactory.getCdg_orWithTwoConditions_countryEqAndRevenueGt();

        Map<String, Object> facts1 = Map.of(
                "customer.country", "GB", // matching condition
                "customer.annualRevenue", new BigDecimal("75000000") // not matching condition
        );
        EvaluationContext ctx1 = new EvaluationContext(facts1);
        Map<String, Object> facts2 = Map.of(
                "customer.country", "US", // not matching condition
                "customer.annualRevenue", new BigDecimal("125000000") // matching condition
        );
        EvaluationContext ctx2 = new EvaluationContext(facts2);

        boolean result1 = cdg.evaluate(ctx1);
        boolean result2 = cdg.evaluate(ctx2);

        Assertions.assertTrue(result1, "Evaluation should return true when country matches and annual revenue does not.");
        Assertions.assertTrue(result2, "Evaluation should return true when revenue matches and country doesn't.");
    }

    @Test
    void test_orShouldNotMatchWhenNoConditionsMatch() {
        ConditionGroup cdg = TestConditionFactory.getCdg_orWithTwoConditions_countryEqAndRevenueGt();

        Map<String, Object> facts = Map.of(
                "customer.country", "US",
                "customer.annualRevenue", new BigDecimal("75000000")
        );
        EvaluationContext ctx = new EvaluationContext(facts);

        boolean result = cdg.evaluate(ctx);
        Assertions.assertFalse(result, "Evaluation should return false when no condition matches in an OR evaluation.");
    }

    @Test
    void test_notShouldReturnFalseWhenConditionIsTrue() {
        ConditionGroup cdg = TestConditionFactory.getCdg_not_countryEq();
        Map<String, Object> facts = Map.of(
                "customer.country", "GB",
                "customer.annualRevenue", new BigDecimal("75000000") // not relevant for test itself but make sure doesn't break the test
        );
        EvaluationContext ctx = new EvaluationContext(facts);

        boolean result = cdg.evaluate(ctx);
        Assertions.assertFalse(result, "Result should be False when country matches the condition but the logical operator is NOT");
    }

    @Test
    void test_notShouldReturnTrueWhenConditionIsTrue() {
        ConditionGroup cdg = TestConditionFactory.getCdg_not_countryEq();
        Map<String, Object> facts = Map.of(
                "customer.country", "US",
                "customer.annualRevenue", new BigDecimal("75000000") // not relevant for test itself but make sure doesn't break the test
        );
        EvaluationContext ctx = new EvaluationContext(facts);

        boolean result = cdg.evaluate(ctx);
        Assertions.assertTrue(result, "Result should be True when country does not match the condition but the logical operator is NOT");
    }

    @Test
    void test_nestedAndWithOr_shouldReturnTrueWhenSatisfied() {
        ConditionGroup nestedCdg = TestConditionFactory.getNestedCdg_andWithNestedOr();
        // inner OR matches on revenue, outer AND matches on Technology
        Map<String, Object> facts = Map.of(
                "customer.country", "US",
                "customer.annualRevenue", new BigDecimal("125000000"),
                "customer.industry", "Technology"
        );

        EvaluationContext ctx = new EvaluationContext(facts);

        boolean result = nestedCdg.evaluate(ctx);
        Assertions.assertTrue(result, "Result should be True when the nested AND with OR conditions are satisfied.");
    }

    @Test
    void test_nestedAndWithOr_shouldReturnFalseWhenUnsatisfied() {
        ConditionGroup nestedCdg = TestConditionFactory.getNestedCdg_andWithNestedOr();
        // inner OR matches on country, outer AND will not match.
        Map<String, Object> facts = Map.of(
                "customer.country", "GB",
                "customer.annualRevenue", new BigDecimal("75000000"),
                "customer.industry", "Financial"
        );

        EvaluationContext ctx = new EvaluationContext(facts);
        boolean result = nestedCdg.evaluate(ctx);
        Assertions.assertFalse(result, "Result should be False when the nested AND with OR conditions are not satisfied.");
    }

    @Test
    void test_emptyAndGroupShouldThrowException() {
        ComparisonException exception = Assertions.assertThrows(ComparisonException.class, () ->
            new ConditionGroup(
                    LogicalOperator.AND,
                    List.of()
            ));
        Assertions.assertEquals("A condition group must contain at least one condition to evaluate.", exception.getMessage());
    }

    @Test
    void test_multipleConditionsWithLogicalNotShouldThrowException() {
        ComparisonException exception = Assertions.assertThrows(ComparisonException.class, () ->
            new ConditionGroup(
                    LogicalOperator.NOT,
                    List.of(
                            TestConditionFactory.getCondition_countryValueGb(ComparisonOperator.EQUALS),
                            TestConditionFactory.getCondition_revenueValue100M(ComparisonOperator.GREATER_THAN)
                    )
            ));
        Assertions.assertEquals("NOT operator can only have one condition to evaluate.", exception.getMessage());
    }

    @Test
    void test_MissingContextFieldShouldThrowMeaningfulException() {
        ComparisonCondition cond = TestConditionFactory.getCondition_industryValueTech(ComparisonOperator.EQUALS);
        Map<String, Object> facts = Map.of(
                "customer.annualRevenue", new BigDecimal("100000000"),
                "customer.country", "GB"
        );

        EvaluationContext ctx = new EvaluationContext(facts);
        ComparisonException exception = Assertions.assertThrows(ComparisonException.class, () -> cond.evaluate(ctx));

        Assertions.assertEquals("Field 'customer.industry' is missing from the evaluation context.", exception.getMessage());
    }

    @Test
    void test_nullFieldValueShouldReturnTrueIfAlsoNull() {
        ComparisonCondition cond = new ComparisonCondition(
                "customer.industry",
                ComparisonOperator.EQUALS,
                null
        );
        Map<String, Object> facts = new HashMap<>();
        facts.put("customer.industry", null);
        EvaluationContext ctx = new EvaluationContext(facts);
        boolean result = cond.evaluate(ctx);

        Assertions.assertTrue(result, "Null compared with null should return true");
    }

    @Test
    void test_nullFieldValueShouldReturnFalseIfFoundNullAgainstNotNull() {
        ComparisonCondition cond = TestConditionFactory.getCondition_industryValueTech(ComparisonOperator.EQUALS);
        Map<String, Object> facts = new HashMap<>();
        facts.put("customer.industry", null);
        EvaluationContext ctx = new EvaluationContext(facts);
        boolean result = cond.evaluate(ctx);

        Assertions.assertFalse(result, "Null compared with not null should return false");
    }

    @Test
    void test_gtValueComparisonWithEqualValuesReturnsFalse() {
        ConditionGroup cdg = TestConditionFactory.getCdg_andWithTwoConditions_countryEqAndRevenueGt();
        Map<String, Object> facts = Map.of(
                "customer.country", "GB",
                "customer.annualRevenue", new BigDecimal("100000000")
        );
        EvaluationContext ctx = new EvaluationContext(facts);

        boolean result = cdg.evaluate(ctx);

        Assertions.assertFalse(result, "Result with equal annual revenues should return false when condition is greater than not greater than or equals");
    }

    @Test
    void test_bigDecimalWithOnePointEqualsBigDecimalWithTwoPoints() {
        ComparisonCondition cond = TestConditionFactory.getCondition_amountWithTwoDecimals(ComparisonOperator.EQUALS);
        Map<String, Object> facts = Map.of(
                "customer.amount", new BigDecimal("1000.0")
        );
        EvaluationContext ctx = new EvaluationContext(facts);

        boolean result = cond.evaluate(ctx);
        Assertions.assertTrue(result, "BigDecimal constructed with one decimal point should equal the same amount constructed with two decimal points.");
    }
}
