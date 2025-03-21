package org.egov.egf.model;

import org.egov.model.masters.Contractor;

import java.util.ArrayList;
import java.util.List;

public class DashboardReport {


    private List<Contractor> contractorList = new ArrayList<>();

    private String message;

    private Long totalSuppliers;

    private Long totalExpenseBills;

    private Long totalBills;

    private Long totalAmount;


    public DashboardReport() {

    }

    public DashboardReport(List<Contractor> contractors) {
        this.contractorList = contractors;
    }


    public void setContractors(List<Contractor> contractors) {
        this.contractorList = contractors;
    }


    public List<Contractor> getContractors() {
        return this.contractorList;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setTotalSuppliers(Long count) {
        this.totalSuppliers = count;
    }

    public Long getTotalSuppliers() {
        return totalSuppliers;
    }

    public void setTotalExpenseBills(Long totalExpenseBills) {
        this.totalExpenseBills = totalExpenseBills;
    }

    public Long getTotalExpenseBills() {
        return totalExpenseBills;
    }


    public void setTotalBills(Long totalBills) {
        this.totalBills = totalBills;
    }

    public Long getTotalBills() {
        return totalBills;
    }

    public void setTotalAmount(Long amount) {
        this.totalAmount = amount;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }


}
