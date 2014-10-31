package org.duckdns.valci.jticketmanager;

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
import com.vaadin.ui.Component;

public class TicketsController {
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
            case ("newSongButton"):
                LOG.trace("New song button clicked");
                LOG.trace("Clearing input fields");
                // view.clearSearchAndSongFields();
                break;
            case ("saveSongButton"):
                LOG.trace("Save button clicked");
                // model.addSong(view.getSongNameField().getValue(), view
                // .getSongTextInput().getValue(), view
                // .getSongAuthorField().getValue());
                break;
            case ("deleteSongButton"):
                LOG.trace("Delete button clicked");
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
            model.getTicketsSQLContainer().removeAllContainerFilters();
            LOG.trace("Using search filter for tickets subjects: "
                    + event.getText());
            model.getTicketsSQLContainer().addContainerFilter(
                    TicketsSQLContainer.propertyIds.ticketSubject.toString(),
                    event.getText(), true, false);
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
            Object contactId = view.getTicketList().getValue();

            if (contactId != null)
                view.getEditorFields().setItemDataSource(
                        view.getTicketList().getItem(contactId));
            ((Component) view.getEditorFields()).setVisible(contactId != null);
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
        return model.getTicketsSQLContainer();
    }

}
