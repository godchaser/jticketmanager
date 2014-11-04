package org.duckdns.valci.jtricketmanager.data;

import java.io.Serializable;
import java.sql.SQLException;
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

	static final Logger LOG = LoggerFactory
			.getLogger(TicketsSQLContainer.class);

	private DatabaseHelper dbHelper = null;
	private SQLContainer ticketsContainer = null;
	private Object oldRowId = null;
	private Object newRowId = null;

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
			ticketsContainer
					.addRowIdChangeListener(new QueryDelegate.RowIdChangeListener() {
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
