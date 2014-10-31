package org.duckdns.valci.jtricketmanager.data;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class TicketsSQLContainer {

    private DatabaseHelper dbHelper = null;
    private SQLContainer ticketsContainer = null;

    public static enum propertyIds {
        ID, ticketCategory, ticketStatus, ticketPriority, ticketSubject, ticketAssignee, ticketAuthor, ticketUpdate;
    }

    /*
     * CREATE TABLE tickets ( ID INTEGER, ticketCategory TEXT(2000000000),
     * ticketStatus TEXT(2000000000), ticketPriority TEXT(2000000000),
     * ticketSubject TEXT(2000000000), ticketAssignee TEXT(2000000000),
     * ticketAuthor TEXT(2000000000), ticketUpdate TEXT(2000000000), version
     * INTEGER NOT NULL, CONSTRAINT TICKETS_PK PRIMARY KEY (ID) );
     */
    public SQLContainer getContainer() {
        return ticketsContainer;
    }

    public TicketsSQLContainer() {
        initContainers();
    }

    private void initContainers() {
        try {
            /* TableQuery and SQLContainer for song - tickets */
            dbHelper = new DatabaseHelper();

            TableQuery q1 = new TableQuery("tickets",
                    dbHelper.getConnectionPool());
            q1.setVersionColumn("version");
            ticketsContainer = new SQLContainer(q1);
            // TODO: maybe should also add song_revisions
            ticketsContainer.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
