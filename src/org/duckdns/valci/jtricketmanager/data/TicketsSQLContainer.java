package org.duckdns.valci.jtricketmanager.data;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class TicketsSQLContainer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(TicketsSQLContainer.class);

    public static enum ticketCategories {
        FEATURE, BUG
    }

    public static enum ticketStatus {
        OPEN, ONGOING, CLOSED, FEEDBACK
    }

    public static enum ticketPriority {
        LOW, NORMAL, HIGH
    }

    public static enum propertyIds {
        ID, ticketCategory, ticketStatus, ticketPriority, ticketSubject, ticketAssignee, ticketUpdate;
    }

    private DatabaseHelper dbHelper = null;
    private SQLContainer ticketsContainer = null;
    private Object oldRowId = null;
    private Object newRowId = null;

    public SQLContainer getContainer() {
        return ticketsContainer;
    }

    public TicketsSQLContainer() {
        initContainers();
    }

    public static LinkedHashMap<String, String> getDbColumnsMap() {
        LinkedHashMap<String, String> propertyIdsMap = new LinkedHashMap<String, String>();
        propertyIdsMap.put(propertyIds.ID.toString(), "#");
        propertyIdsMap.put(propertyIds.ticketCategory.toString(), "Category");
        propertyIdsMap.put(propertyIds.ticketStatus.toString(), "Status");
        propertyIdsMap.put(propertyIds.ticketPriority.toString(), "Priority");
        propertyIdsMap.put(propertyIds.ticketSubject.toString(), "Subject");
        propertyIdsMap.put(propertyIds.ticketAssignee.toString(), "Assignee");
        propertyIdsMap.put(propertyIds.ticketUpdate.toString(), "Updated");
        return propertyIdsMap;
    }

    public static HashMap<String, String> getDefaultFields() {
        HashMap<String, String> defaultFields = new HashMap<String, String>();
        defaultFields.put(propertyIds.ticketCategory.toString(), ticketCategories.FEATURE.toString());
        defaultFields.put(propertyIds.ticketStatus.toString(), ticketStatus.OPEN.toString());
        defaultFields.put(propertyIds.ticketPriority.toString(), ticketPriority.NORMAL.toString());
        defaultFields.put(propertyIds.ticketSubject.toString(), "");
        defaultFields.put(propertyIds.ticketAssignee.toString(), "");
        return defaultFields;
    }

    private void initContainers() {
        try {
            /* TableQuery and SQLContainer for - tickets */
            dbHelper = new DatabaseHelper();

            TableQuery q1 = new TableQuery("tickets", dbHelper.getConnectionPool());
            q1.setVersionColumn("version");
            ticketsContainer = new SQLContainer(q1);
            ticketsContainer.setAutoCommit(false);
            ticketsContainer.addRowIdChangeListener(new QueryDelegate.RowIdChangeListener() {
                /**
						 * 
						 */
                private static final long serialVersionUID = 1L;

                @Override
                public void rowIdChange(RowIdChangeEvent event) {
                    LOG.trace("RowId change event fired!");
                    setOldRowId(event.getOldRowId());
                    LOG.trace("OldRowId: " + getOldRowId().toString());
                    setNewRowId(event.getOldRowId());
                    LOG.trace("NewRowId: " + getNewRowId().toString());
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Object getOldRowId() {
        return oldRowId;
    }

    public void setOldRowId(Object oldRowId) {
        this.oldRowId = oldRowId;
    }

    public Object getNewRowId() {
        return newRowId;
    }

    public void setNewRowId(Object newRowId) {
        this.newRowId = newRowId;
    }
}
