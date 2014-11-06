package org.duckdns.valci.jticketmanager;

import org.duckdns.valci.jtricketmanager.data.TicketsSQLContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class TicketsView extends VerticalLayout implements View {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(TicketsView.class);

    private TicketsController controller;

    private Table ticketList = new Table("Ticket List");
    private TextField ticketSearchField = new TextField();

    private Button addNewTicketButton = new Button("New ticket");
    private Button removeTicketButton = new Button("Remove");

    private Button saveTicketButton = new Button("Save");;

    private FormLayout editorLayout = new FormLayout();

    private TextField ticketIDField;

    private ComboBox selectCategory;
    private ComboBox selectStatus;
    private ComboBox selectPriority;

    private TextArea ticketSubjectField;
    private TextField ticketAsigneeField;
    private TextField ticketUpdatedField;

    private FieldGroup editorFields;

    public TicketsView(TicketsSQLContainer ticketsSQLContainerInstance) {
        this.controller = new TicketsController(this, ticketsSQLContainerInstance);
        initLayout();
        initTicketList();
        initEditor();
        initSearch();
        initAddRemoveSaveButtons();
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
         * On the left side, expand the size of the contactList so that it uses all the space left after from
         * bottomLeftLayout
         */
        leftLayout.setExpandRatio(ticketList, 1);
        ticketList.setSizeFull();
        /*
         * In the bottomLeftLayout, searchField takes all the width there is after adding addNewContactButton. The
         * height of the layout is defined by the tallest component.
         */
        bottomLeftLayout.setWidth("100%");
        ticketSearchField.setWidth("100%");
        bottomLeftLayout.setExpandRatio(ticketSearchField, 1);
        /* Put a little margin around the fields in the right side editor */
        editorLayout.setMargin(true);
        editorLayout.setSpacing(true);
        editorLayout.setVisible(false);
    }

    private void initTicketList() {
        ticketList.setContainerDataSource(controller.getTicketsSQLContainer());
        for (String key : TicketsSQLContainer.getDbColumnsMap().keySet()) {
            ticketList.setColumnHeader(key, TicketsSQLContainer.getDbColumnsMap().get(key));
        }
        ticketList.setVisibleColumns(new Object[] { ticketList.getVisibleColumns()[0],
                ticketList.getVisibleColumns()[1], ticketList.getVisibleColumns()[2],
                ticketList.getVisibleColumns()[3], ticketList.getVisibleColumns()[4],
                ticketList.getVisibleColumns()[5], ticketList.getVisibleColumns()[6] });
        ticketList.setColumnExpandRatio(TicketsSQLContainer.propertyIds.ticketSubject.toString(), 2);
        ticketList.setSelectable(true);
        ticketList.setImmediate(true);
        ticketList.setNullSelectionAllowed(false);
        ticketList.addValueChangeListener(controller.getTableValueChangeListener());
    }

    private void initEditor() {
        editorLayout.addComponent(removeTicketButton);
        editorLayout.addComponent(saveTicketButton);

        // EDITOR FIELDS
        // ID Category Status Priority Subject Assignee Updated

        // ID FIELD
        ticketIDField = new TextField(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.ID.toString()));
        ticketIDField.setId(TicketsSQLContainer.propertyIds.ID.toString());
        editorLayout.addComponent(ticketIDField);
        ticketIDField.setWidth("100%");
        editorFields = new FieldGroup();
        editorFields.bind(ticketIDField, TicketsSQLContainer.propertyIds.ID.toString());

        // COMBOBOXES
        selectCategory = new ComboBox(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.ticketCategory.toString()));
        selectCategory.setId(TicketsSQLContainer.propertyIds.ticketCategory.toString());
        selectCategory.addItem(TicketsSQLContainer.ticketCategories.FEATURE.toString());
        selectCategory.addItem(TicketsSQLContainer.ticketCategories.BUG.toString());
        selectCategory.setTextInputAllowed(false);
        selectCategory.setNewItemsAllowed(false);
        editorLayout.addComponent(selectCategory);
        selectCategory.setWidth("100%");

        selectStatus = new ComboBox(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.ticketStatus.toString()));
        selectStatus.setId(TicketsSQLContainer.propertyIds.ticketStatus.toString());
        selectStatus.addItem(TicketsSQLContainer.ticketStatus.OPEN.toString());
        selectStatus.addItem(TicketsSQLContainer.ticketStatus.ONGOING.toString());
        selectStatus.addItem(TicketsSQLContainer.ticketStatus.FEEDBACK.toString());
        selectStatus.addItem(TicketsSQLContainer.ticketStatus.CLOSED.toString());
        selectStatus.setTextInputAllowed(false);
        selectStatus.setNewItemsAllowed(false);
        editorLayout.addComponent(selectStatus);
        selectStatus.setWidth("100%");

        selectPriority = new ComboBox(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.ticketPriority.toString()));
        selectPriority.setId(TicketsSQLContainer.propertyIds.ticketPriority.toString());
        selectPriority.addItem(TicketsSQLContainer.ticketPriority.LOW.toString());
        selectPriority.addItem(TicketsSQLContainer.ticketPriority.NORMAL.toString());
        selectPriority.addItem(TicketsSQLContainer.ticketPriority.HIGH.toString());
        selectPriority.setTextInputAllowed(false);
        selectPriority.setNewItemsAllowed(false);
        editorLayout.addComponent(selectPriority);
        selectPriority.setWidth("100%");

        // SUBJECT TEXTAREA
        ticketSubjectField = new TextArea(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.ticketSubject.toString()));
        ticketSubjectField.setId(TicketsSQLContainer.propertyIds.ticketSubject.toString());
        editorLayout.addComponent(ticketSubjectField);
        ticketSubjectField.setWidth("100%");
        ticketSubjectField.setRows(4);
        editorFields.bind(ticketSubjectField, TicketsSQLContainer.propertyIds.ticketSubject.toString());

        // OTHER FIELDS
        ticketAsigneeField = new TextField(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.ticketAssignee.toString()));
        ticketAsigneeField.setId(TicketsSQLContainer.propertyIds.ticketAssignee.toString());
        editorLayout.addComponent(ticketAsigneeField);
        ticketAsigneeField.setWidth("100%");
        editorFields.bind(ticketAsigneeField, TicketsSQLContainer.propertyIds.ticketAssignee.toString());

        ticketUpdatedField = new TextField(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.ticketUpdate.toString()));
        ticketUpdatedField.setId(TicketsSQLContainer.propertyIds.ticketUpdate.toString());
        editorLayout.addComponent(ticketUpdatedField);
        ticketUpdatedField.setWidth("100%");
        editorFields.bind(ticketUpdatedField, TicketsSQLContainer.propertyIds.ticketUpdate.toString());

        editorFields.setBuffered(true);
    }

    private void initAddRemoveSaveButtons() {
        addNewTicketButton.setId("addNewTicketButton");
        addNewTicketButton.addClickListener(controller.getButtonClickListener());
        removeTicketButton.setId("removeTicketButton");
        removeTicketButton.addClickListener(controller.getButtonClickListener());
        saveTicketButton.setId("saveTicketButton");
        saveTicketButton.addClickListener(controller.getButtonClickListener());
    }

    private void initSearch() {
        ticketSearchField.setInputPrompt("Search tickets");
        ticketSearchField.setTextChangeEventMode(TextChangeEventMode.LAZY);
        ticketSearchField.addTextChangeListener(controller.getSearchFieldTextChangeListener());
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

    public ComboBox getSelectStatus() {
        return selectStatus;
    }

    public ComboBox getSelectPriority() {
        return selectPriority;
    }

    public ComboBox getSelectCategory() {
        return selectCategory;
    }

    public TextArea getTicketSubjectField() {
        return ticketSubjectField;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub

    }
}
