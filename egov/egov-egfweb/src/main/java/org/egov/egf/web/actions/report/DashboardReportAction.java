package org.egov.egf.web.actions.report;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.egov.egf.model.DashboardReport;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.model.masters.Contractor;
import org.egov.services.report.DashboardReportService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@ParentPackage("egov")
@Results({
        @Result(name = "viewReport", location = "dashboardReport-viewReport.jsp")
})
public class DashboardReportAction extends BaseFormAction {


    @Autowired
    private DashboardReportService dashboardReportService;

    private DashboardReport dashboardReport = new DashboardReport();


    @Override
    public Object getModel() {
        return dashboardReport;
    }


    @SkipValidation
    @Action(value = "/report/dashboardReport-viewReport")
    public String viewReport() {

        List<Contractor> contractorList = getAllContractors();
        dashboardReport.setContractors(contractorList);

        dashboardReport.setTotalSuppliers(dashboardReportService.getSuppliersCount());

        dashboardReport.setTotalExpenseBills(dashboardReportService.getTotalExpenseBillsCreated());

        dashboardReport.setTotalBills(dashboardReportService.getTotalBills());

        dashboardReport.setTotalAmount(dashboardReportService.getTotalExpenseBillAmount());

        dashboardReport.setMessage("Hello world !");
        return "viewReport";
    }

    private List<Contractor> getAllContractors() {
        List<Contractor> contractorList = dashboardReportService.getAllContractors();
        return contractorList;
    }


}
