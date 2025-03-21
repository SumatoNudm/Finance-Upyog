package org.egov.services.report;

import org.egov.egf.expensebill.repository.ExpenseBillRepository;
import org.egov.egf.masters.repository.ContractorRepository;
import org.egov.egf.masters.repository.SupplierRepository;
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


    public List<Contractor> getAllContractors() {
        List<Contractor> allContractors = contractorRepository.findAll();
        return allContractors;
    }

    public Long getSuppliersCount() {
       return supplierRepository.count();
    }

    public Long getTotalExpenseBillsCreated() {
        return expenseBillRepository.countByExpendituretype("Expense");
    }

    public Long getTotalBills() {
        return expenseBillRepository.count();
    }

    public Long getAllBillsAmount() {
        return expenseBillRepository.totalAmountOfBills();
    }

    public Long getTotalExpenseBillAmount() {
        return expenseBillRepository.totalAmountOfBills();
    }





}
