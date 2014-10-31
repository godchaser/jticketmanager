package org.duckdns.valci.jticketmanager;

import org.duckdns.valci.jtricketmanager.data.TicketsSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class TicketsView extends VerticalLayout implements View {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(TicketsView.class);

    TicketsController controller;

    private Table ticketList = new Table();
    private TextField ticketSearchField = new TextField();
    private Button addNewTicketButton = new Button("New ticket");
    private Button removeTicketButton = new Button("Remove this ticket");
    private FormLayout editorLayout = new FormLayout();

    private FieldGroup editorFields = new FieldGroup();

    // table column header
    private static final String TICKETID = "#";
    private static final String TICKETCATEGORY = "Category";
    private static final String TICKETSTATUS = "Status";
    private static final String TICKETPRIORITY = "Priority";
    private static final String TICKETSUBJECT = "Subject";
    private static final String TICKETASSIGNEE = "Assignee";
    private static final String TICKETUPDATE = "Updated";

    private static final String[] fieldNames = new String[] { TICKETID,
            TICKETCATEGORY, TICKETSTATUS, TICKETPRIORITY, TICKETSUBJECT,
            TICKETASSIGNEE, TICKETUPDATE };

    public TicketsView(TicketsSQLContainer ticketsSQLContainerInstance) {
        this.controller = new TicketsController(this,
                ticketsSQLContainerInstance);
        initLayout();
        initTicketList();
        initEditor();
        initSearch();
        initAddRemoveButtons();
    }

    private void initAddRemoveButtons() {
        // TODO Auto-generated method stub
        addNewTicketButton.setId("addNewTicketButton");
        addNewTicketButton
                .addClickListener(controller.getButtonClickListener());
        removeTicketButton.setId("removeTicketButton");
        removeTicketButton
                .addClickListener(controller.getButtonClickListener());

        /*
         * addNewContactButton.addClickListener(new ClickListener() { public
         * void buttonClick(ClickEvent event) {
         * 
         * contactContainer.removeAllContainerFilters(); Object contactId =
         * contactContainer.addItemAt(0);
         * 
         * contactList.getContainerProperty(contactId, FNAME).setValue( "New");
         * contactList.getContainerProperty(contactId, LNAME).setValue(
         * "Contact");
         * 
         * contactList.select(contactId); } });
         * removeContactButton.addClickListener(new ClickListener() { public
         * void buttonClick(ClickEvent event) { Object contactId =
         * contactList.getValue(); contactList.removeItem(contactId); } });
         */
    }

    private void initSearch() {

        ticketSearchField.setInputPrompt("Search tickets");
        ticketSearchField.setTextChangeEventMode(TextChangeEventMode.LAZY);
        ticketSearchField.addTextChangeListener(controller
                .getSearchFieldTextChangeListener());

    }

    private void initTicketList() {
        // here we have to add data source
        ticketList.setContainerDataSource(controller.getTicketsSQLContainer());
        ticketList.setVisibleColumns(new String[] {
                TicketsSQLContainer.propertyIds.ID.toString(),
                TicketsSQLContainer.propertyIds.ticketCategory.toString(),
                TicketsSQLContainer.propertyIds.ticketStatus.toString(),
                TicketsSQLContainer.propertyIds.ticketPriority.toString(),
                TicketsSQLContainer.propertyIds.ticketSubject.toString() });
        ticketList.setSelectable(true);
        ticketList.setImmediate(true);
        ticketList.addValueChangeListener(controller
                .getTableValueChangeListener());

    }

    private void initEditor() {
        editorLayout.addComponent(removeTicketButton);
        for (String fieldName : fieldNames) {
            TextField field = new TextField(fieldName);
            field.setId(fieldName);
            editorLayout.addComponent(field);
            field.setWidth("100%");
            editorFields.bind(field, fieldName);
        }
        editorFields.setBuffered(true);
    }

    private void initLayout() {
        setSizeFull();
        setSpacing(true);
        setMargin(true);
        /* Root of the user interface component tree is set */
        HorizontalSplitPanel splitPanel = new HorizontalSplitPanel();
        addComponent(splitPanel);
        /* Build the component tree */
        VerticalLayout leftLayout = new VerticalLayout();
        splitPanel.addComponent(leftLayout);
        splitPanel.addComponent(editorLayout);
        leftLayout.addComponent(ticketList);
        HorizontalLayout bottomLeftLayout = new HorizontalLayout();
        leftLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(ticketSearchField);
        bottomLeftLayout.addComponent(addNewTicketButton);
        /* Set the contents in the left of the split panel to use all the space */
        leftLayout.setSizeFull();
        /*
         * On the left side, expand the size of the contactList so that it uses
         * all the space left after from bottomLeftLayout
         */
        leftLayout.setExpandRatio(ticketList, 1);
        ticketList.setSizeFull();
        /*
         * In the bottomLeftLayout, searchField takes all the width there is
         * after adding addNewContactButton. The height of the layout is defined
         * by the tallest component.
         */
        bottomLeftLayout.setWidth("100%");
        ticketSearchField.setWidth("100%");
        bottomLeftLayout.setExpandRatio(ticketSearchField, 1);
        /* Put a little margin around the fields in the right side editor */
        editorLayout.setMargin(true);
        editorLayout.setVisible(false);
    }

    public Table getTicketList() {
        return ticketList;
    }

    public FieldGroup getEditorFields() {
        return editorFields;
    }

    public FormLayout getEditorLayout() {
        return editorLayout;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }
}
