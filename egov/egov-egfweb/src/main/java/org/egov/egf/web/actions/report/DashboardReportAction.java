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

    private final DashboardReport dashboardReport = new DashboardReport();


    @Override
    public Object getModel() {
        return dashboardReport;
    }


    @SkipValidation
    @Action(value = "/report/dashboardReport-viewReport")
    public String viewReport() {


        // Total expense bills
        dashboardReport.setTotalExpenseBills(dashboardReportService.getTotalBillsCreated("Expense"));

        // Total contractors bills
        dashboardReport.setTotalContractorBills(dashboardReportService.getTotalBillsCreated("Works"));

        // Total supplier bills
        dashboardReport.setTotalSupplierBills(dashboardReportService.getTotalBillsCreated("Purchase"));

        // Total work orders
        dashboardReport.setTotalWorkOrders(dashboardReportService.getTotalWorkOrdersCount());

        // Total purchase order
        dashboardReport.setTotalPurchaseOrders(dashboardReportService.getTotalPurchaseOrderCount());

        // Total journal voucher
        dashboardReport.setTotalJournalVouchers(dashboardReportService.getTotalJournalVoucherCount());

        // Total fund
        dashboardReport.setTotalFunds(dashboardReportService.getTotalsFundsCount());

        // Total bank accounts
        dashboardReport.setTotalBankAccounts(dashboardReportService.getTotalBankAccountCount());

        // Total Contractors
        dashboardReport.setTotalContractors(dashboardReportService.getContractorsCount());

        // Total supplier
        dashboardReport.setTotalSuppliers(dashboardReportService.getTotalSupplierCount());

        // Total payments
        dashboardReport.setTotalBillsPayment(dashboardReportService.getTotalPaymentCount());


        return "viewReport";
    }



}
