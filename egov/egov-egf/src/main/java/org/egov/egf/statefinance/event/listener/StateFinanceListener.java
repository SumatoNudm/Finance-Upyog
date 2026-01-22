package org.egov.egf.statefinance.event.listener;


import org.egov.egf.statefinance.event.StateFinanceEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class StateFinanceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateFinanceService.class);

    @Autowired
    StateFinanceService stateFinanceService;

    @Async
    @EventListener
    public void handleEvent(StateFinanceEvent event) throws InterruptedException {
        stateFinanceService.pushToStateFinance(event);
    }

}
