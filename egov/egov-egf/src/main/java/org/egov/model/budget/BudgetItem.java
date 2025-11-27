package org.egov.model.budget;


import lombok.Getter;
import lombok.Setter;

import org.egov.commons.CFinancialYear;
import org.egov.commons.CFunction;
import org.egov.commons.Scheme;
import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Unique;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = BudgetItem.TABLE_NAME)
@Unique(id = "id", tableName = BudgetItem.TABLE_NAME, enableDfltMsg = true)
@SequenceGenerator(name = BudgetItem.SEQ_BUDGET_ITEM, sequenceName = BudgetItem.SEQ_BUDGET_ITEM, allocationSize = 1)
@Setter
@Getter
public class BudgetItem extends AbstractAuditable {
    public static final String TABLE_NAME = "egf_budgetitem";
    public static final String SEQ_BUDGET_ITEM = "seq_egf_budgetitem";


    @Id
    @GeneratedValue(generator = SEQ_BUDGET_ITEM, strategy = GenerationType.SEQUENCE)
    private Long id;


    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "functionid")
    private CFunction function;

    // @Column(name = "functionid")
    // private Long function;

    @ManyToOne
    @JoinColumn(name = "budgetheadid")
    private BudgetHead budgetHead;

    @ManyToOne
    @JoinColumn(name = "financialyearid")
    private CFinancialYear financialYear; // budget being created for fy

    @ManyToOne
    @JoinColumn(name = "currentfinancialyearid")
    private CFinancialYear currentFinancialYear; // budget creating on fy

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_register_id")
    private BudgetRegister budgetRegister;


    @ManyToOne
    @JoinColumn(name = "schemeid")
    private Scheme scheme;

    @Column(name = "statebudgetcode")
    private String stateBudgetCode;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }


    public String toString() {
        String stringValue = "Function Id: " + function.getId() +
                ", Function Code: " + function.getCode() +
                ", Budget Head Id: " + budgetHead.getId() +
                ", Financial year: " + financialYear +
                ", Current Financial year: " + currentFinancialYear +
                ", Budget Code: " + budgetCode +
                ", Budget Group: " + budgetGroup +
                ", Current estimate: " + currentEstimate +
                ", Current Actual: " + currentActual +
                ", Current Revised: " + currentRevisedEstimate +
                ", Next Estimate: " + nextEstimate;

        return stringValue;
    }



}
