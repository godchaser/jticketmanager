package org.duckdns.valci.jticketmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class TicketsView extends VerticalLayout implements View{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(TicketsView.class);
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
    
    public TicketsView(){
        initLayout();
        initTicketList();
        //initEditor();
        //initSearch();
        //initAddRemoveButtons();
    }

    private void initTicketList() {
        // TODO Auto-generated method stub
        
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

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
        
    }
}
