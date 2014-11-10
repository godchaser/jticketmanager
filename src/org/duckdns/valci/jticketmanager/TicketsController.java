package org.duckdns.valci.jticketmanager;

import java.io.Serializable;

import org.duckdns.valci.jticketmanager.data.TicketsSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

public class TicketsController implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    static final Logger LOG = LoggerFactory.getLogger(TicketsController.class);

    private TicketsModel model;
    private TicketsView view;

    private SearchFieldTextChangeListener searchFieldTextChangeListener;
    private ButtonClickListener buttonClickListener;
    private TableValueChangeListener tableValueChangeListener;

    public TicketsController(TicketsView view, TicketsSQLContainer ticketsSQLContainerInstance) {
        this.model = new TicketsModel(ticketsSQLContainerInstance);
        this.view = view;
        this.setSearchFieldTextChangeListener(new SearchFieldTextChangeListener());
        this.setButtonClickListener(new ButtonClickListener());
        this.setTableValueChangeListener(new TableValueChangeListener());
    }

    // this is listener for all button features
    private final class ButtonClickListener implements Button.ClickListener {

        private static final long serialVersionUID = 1L;

        public void buttonClick(ClickEvent event) {
            switch (event.getButton().getId()) {
            case ("addNewTicketButton"):
                LOG.trace("New ticket button clicked");
                model.addNewTicket(view.getTicketList(), view.getEditorFields());
                LOG.trace("Setting focus on subject field");
                view.getEditorFields().getField(TicketsSQLContainer.propertyIds.TICKETSUBJECT.toString()).focus();
                LOG.trace("Clearing subject field");
                view.getTicketSubjectField().setValue("");
                break;
            case ("saveTicketButton"):
                LOG.trace("Save ticket button clicked");
                model.saveTicket(view.getEditorFields(), view.getTicketList(), view.getSelectCategory(),
                        view.getSelectPriority(), view.getSelectStatus());
                break;
            case ("removeTicketButton"):
                LOG.trace("Remove ticket button clicked");
                model.removeTicket(view.getTicketList().getValue());
                LOG.trace("Selecting last ItemId in table");
                view.getTicketList().select(model.getTicketsSQLContainer().getContainer().lastItemId());
                break;
            }
        };
    }

    private final class SearchFieldTextChangeListener implements TextChangeListener {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void textChange(TextChangeEvent event) {
            LOG.trace("Removing all container filters");
            model.getTicketsSQLContainer().getContainer().removeAllContainerFilters();
            LOG.trace("Using search filter for tickets subjects: " + event.getText());
            model.getTicketsSQLContainer()
                    .getContainer()
                    .addContainerFilter(TicketsSQLContainer.propertyIds.TICKETSUBJECT.toString(), event.getText(),
                            true, false);
        }
    }

    private final class TableValueChangeListener implements ValueChangeListener {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void valueChange(ValueChangeEvent event) {
            // TODO Auto-generated method stub
            Object ticketId = view.getTicketList().getValue();
            if (ticketId != null) {
                LOG.trace("Selected ticketId: " + ticketId.toString());

                // UPDATE EDITOR FIELDS
                view.getEditorFields().setItemDataSource(view.getTicketList().getItem(ticketId));
                // this is for disabling UPDATE timestamp field
                view.getEditorFields().getField(TicketsSQLContainer.propertyIds.TICKETUPDATE.toString())
                        .setReadOnly(true);

                // UPDATE COMBOBOXES
                Object ticketCategory = view.getTicketList().getItem(ticketId)
                        .getItemProperty(TicketsSQLContainer.propertyIds.TICKETCATEGORY.toString()).getValue();
                LOG.trace("Selecting ticketCategory: " + ticketCategory);
                view.getSelectCategory().select(ticketCategory);

                Object ticketStatus = view.getTicketList().getItem(ticketId)
                        .getItemProperty(TicketsSQLContainer.propertyIds.TICKETSTATUS.toString()).getValue();
                LOG.trace("Selecting ticketStatus: " + ticketStatus);
                view.getSelectStatus().select(ticketStatus);

                Object ticketPriority = view.getTicketList().getItem(ticketId)
                        .getItemProperty(TicketsSQLContainer.propertyIds.TICKETPRIORITY.toString()).getValue();
                LOG.trace("Selecting ticketPriority: " + ticketPriority);
                view.getSelectPriority().select(ticketPriority);
            } else {
                LOG.trace("TicketId is null");
            }
            view.getEditorLayout().setVisible(ticketId != null);
        }
    }

    public SearchFieldTextChangeListener getSearchFieldTextChangeListener() {
        return this.searchFieldTextChangeListener;
    }

    public void setSearchFieldTextChangeListener(SearchFieldTextChangeListener searchFieldTextChangeListener) {
        this.searchFieldTextChangeListener = searchFieldTextChangeListener;
    }

    public ButtonClickListener getButtonClickListener() {
        return this.buttonClickListener;
    }

    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }

    public TableValueChangeListener getTableValueChangeListener() {
        return tableValueChangeListener;
    }

    public void setTableValueChangeListener(TableValueChangeListener tableValueChangeListener) {
        this.tableValueChangeListener = tableValueChangeListener;
    }

    public SQLContainer getTicketsSQLContainer() {
        return model.getTicketsSQLContainer().getContainer();
    }

}
