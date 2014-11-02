package org.duckdns.valci.jticketmanager;

import java.sql.SQLException;

import org.duckdns.valci.jtricketmanager.data.TicketsSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.Table;

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

	@SuppressWarnings("unchecked")
	public void addNewTicket(Table ticketList) {
		this.ticketsSQLContainer.removeAllContainerFilters();
		Object ticketId = this.ticketsSQLContainer.addItemAt(0);
		/*
		 * Each Item has a set of Properties that hold values. Here we set a
		 * couple of those.
		 */
		ticketList.getContainerProperty(ticketId, TicketsSQLContainer.propertyIds.ticketCategory.toString()).setValue("New Ticket");
		ticketList.getContainerProperty(ticketId, TicketsSQLContainer.propertyIds.ticketSubject.toString()).setValue("New Subject");
		/* Lets choose the newly created contact to edit it. */
		ticketList.select(ticketId);
		try {
			this.ticketsSQLContainer.commit();
		} catch (UnsupportedOperationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
