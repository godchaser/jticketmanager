package org.duckdns.valci.jticketmanager;

import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Observable;

import org.duckdns.valci.jticketmanager.data.TicketsSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.sqlcontainer.OptimisticLockException;
import com.vaadin.ui.Notification;

public class TicketsModel extends Observable implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private TicketsSQLContainer ticketsSQLContainer;

    static final Logger LOG = LoggerFactory.getLogger(TicketsModel.class);

    public TicketsModel(TicketsController ticketController) {
        LOG.trace("Model instance created and Ticket SQL DB Container initialized");
        ticketsSQLContainer = new TicketsSQLContainer();
        LOG.trace("Registering controller observer");
        addObserver(ticketController);
    }

    public TicketsSQLContainer getTicketsSQLContainer() {
        // initDatabase();
        return ticketsSQLContainer;
    }

    @SuppressWarnings("unchecked")
    public void addNewTicket() {
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

        LOG.trace("Setting timestamp: " + timeStamp);
        ticketsSQLContainer.getContainer()
                .getContainerProperty(ticketId, TicketsSQLContainer.propertyIds.TICKETUPDATE.toString())
                .setValue(timeStamp);

        LOG.trace("now trying to add new ticket: " + ticketId.toString());
        if (commitToContainer()) {
            LOG.trace("commit success, selecting updated value");
            Object newRowId = ticketsSQLContainer.getContainer().getItem(ticketsSQLContainer.getNewRowId());

            if (newRowId == null) {
                LOG.trace("RowId is null, so selecting last item in list");
                // this is workaround because seems that RowChangeId listener is
                // not working sometimes so we can select last item in list
                newRowId = ticketsSQLContainer.getContainer().lastItemId();
            }
            LOG.trace("Updated new row in table: " + newRowId);
            setChanged();
            notifyObservers(newRowId);
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

    // TODO: this has to be decoupled

    @SuppressWarnings("unchecked")
    public void saveTicket(FieldGroup fieldGroup, Object itemID, String ticketCategory, String ticketPriority,
            String ticketStatus) {
        LOG.trace("now trying to save ticket");

        commitFieldGroup(fieldGroup);

        ticketsSQLContainer.getContainer()
                .getContainerProperty(itemID, TicketsSQLContainer.propertyIds.TICKETCATEGORY.toString())
                .setValue(ticketCategory);

        ticketsSQLContainer.getContainer()
                .getContainerProperty(itemID, TicketsSQLContainer.propertyIds.TICKETPRIORITY.toString())
                .setValue(ticketPriority);

        ticketsSQLContainer.getContainer()
                .getContainerProperty(itemID, TicketsSQLContainer.propertyIds.TICKETSTATUS.toString())
                .setValue(ticketStatus);

        String timeStamp = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(Calendar.getInstance().getTime());
        ticketsSQLContainer.getContainer()
                .getContainerProperty(itemID, TicketsSQLContainer.propertyIds.TICKETUPDATE.toString())
                .setValue(timeStamp);

        if (commitToContainer()) {
            LOG.trace("commit success, selecting updated value");
            setChanged();
            notifyObservers(itemID);
        }
    }

    private boolean commitToContainer() {
        boolean commitSuccess = false;
        try {
            LOG.trace("trying to commit change to sql db");
            ticketsSQLContainer.getContainer().commit();
            commitSuccess = true;
        } catch (UnsupportedOperationException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: UnsupportedOperationException" + e);
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            LOG.trace("commit failed: SQLException" + e);
            e.printStackTrace();
        } catch (OptimisticLockException e) {
            LOG.trace("Caught OptimisticLockException, should refresh page - mid air collision");
            Notification.show("Mid air collision detected",
                    "Someone already updated ticket you are using, refreshing page", Notification.Type.WARNING_MESSAGE);
            e.printStackTrace();
        }
        return commitSuccess;
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
