package org.duckdns.valci.jticketmanager;

import java.io.Serializable;

import org.duckdns.valci.jtricketmanager.data.TicketsSQLContainer;
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

    public TicketsController(TicketsView view,
            TicketsSQLContainer ticketsSQLContainerInstance) {
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
                // view.getTicketList().select(null);
                // view.getEditorFields().discard();
                model.addNewTicket(view.getTicketList(), view.getEditorFields());

                // view.clearSearchAndSongFields();
                break;
            case ("saveTicketButton"):
                LOG.trace("Save ticket button clicked");
                model.saveTicket(view.getEditorFields(), view.getTicketList());
                // model.addSong(view.getSongNameField().getValue(), view
                // .getSongTextInput().getValue(), view
                // .getSongAuthorField().getValue());
                break;
            case ("removeTicketButton"):
                LOG.trace("Remove ticket button clicked");
                model.removeTicket(view.getTicketList().getValue());
                LOG.trace("Selecting last ItemId in table");
                view.getTicketList().select(
                        model.getTicketsSQLContainer().getContainer()
                                .lastItemId());
                // LOG.trace("Discarding all changes in EditorFields");
                // view.getEditorFields().discard();
                // model.deleteSong(view.getSongListTable().getValue());
            }
        };
    }

    private final class SearchFieldTextChangeListener implements
            TextChangeListener {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void textChange(TextChangeEvent event) {
            LOG.trace("Removing all container filters");
            model.getTicketsSQLContainer().getContainer()
                    .removeAllContainerFilters();
            LOG.trace("Using search filter for tickets subjects: "
                    + event.getText());
            model.getTicketsSQLContainer()
                    .getContainer()
                    .addContainerFilter(
                            TicketsSQLContainer.getDbColumnsMap().get(
                                    "ticketSubject"), event.getText(), true,
                            false);
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
            }
            // this is to disable Update edits
            view.getEditorFields().setItemDataSource(
                    view.getTicketList().getItem(ticketId));
            view.getEditorFields()
                    .getField(
                            TicketsSQLContainer.propertyIds.ticketUpdate
                                    .toString()).setReadOnly(true);
            /*
             * view.getEditorFields() .getField(
             * TicketsSQLContainer.propertyIds.ticketUpdate
             * .toString()).setEnabled(false);
             */
            // view.getEditorLayout().setVisible(true);
            view.getEditorLayout().setVisible(ticketId != null);
        }
    }

    public SearchFieldTextChangeListener getSearchFieldTextChangeListener() {
        return this.searchFieldTextChangeListener;
    }

    public void setSearchFieldTextChangeListener(
            SearchFieldTextChangeListener searchFieldTextChangeListener) {
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

    public void setTableValueChangeListener(
            TableValueChangeListener tableValueChangeListener) {
        this.tableValueChangeListener = tableValueChangeListener;
    }

    public SQLContainer getTicketsSQLContainer() {
        return model.getTicketsSQLContainer().getContainer();
    }

}
