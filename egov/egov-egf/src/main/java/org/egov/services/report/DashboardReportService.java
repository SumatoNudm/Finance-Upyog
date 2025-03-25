package org.egov.services.report;

import org.apache.log4j.Logger;
import org.egov.commons.dao.FinancialYearHibernateDAO;
import org.egov.commons.repository.FundRepository;
import org.egov.egf.commons.bankaccount.repository.BankAccountRepository;
import org.egov.egf.expensebill.repository.ExpenseBillRepository;
import org.egov.egf.masters.repository.ContractorRepository;
import org.egov.egf.masters.repository.PurchaseOrderRepository;
import org.egov.egf.masters.repository.SupplierRepository;
import org.egov.egf.masters.repository.WorkOrderRepository;
import org.egov.egf.voucher.repository.JournalVoucherRepository;
import org.egov.infra.config.security.repository.ApplicationSecurityRepository;
import org.egov.infstr.services.PersistenceService;
import org.egov.model.masters.Contractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

    @Autowired
    private FinancialYearHibernateDAO financialYearHibernateDAO;


    private static final Logger LOGGER = Logger.getLogger(DashboardReportService.class);


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
           String startDate = financialYearHibernateDAO.getCurrYearStartDate();
           LOGGER.info("mridx! "+ startDate);
           final String temp[] = startDate.split("-");
           final String temp1[] = temp[2].split(" ");
           final Date dt = new Date();
           final Date dt1 = new Date();

           final SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
           final GregorianCalendar calendar = new GregorianCalendar();
           calendar.setTime(dt1);
           calendar.set(Calendar.YEAR, Integer.parseInt(temp[0]));
           calendar.set(Calendar.MONTH, Integer.parseInt(temp[1]) - 1);
           calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(temp1[0]));
           startDate = formatter.format(calendar.getTime());

           calendar.setTime(dt);
           calendar.set(Calendar.YEAR, Integer.parseInt(temp[0]));
//           count = expenseBillRepository.countByExpenditureTypeAndBillDateBetween(type, )
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
        return journalVoucherRepository.getPaymentsCount();
    }







}
