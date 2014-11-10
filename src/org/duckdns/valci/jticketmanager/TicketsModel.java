package org.duckdns.valci.jticketmanager;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.duckdns.valci.jticketmanager.data.TicketsSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;

public class TicketsModel implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TicketsSQLContainer ticketsSQLContainer;

    static final Logger LOG = LoggerFactory.getLogger(TicketsModel.class);

    public TicketsModel(TicketsSQLContainer ticketsSQLContainerInstance) {
        ticketsSQLContainer = ticketsSQLContainerInstance;
    }

    public TicketsSQLContainer getTicketsSQLContainer() {
        return ticketsSQLContainer;
    }

    @SuppressWarnings("unchecked")
    public void addNewTicket(Table ticketList, FieldGroup fieldGroup) {
        ticketsSQLContainer.getContainer().removeAllContainerFilters();
        Object ticketId = ticketsSQLContainer.getContainer().addItem();
        LOG.trace("Setting default values to container");

        ticketsSQLContainer
                .getContainer()
                .getContainerProperty(ticketId, TicketsSQLContainer.propertyIds.TICKETCATEGORY.toString())
                .setValue(
                        TicketsSQLContainer.getDefaultFields().get(
                                TicketsSQLContainer.propertyIds.TICKETCATEGORY.toString()));

        ticketsSQLContainer
                .getContainer()
                .getContainerProperty(ticketId, TicketsSQLContainer.propertyIds.TICKETPRIORITY.toString())
                .setValue(
                        TicketsSQLContainer.getDefaultFields().get(
                                TicketsSQLContainer.propertyIds.TICKETPRIORITY.toString()));

        ticketsSQLContainer
                .getContainer()
                .getContainerProperty(ticketId, TicketsSQLContainer.propertyIds.TICKETSTATUS.toString())
                .setValue(
                        TicketsSQLContainer.getDefaultFields().get(
                                TicketsSQLContainer.propertyIds.TICKETSTATUS.toString()));

        ticketsSQLContainer
                .getContainer()
                .getContainerProperty(ticketId, TicketsSQLContainer.propertyIds.TICKETSUBJECT.toString())
                .setValue(
                        TicketsSQLContainer.getDefaultFields().get(
                                TicketsSQLContainer.propertyIds.TICKETSUBJECT.toString()));

        ticketsSQLContainer
                .getContainer()
                .getContainerProperty(ticketId, TicketsSQLContainer.propertyIds.TICKETASSIGNEE.toString())
                .setValue(
                        TicketsSQLContainer.getDefaultFields().get(
                                TicketsSQLContainer.propertyIds.TICKETASSIGNEE.toString()));

        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());

        LOG.trace("Setting timestamp" + timeStamp);
        ticketsSQLContainer.getContainer()
                .getContainerProperty(ticketId, TicketsSQLContainer.propertyIds.TICKETUPDATE.toString())
                .setValue(timeStamp);

        LOG.trace("now trying to add new ticket: " + ticketId.toString());
        commitToContainer();
        Object newRowId = ticketsSQLContainer.getContainer().getItem(ticketsSQLContainer.getNewRowId());

        if (newRowId != null) {
            LOG.trace("Selecting new row in table: " + newRowId);
            ticketList.select(newRowId);
        } else {
            // this is workaround because seems that RowChangeId listener is not working
            Object lastRowId = ticketsSQLContainer.getContainer().lastItemId();
            LOG.trace("Selecting last row in table: " + lastRowId);
            ticketList.select(lastRowId);
        }
    }

    public void removeTicket(Object ticket) {
        if (ticket != null) {
            LOG.trace("now deleting ticket: " + ticket.toString());
            ticketsSQLContainer.getContainer().removeItem(ticket);
            commitToContainer();
        } else {
            LOG.trace("Cannot delete null ticket");
        }
    }

    @SuppressWarnings("unchecked")
    public void saveTicket(FieldGroup fieldGroup, Table ticketTable, ComboBox categoryComboBox,
            ComboBox priorityComboBox, ComboBox statusComboBox) {
        LOG.trace("now trying to save ticket");

        commitFieldGroup(fieldGroup);

        ticketsSQLContainer
                .getContainer()
                .getContainerProperty(ticketTable.getValue(), TicketsSQLContainer.propertyIds.TICKETCATEGORY.toString())
                .setValue(categoryComboBox.getValue().toString());

        ticketsSQLContainer
                .getContainer()
                .getContainerProperty(ticketTable.getValue(), TicketsSQLContainer.propertyIds.TICKETPRIORITY.toString())
                .setValue(priorityComboBox.getValue().toString());

        ticketsSQLContainer.getContainer()
                .getContainerProperty(ticketTable.getValue(), TicketsSQLContainer.propertyIds.TICKETSTATUS.toString())
                .setValue(statusComboBox.getValue().toString());

        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());
        ticketsSQLContainer.getContainer()
                .getContainerProperty(ticketTable.getValue(), TicketsSQLContainer.propertyIds.TICKETUPDATE.toString())
                .setValue(timeStamp);

        commitToContainer();
        ticketTable.select(ticketTable.getValue());
    }

    private void commitToContainer() {
        try {
            LOG.trace("trying to commit change to sql db");
            ticketsSQLContainer.getContainer().commit();
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
