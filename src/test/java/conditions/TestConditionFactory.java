package conditions;

import java.math.BigDecimal;
import java.util.List;

class TestConditionFactory {

    static ConditionGroup getCdg_andWithTwoConditions_countryEqAndRevenueGt() {
        return new ConditionGroup(
                LogicalOperator.AND,
                List.of(
                        getCondition_countryValueGb(ComparisonOperator.EQUALS),
                        getCondition_revenueValue100M(ComparisonOperator.GREATER_THAN))
        );
    }

    static ConditionGroup getCdg_orWithTwoConditions_countryEqAndRevenueGt() {
        return new ConditionGroup(
                LogicalOperator.OR,
                List.of(
                        getCondition_countryValueGb(ComparisonOperator.EQUALS),
                        getCondition_revenueValue100M(ComparisonOperator.GREATER_THAN)
                )
        );
    }

    static ConditionGroup getCdg_not_countryEq() {
        return new ConditionGroup(
                LogicalOperator.NOT,
                List.of(
                        getCondition_countryValueGb(ComparisonOperator.EQUALS)
                )
        );
    }

    static ConditionGroup getNestedCdg_andWithNestedOr() {
        return new ConditionGroup(
                LogicalOperator.AND,
                List.of(
                        getCondition_industryValueTech(ComparisonOperator.EQUALS),
                        getCdg_orWithTwoConditions_countryEqAndRevenueGt()
                )
        );
    }

    static ComparisonCondition getCondition_countryValueGb(ComparisonOperator op) {
        return new ComparisonCondition(
                "customer.country",
                op,
                "GB"
        );
    }

    static ComparisonCondition getCondition_revenueValue100M(ComparisonOperator op) {
        return new ComparisonCondition(
                "customer.annualRevenue",
                op,
                new BigDecimal("100000000")
        );
    }

    static ComparisonCondition getCondition_industryValueTech(ComparisonOperator op) {
        return new ComparisonCondition(
                "customer.industry",
                op,
                "Technology"
        );
    }

    static ComparisonCondition getCondition_amountWithTwoDecimals(ComparisonOperator op) {
        return new ComparisonCondition(
                "customer.amount",
                op,
                new BigDecimal("1000.00")
        );
    }
}
