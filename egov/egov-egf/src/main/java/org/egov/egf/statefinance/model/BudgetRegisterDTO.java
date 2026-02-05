package org.egov.egf.statefinance.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BudgetRegisterDTO {

    private Long id;

    private Long budgetRegisterId;

    private String tenantId;

    private String cityName;

    private String budgetRegisterNumber;

    private String budgetRegisterName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startingDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endingDate;

    private String currentFy;

    private String nextFy;


}
