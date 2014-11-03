package org.duckdns.valci.jtricketmanager.data;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.LinkedHashMap;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class TicketsSQLContainer implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private DatabaseHelper dbHelper = null;
    private SQLContainer ticketsContainer = null;

    /*
     * public static enum propertyIds { ID, ticketCategory, ticketStatus,
     * ticketPriority, ticketSubject, ticketAssignee, ticketAuthor,
     * ticketUpdate; public static final String[] names = new
     * String[values().length]; static { propertyIds[] values = values(); for
     * (int i = 0; i < values.length; i++) names[i] = values[i].name(); } }
     */

    /*
     * CREATE TABLE tickets ( ID INTEGER PRIMARY KEY AUTOINCREMENT,
     * ticketCategory text, ticketStatus text, ticketPriority text,
     * ticketSubject text, ticketAssignee text, ticketAuthor text, ticketUpdate
     * text, version INTEGER DEFAULT 0 NOT NULL )
     */
    public SQLContainer getContainer() {
        return ticketsContainer;
    }

    public TicketsSQLContainer() {
        initContainers();
    }

    public static LinkedHashMap<String, String> getDbColumnsMap() {
        LinkedHashMap<String, String> propertyIds = new LinkedHashMap<String, String>();
        propertyIds.put("ID", "#");
        propertyIds.put("ticketCategory", "Category");
        propertyIds.put("ticketStatus", "Status");
        propertyIds.put("ticketPriority", "Priority");
        propertyIds.put("ticketSubject", "Subject");
        propertyIds.put("ticketAssignee", "Assignee");
        propertyIds.put("ticketUpdate", "Updated");
        return propertyIds;
    }

    private void initContainers() {
        try {
            /* TableQuery and SQLContainer for - tickets */
            dbHelper = new DatabaseHelper();

            TableQuery q1 = new TableQuery("tickets",
                    dbHelper.getConnectionPool());
            q1.setVersionColumn("version");
            ticketsContainer = new SQLContainer(q1);
            ticketsContainer.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
