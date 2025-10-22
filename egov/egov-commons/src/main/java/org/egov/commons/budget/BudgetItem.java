package org.egov.commons.budget;


import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.SafeHtml;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = BudgetItem.TABLE_NAME)
@Unique(id = "id", tableName = BudgetItem.TABLE_NAME, enableDfltMsg = true)
@SequenceGenerator(name = BudgetItem.SEQ_BUDGET_ITEM, sequenceName = BudgetItem.SEQ_BUDGET_ITEM, allocationSize = 1)
@Audited
public class BudgetItem extends AbstractAuditable {
    public static final String TABLE_NAME = "egf_budgetitem";
    public static final String SEQ_BUDGET_ITEM = "seq_egf_budgetitem";


    @Id
    @GeneratedValue(generator = SEQ_BUDGET_ITEM, strategy = GenerationType.SEQUENCE)
    private Long id;


    @Column(name = "functionid")
    private Long functionId;


    @Column(name = "budgetheadid")
    private Long budgetHeadId;


    @Column(name = "financialyearid")
    private Long financialYearId;

    @Column(name = "currentfinancialyearid")
    private Long currentFinancialYearId;


    @Column(name = "budgetcode")
    private String budgetCode;


    @NotNull
    @Column(name = "budgetgroup")
    private String budgetGroup;


    @NotNull
    @Column(name = "currentestimate", precision = 13, scale = 2)
    private BigDecimal currentEstimate;


    @NotNull
    @Column(name = "currentactual",  precision = 13, scale = 2)
    private BigDecimal currentActual;


    @NotNull
    @Column(name = "currentrevisedestimate",  precision = 13, scale = 2)
    private BigDecimal currentRevisedEstimate;


    @NotNull
    @Column(name = "nextestimate",  precision = 13, scale = 2)
    private BigDecimal nextEstimate;



    @Override
    public Long getId() {
        return id;
    }

    @Override
    protected void setId(Long id) {
        this.id = id;
    }

    public void setFunctionId(Long functionId) {
        this.functionId = functionId;
    }

    public Long getFunctionId() {
        return functionId;
    }

    public void setBudgetHeadId(Long budgetHeadId) {
        this.budgetHeadId = budgetHeadId;
    }

    public Long getBudgetHeadId() {
        return budgetHeadId;
    }

    public Long getFinancialYearId() {
        return financialYearId;
    }

    public void setFinancialYearId(Long financialYearId) {
        this.financialYearId = financialYearId;
    }

    public Long getCurrentFinancialYearId() {
        return currentFinancialYearId;
    }

    public void setCurrentFinancialYearId(Long currentFinancialYearId) {
        this.currentFinancialYearId = currentFinancialYearId;
    }

    public void setBudgetCode(String budgetCode) {
        this.budgetCode = budgetCode;
    }

    public String getBudgetCode() {
        return budgetCode;
    }

    public void setBudgetGroup(String budgetGroup) {
        this.budgetGroup = budgetGroup;
    }

    public String getBudgetGroup() {
        return budgetGroup;
    }

    public BigDecimal getCurrentEstimate() {
        return currentEstimate;
    }

    public void setCurrentEstimate(BigDecimal currentEstimate) {
        this.currentEstimate = currentEstimate;
    }

    public BigDecimal getCurrentActual() {
        return currentActual;
    }

    public void setCurrentActual(BigDecimal currentActual) {
        this.currentActual = currentActual;
    }

    public BigDecimal getCurrentRevisedEstimate() {
        return currentRevisedEstimate;
    }

    public void setCurrentRevisedEstimate(BigDecimal currentRevisedEstimate) {
        this.currentRevisedEstimate = currentRevisedEstimate;
    }

    public BigDecimal getNextEstimate() {
        return nextEstimate;
    }

    public void setNextEstimate(BigDecimal nextEstimate) {
        this.nextEstimate = nextEstimate;
    }
}
