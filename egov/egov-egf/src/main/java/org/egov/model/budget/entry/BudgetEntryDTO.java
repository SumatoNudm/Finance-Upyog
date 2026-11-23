package org.egov.model.budget.entry;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.python.antlr.ast.Str;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BudgetEntryDTO {

    private Long id;

    private Long functionId;

    private String functionName;

    private String functionCode;

    private Long budgetHeadId;

    private String budgetHeadName;

    private String budgetHeadCode;

    private String budgetHeadAccountType;

    private String budgetHeadAccountTypeCode;

    private String budgetHeadCategory;

    private String budgetHeadStateCode;

    private String budgetCode;

    private BigDecimal currentEstimate;

    private BigDecimal currentActual;

    private BigDecimal currentRevisedEstimate;

    private BigDecimal nextEstimate;

    private Integer schemeId;

    private String schemeCode;

    private String schemeName;

    private String stateBudgetCode;


}
