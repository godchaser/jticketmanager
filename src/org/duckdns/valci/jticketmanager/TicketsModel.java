package org.duckdns.valci.jticketmanager;

import java.io.Serializable;
import java.sql.SQLException;

import org.duckdns.valci.jtricketmanager.data.TicketsSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.Table;

public class TicketsModel implements Serializable {
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

    @SuppressWarnings("unchecked")
    public void addNewTicket(Table ticketList, FieldGroup fieldGroup) {
        this.ticketsSQLContainer.removeAllContainerFilters();
        Object ticketId = this.ticketsSQLContainer.addItem();
        LOG.trace("now trying to add new ticket: " + ticketId.toString());
        try {
            fieldGroup.commit();
        } catch (CommitException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        /*
         * ticketList.getContainerProperty(ticketId,
         * TicketsSQLContainer.propertyIds.ticketCategory.toString())
         * .setValue("New Ticket"); ticketList.getContainerProperty(ticketId,
         * TicketsSQLContainer.propertyIds.ticketSubject.toString())
         * .setValue("New Subject");
         */
        try {
            this.ticketsSQLContainer.commit();
            ticketList.select(ticketId);
        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        ticketList.select(ticketId);
    }

    public void removeTicket(Object ticket) {
        LOG.trace("now deleting ticket: " + ticket.toString());
        this.ticketsSQLContainer.removeItem(ticket);
        try {
            LOG.trace("trying to commit ticket deletion to sql db");
            this.ticketsSQLContainer.commit();
        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: UnsupportedOperationException" + e);
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: SQLException" + e);
            e.printStackTrace();
        }

    }

    public void saveTicket(FieldGroup editorFields) {
        try {
            LOG.trace("now trying to save ticket");
            editorFields.commit();
        } catch (CommitException e) {
            LOG.trace("Unable to save ticket");
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            LOG.trace("trying to commit ticket save to sql db");
            this.ticketsSQLContainer.commit();
        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: UnsupportedOperationException" + e);
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: SQLException" + e);
            e.printStackTrace();
        }
    }

}
