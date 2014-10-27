package org.duckdns.valci.jticketmanager;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.Navigator.ComponentContainerViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import org.duckdns.valci.jticketmanager.AddressBookExample;

@SuppressWarnings("serial")
@Theme("valo-theme")
public class JticketmanagerUI extends UI {
    public Navigator navigator;
    VerticalLayout layout;

    @WebServlet(value = "/*", asyncSupported = true)
    @VaadinServletConfiguration(productionMode = false, ui = JticketmanagerUI.class)
    public static class Servlet extends VaadinServlet {
    }

    @Override
    protected void init(VaadinRequest request) {
        layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
        ComponentContainerViewDisplay viewDisplay = new ComponentContainerViewDisplay(
                layout);
        navigator = new Navigator(UI.getCurrent(), viewDisplay);
        navigator.addView("", new AddressBookExample());
    }

}