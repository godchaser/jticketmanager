package org.duckdns.valci.jticketmanager;

import java.io.Serializable;
import java.sql.SQLException;

import org.duckdns.valci.jtricketmanager.data.TicketsSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Table;

public class TicketsModel implements Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 1L;
	private TicketsSQLContainer ticketsSQLContainer;

	static final Logger LOG = LoggerFactory.getLogger(TicketsModel.class);

	public TicketsModel(TicketsSQLContainer ticketsSQLContainerInstance) {
		this.ticketsSQLContainer = ticketsSQLContainerInstance;
	}

	public TicketsSQLContainer getTicketsSQLContainer() {
		return ticketsSQLContainer;
	}

	public void addNewTicket(Table ticketList, FieldGroup fieldGroup) {
		this.ticketsSQLContainer.getContainer().removeAllContainerFilters();
		Object ticketId = this.ticketsSQLContainer.getContainer().addItem();
		LOG.trace("now trying to add new ticket: " + ticketId.toString());
		commitFieldGroup(fieldGroup);
		commitToContainer();
		Object newRowId = this.ticketsSQLContainer.getContainer().getItem(
				this.ticketsSQLContainer.getNewRowId());
		if (newRowId != null) {
			LOG.trace("Selecting new row in table: " + newRowId);
			ticketList.select(newRowId);
		} else {
			// this is workaround because seems that RowChangeId listener is not
			// working
			Object lastRowId = ticketsSQLContainer.getContainer().lastItemId();
			LOG.trace("Selecting last row in table: " + lastRowId);
			ticketList.select(lastRowId);
		}
	}

	public void removeTicket(Object ticket) {
		LOG.trace("now deleting ticket: " + ticket.toString());
		this.ticketsSQLContainer.getContainer().removeItem(ticket);
		commitToContainer();
	}

	public void saveTicket(FieldGroup fieldGroup) {
		LOG.trace("now trying to save ticket");
		commitFieldGroup(fieldGroup);
		commitToContainer();
	}

	private void commitToContainer() {
		try {
			LOG.trace("trying to commit change to sql db");
			this.ticketsSQLContainer.getContainer().commit();
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

	private void commitFieldGroup(FieldGroup fieldGroup) {
		try {
			LOG.trace("now trying to commit field group values");
			fieldGroup.commit();
		} catch (CommitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

}
