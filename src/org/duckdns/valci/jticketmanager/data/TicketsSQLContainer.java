package org.duckdns.valci.jticketmanager.data;

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

    public static final String TABLE = "tickets";

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
        ID, TICKETCATEGORY, TICKETSTATUS, TICKETPRIORITY, TICKETSUBJECT, TICKETASSIGNEE, TICKETUPDATE, TICKETAUTHOR, VERSION;
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
        propertyIdsMap.put(propertyIds.TICKETCATEGORY.toString(), "Category");
        propertyIdsMap.put(propertyIds.TICKETSTATUS.toString(), "Status");
        propertyIdsMap.put(propertyIds.TICKETPRIORITY.toString(), "Priority");
        propertyIdsMap.put(propertyIds.TICKETSUBJECT.toString(), "Subject");
        propertyIdsMap.put(propertyIds.TICKETASSIGNEE.toString(), "Assignee");
        propertyIdsMap.put(propertyIds.TICKETUPDATE.toString(), "Updated");
        return propertyIdsMap;
    }

    public static HashMap<String, String> getDefaultFields() {
        HashMap<String, String> defaultFields = new HashMap<String, String>();
        defaultFields.put(propertyIds.TICKETCATEGORY.toString(), ticketCategories.FEATURE.toString());
        defaultFields.put(propertyIds.TICKETSTATUS.toString(), ticketStatus.OPEN.toString());
        defaultFields.put(propertyIds.TICKETPRIORITY.toString(), ticketPriority.NORMAL.toString());
        defaultFields.put(propertyIds.TICKETSUBJECT.toString(), "");
        defaultFields.put(propertyIds.TICKETASSIGNEE.toString(), "");
        return defaultFields;
    }

    private void initContainers() {
        try {
            /* TableQuery and SQLContainer for - tickets */
            dbHelper = new DatabaseHelper();

            TableQuery q1 = new TableQuery(TABLE, dbHelper.getConnectionPool());
            q1.setVersionColumn(propertyIds.VERSION.toString());
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

    public DatabaseHelper getDbHelper() {
        return dbHelper;
    }
}
