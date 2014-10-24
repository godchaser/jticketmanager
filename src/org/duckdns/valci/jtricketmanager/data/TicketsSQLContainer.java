package org.duckdns.valci.jtricketmanager.data;

import java.sql.SQLException;


import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

public class TicketsSQLContainer {

    private DatabaseHelper dbHelper = null;
    private SQLContainer ticketsContainer = null;

    public SQLContainer getSongContainer() {
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
