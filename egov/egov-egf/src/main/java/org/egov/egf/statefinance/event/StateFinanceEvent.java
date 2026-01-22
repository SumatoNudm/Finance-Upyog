package org.egov.egf.statefinance.event;


import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class StateFinanceEvent extends ApplicationEvent {

    Object data;
    StateFinanceEventType stateFinanceEventType;
    String tenantId;
    String token;
    String domainName;


    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public StateFinanceEvent(Object source, Object data, StateFinanceEventType stateFinanceEventType, String tenantId, String token, String domainName) {
        super(source);
        this.data = data;
        this.stateFinanceEventType = stateFinanceEventType;
        this.tenantId = tenantId;
        this.token = token;
        this.domainName = domainName;
    }


}
