package org.egov.services.report;

import org.egov.commons.repository.FundRepository;
import org.egov.egf.commons.bankaccount.repository.BankAccountRepository;
import org.egov.egf.expensebill.repository.ExpenseBillRepository;
import org.egov.egf.masters.repository.ContractorRepository;
import org.egov.egf.masters.repository.PurchaseOrderRepository;
import org.egov.egf.masters.repository.SupplierRepository;
import org.egov.egf.masters.repository.WorkOrderRepository;
import org.egov.egf.voucher.repository.JournalVoucherRepository;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.masters.Contractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardReportService {


    @Autowired
    @Qualifier("persistenceService")
    private PersistenceService persistenceService;

    @Autowired
    private ContractorRepository contractorRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ExpenseBillRepository expenseBillRepository;

    @Autowired
    private WorkOrderRepository workOrderRepository;

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private JournalVoucherRepository journalVoucherRepository;

    @Autowired
    private FundRepository fundRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;




    public Long getContractorsCount() {
        return contractorRepository.count();
    }

    public Long getSuppliersCount() {
       return supplierRepository.count();
    }


    public Long getTotalBillsCreated(String type) {
        Long count = 0L;
       if (type == null || type.isEmpty()) {
           count =  expenseBillRepository.count();
        } else {
            count = expenseBillRepository.countByExpendituretype(type);
        }
        return  count;
    }

    public Long getTotalWorkOrdersCount() {
        return workOrderRepository.count();
    }


    public Long getTotalPurchaseOrderCount() {
        return purchaseOrderRepository.count();
    }

    public Long getTotalJournalVoucherCount() {
        return journalVoucherRepository.count();
    }

    public Long getTotalsFundsCount() {
        return fundRepository.count();
    }


    public Long getTotalBankAccountCount() {
        return bankAccountRepository.count();
    }

    public Long getTotalContractorCount() {
        return contractorRepository.count();
    }

    public Long getTotalSupplierCount() {
        return supplierRepository.count();
    }


    public Long getTotalPaymentCount() {
        return 0L;
    }




}
