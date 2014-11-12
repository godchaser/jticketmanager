package org.duckdns.valci.jticketmanager;

import org.duckdns.valci.jticketmanager.data.TicketsSQLContainer;
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
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class TicketsView extends VerticalLayout implements View {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(TicketsView.class);

    private TicketsController controller;
    private HorizontalLayout rootPanel = new HorizontalLayout();
    private VerticalLayout leftTicketListLayout = new VerticalLayout();
    private HorizontalLayout bottomLeftLayout = new HorizontalLayout();

    private Table ticketList = new Table("Ticket List");
    private TextField ticketSearchField = new TextField();
    private Button addNewTicketButton = new Button("New ticket");

    private Button removeTicketButton = new Button("Remove");
    private Button saveTicketButton = new Button("Save");;

    private FormLayout rightTicketEditorLayout = new FormLayout();
    private TextField ticketIDField;
    private ComboBox selectCategory;
    private ComboBox selectStatus;
    private ComboBox selectPriority;
    private TextArea ticketSubjectField;
    private TextField ticketAsigneeField;
    private TextField ticketUpdatedField;
    private FieldGroup editorFields;

    public TicketsView() {
        this.controller = new TicketsController(this);
        initLayout();
        initTicketList();
        initEditor();
        initSearch();
        initAddRemoveSaveButtons();
    }

    private void initLayout() {
        setSizeFull();
        // setSpacing(true);
        setMargin(true);

        // HEADER
        HorizontalLayout headerLayout = new HorizontalLayout();
        Label labelWelcome = new Label("JTicket Manager");
        labelWelcome.addStyleName(ValoTheme.LABEL_H2);

        NavigationMenu navigationMenu = new NavigationMenu();

        headerLayout.setSizeFull();
        headerLayout.setWidth("100%");
        headerLayout.setSpacing(true);
        headerLayout.setMargin(true);
        headerLayout.addComponent(labelWelcome);
        headerLayout.addComponent(navigationMenu);

        // TODO: this has to be fixed
        headerLayout.setExpandRatio(labelWelcome, 9);
        headerLayout.setExpandRatio(navigationMenu, 1);

        addComponent(headerLayout);
        addComponent(rootPanel);

        // ROOT PANEL
        rootPanel.setSizeFull();
        rootPanel.setSpacing(true);
        rootPanel.setMargin(true);
        rootPanel.addComponent(leftTicketListLayout);
        rootPanel.addComponent(rightTicketEditorLayout);
        rootPanel.setExpandRatio(leftTicketListLayout, 4);
        rootPanel.setExpandRatio(rightTicketEditorLayout, 1);

        // LEFT PANEL
        leftTicketListLayout.addComponent(ticketList);
        leftTicketListLayout.addComponent(bottomLeftLayout);
        bottomLeftLayout.addComponent(ticketSearchField);
        bottomLeftLayout.addComponent(addNewTicketButton);
        // leftTicketListLayout.setSizeFull();
        // leftTicketListLayout.setExpandRatio(ticketList, 1);
        bottomLeftLayout.setWidth("100%");
        ticketSearchField.setWidth("100%");
        bottomLeftLayout.setExpandRatio(ticketSearchField, 1);

        // RIGHT PANEL
        rightTicketEditorLayout.setMargin(true);
        rightTicketEditorLayout.setSpacing(true);
        rightTicketEditorLayout.setVisible(false);
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
        ticketList.setSelectable(true);
        ticketList.setImmediate(true);
        ticketList.setNullSelectionAllowed(false);
        ticketList.setSizeFull();
        ticketList.setWidth("100%");
        ticketList.addValueChangeListener(controller.getTableValueChangeListener());
    }

    private void initEditor() {
        rightTicketEditorLayout.addComponent(removeTicketButton);
        rightTicketEditorLayout.addComponent(saveTicketButton);

        // EDITOR FIELDS
        // ID Category Status Priority Subject Assignee Updated

        // ID FIELD
        ticketIDField = new TextField(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.ID.toString()));
        ticketIDField.setId(TicketsSQLContainer.propertyIds.ID.toString());
        rightTicketEditorLayout.addComponent(ticketIDField);
        ticketIDField.setWidth("100%");
        editorFields = new FieldGroup();
        editorFields.bind(ticketIDField, TicketsSQLContainer.propertyIds.ID.toString());

        // COMBOBOXES
        selectCategory = new ComboBox(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.TICKETCATEGORY.toString()));
        selectCategory.setId(TicketsSQLContainer.propertyIds.TICKETCATEGORY.toString());
        selectCategory.addItem(TicketsSQLContainer.ticketCategories.FEATURE.toString());
        selectCategory.addItem(TicketsSQLContainer.ticketCategories.BUG.toString());
        selectCategory.setTextInputAllowed(false);
        selectCategory.setNewItemsAllowed(false);
        rightTicketEditorLayout.addComponent(selectCategory);
        selectCategory.setWidth("100%");

        selectStatus = new ComboBox(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.TICKETSTATUS.toString()));
        selectStatus.setId(TicketsSQLContainer.propertyIds.TICKETSTATUS.toString());
        selectStatus.addItem(TicketsSQLContainer.ticketStatus.OPEN.toString());
        selectStatus.addItem(TicketsSQLContainer.ticketStatus.ONGOING.toString());
        selectStatus.addItem(TicketsSQLContainer.ticketStatus.FEEDBACK.toString());
        selectStatus.addItem(TicketsSQLContainer.ticketStatus.CLOSED.toString());
        selectStatus.setTextInputAllowed(false);
        selectStatus.setNewItemsAllowed(false);
        rightTicketEditorLayout.addComponent(selectStatus);
        selectStatus.setWidth("100%");

        selectPriority = new ComboBox(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.TICKETPRIORITY.toString()));
        selectPriority.setId(TicketsSQLContainer.propertyIds.TICKETPRIORITY.toString());
        selectPriority.addItem(TicketsSQLContainer.ticketPriority.LOW.toString());
        selectPriority.addItem(TicketsSQLContainer.ticketPriority.NORMAL.toString());
        selectPriority.addItem(TicketsSQLContainer.ticketPriority.HIGH.toString());
        selectPriority.setTextInputAllowed(false);
        selectPriority.setNewItemsAllowed(false);
        rightTicketEditorLayout.addComponent(selectPriority);
        selectPriority.setWidth("100%");

        // SUBJECT TEXTAREA
        ticketSubjectField = new TextArea(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.TICKETSUBJECT.toString()));
        ticketSubjectField.setId(TicketsSQLContainer.propertyIds.TICKETSUBJECT.toString());
        rightTicketEditorLayout.addComponent(ticketSubjectField);
        ticketSubjectField.setWidth("100%");
        ticketSubjectField.setRows(4);
        editorFields.bind(ticketSubjectField, TicketsSQLContainer.propertyIds.TICKETSUBJECT.toString());

        // OTHER FIELDS
        ticketAsigneeField = new TextField(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.TICKETASSIGNEE.toString()));
        ticketAsigneeField.setId(TicketsSQLContainer.propertyIds.TICKETASSIGNEE.toString());
        rightTicketEditorLayout.addComponent(ticketAsigneeField);
        ticketAsigneeField.setWidth("100%");
        editorFields.bind(ticketAsigneeField, TicketsSQLContainer.propertyIds.TICKETASSIGNEE.toString());

        ticketUpdatedField = new TextField(TicketsSQLContainer.getDbColumnsMap().get(
                TicketsSQLContainer.propertyIds.TICKETUPDATE.toString()));
        ticketUpdatedField.setId(TicketsSQLContainer.propertyIds.TICKETUPDATE.toString());
        rightTicketEditorLayout.addComponent(ticketUpdatedField);
        ticketUpdatedField.setWidth("100%");
        editorFields.bind(ticketUpdatedField, TicketsSQLContainer.propertyIds.TICKETUPDATE.toString());

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
        return rightTicketEditorLayout;
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
        LOG.trace("Enter View event fired");
    }

}
