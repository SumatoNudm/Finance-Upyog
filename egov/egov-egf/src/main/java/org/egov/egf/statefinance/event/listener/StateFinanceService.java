package org.egov.egf.statefinance.event.listener;


import org.egov.egf.statefinance.event.StateFinanceEvent;
import org.egov.egf.statefinance.event.StateFinanceEventType;
import org.egov.egf.statefinance.model.BudgetRegisterRequestWrapper;
import org.egov.egf.statefinance.model.BudgetRegisterWrapper;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.exception.ApplicationRuntimeException;
import org.egov.infra.microservice.models.RequestInfo;
import org.egov.infra.microservice.utils.MicroserviceUtils;
import org.egov.model.budget.BudgetRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Transactional(readOnly = true)
@Service
public class StateFinanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateFinanceService.class);

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private MicroserviceUtils microserviceUtils;



    public void forwardBudgetForApproval(StateFinanceEventType stateFinanceEventType, Object data) {
        String tenantId = microserviceUtils.getTenentId();
        String token = microserviceUtils.generateAdminToken(tenantId);
        String domainName = ApplicationThreadLocals.getDomainName();
        StateFinanceEvent event = new StateFinanceEvent(this, data, stateFinanceEventType, tenantId, token, domainName);
        applicationEventPublisher.publishEvent(event);
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public void pushToStateFinance(StateFinanceEvent event) {
        try {

            Object data = event.getData();
            String tenantId = event.getTenantId();
            String token = event.getToken();
            String domainName = event.getDomainName();

            this.prepareThreadLocal(tenantId, domainName);


            BudgetRegisterRequestWrapper budgetRegisterRequestWrapper = new BudgetRegisterRequestWrapper();
            budgetRegisterRequestWrapper.setBudgetRegister((BudgetRegisterWrapper) data);
            RequestInfo requestInfo = new RequestInfo();
            requestInfo.setAuthToken(token);
            budgetRegisterRequestWrapper.setRequestInfo(requestInfo);

            LOGGER.info("push budget to state finance");
            microserviceUtils.pushBudgetToStateFinance(budgetRegisterRequestWrapper);

        } catch (ApplicationRuntimeException e) {
            LOGGER.error("ERROR while generation event to publish data to state finance");
        }

    }


    private void prepareThreadLocal(String tenant, String domainName) {
        ApplicationThreadLocals.setTenantID(tenant.split(Pattern.quote("."))[1]);
        ApplicationThreadLocals.setDomainName(domainName);
    }

}
