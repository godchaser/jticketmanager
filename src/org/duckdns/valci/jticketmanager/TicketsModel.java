package org.duckdns.valci.jticketmanager;

import org.duckdns.valci.jtricketmanager.data.TicketsSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.sqlcontainer.SQLContainer;

public class TicketsModel {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private SQLContainer ticketsSQLContainer;

    static final Logger LOG = LoggerFactory.getLogger(TicketsModel.class);

    public TicketsModel(TicketsSQLContainer ticketsSQLContainerInstance) {
        this.ticketsSQLContainer = ticketsSQLContainerInstance.getContainer();
    }

    public SQLContainer getTicketsSQLContainer() {
        return ticketsSQLContainer;
    }

}
