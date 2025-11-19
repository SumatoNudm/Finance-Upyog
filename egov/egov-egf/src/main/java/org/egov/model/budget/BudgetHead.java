package org.egov.model.budget;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.egov.infra.persistence.entity.AbstractAuditable;
import org.egov.infra.persistence.validator.annotation.Required;
import org.egov.infra.persistence.validator.annotation.Unique;
import org.egov.utils.BudgetAccountType;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;

@Entity
@Table(name = "EGF_BUDGETHEAD")
@SequenceGenerator(name = BudgetHead.SEQ_BUDGETHEAD, sequenceName = BudgetHead.SEQ_BUDGETHEAD, allocationSize = 1)
@Unique(fields = "code", enableDfltMsg = true)
public class BudgetHead extends AbstractAuditable {

    public static final String SEQ_BUDGETHEAD = "SEQ_EGF_BUDGETHEAD";
    private static final long serialVersionUID = 202502091745000L;

    public static final String TABLE_NAME = "EGF_BUDGETHEAD";

    @Id
    @GeneratedValue(generator = SEQ_BUDGETHEAD, strategy = GenerationType.SEQUENCE)
    private Long id;

    @SafeHtml
    @Required(message = "Name should not be empty")
    @Length(max = 250)
    private String name;

    @SafeHtml
    @Required(message = "Code should not be empty")
    @Length(max = 20)
    private String code;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Please select accounttype")
    private BudgetAccountType accountType;

    @SafeHtml
    @Length(max = 5)
    private String accountTypeCode;

    @SafeHtml
    @Length(max = 5)
    private String program;

    @SafeHtml
    @NotNull
    private String category;

    private Boolean isactive;

    @SafeHtml
    @Column(name = "state_code")
    private String stateCode;

    // --- Getters and Setters ---
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public BudgetAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(final BudgetAccountType accountType) {
        this.accountType = accountType;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(final String program) {
        this.program = program;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getIsactive() {
        return isactive;
    }

    public void setIsactive(Boolean isactive) {
        this.isactive = isactive;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(final String stateCode) {
        this.stateCode = stateCode;
    }



}
