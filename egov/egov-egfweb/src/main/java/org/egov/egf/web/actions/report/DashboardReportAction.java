package org.egov.egf.web.actions.report;


import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.json.annotations.JSON;
import org.codehaus.jackson.JsonGenerator;
import org.egov.egf.model.DashboardReport;
import org.egov.infra.web.struts.actions.BaseFormAction;
import org.egov.model.masters.Contractor;
import org.egov.services.report.DashboardReportService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@ParentPackage("egov")
@Results({
        @Result(name = "viewReport", location = "dashboardReport-viewReport.jsp"),
        @Result(name = "viewForm", location = "dashboardReport-viewForm.jsp")
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

        HttpServletRequest request = ServletActionContext.getRequest();

      String startDate =  request.getParameter("startDate");
       String endDate = request.getParameter("endDate");



       dashboardReport.setDataS(startDate + " : "+ endDate);


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


    @SkipValidation
    @Action(value = "/report/dashboardReport-viewForm")
    public String viewForm() {
        return "viewForm";
    }


    @SkipValidation
    @Action(value = "/report/dashboardReport-viewFilteredReport")
    public String viewFilteredReport() {

        HttpServletRequest request = ServletActionContext.getRequest();


        try {

            String sDate =  request.getParameter("startDate");
            String eDate = request.getParameter("endDate");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

            Date startDate =  simpleDateFormat.parse(sDate);
            Date endDate =  simpleDateFormat.parse(eDate);


            // Total expense bills
//        dashboardReport.setTotalExpenseBills(dashboardReportService.getTotalBillsCreated("Expense"));

            dashboardReport.setTotalExpenseBills(dashboardReportService.getTotalBillsCreated("Expense", startDate, endDate));

            // Total contractors bills
            dashboardReport.setTotalContractorBills(dashboardReportService.getTotalBillsCreated("Works", startDate, endDate));

            // Total supplier bills
            dashboardReport.setTotalSupplierBills(dashboardReportService.getTotalBillsCreated("Purchase", startDate, endDate));

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





        } catch (Exception e) {
            e.printStackTrace();
        }





        return "viewReport";
    }






}
