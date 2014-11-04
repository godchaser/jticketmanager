package org.duckdns.valci.jticketmanager;

import javax.servlet.annotation.WebServlet;

import org.duckdns.valci.jtricketmanager.data.TicketsSQLContainer;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.Navigator.ComponentContainerViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("valo-theme")
public class JticketmanagerUI extends UI {
	public Navigator navigator;
	VerticalLayout layout;

	TicketsSQLContainer ticketsSQLContainerInstance;

	public static final String JTICKETMANAGER = "tickets";
	public static final String ADDRESSBOOKEXAMPLE = "example";

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = JticketmanagerUI.class)
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {

		ticketsSQLContainerInstance = new TicketsSQLContainer();
		layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setSpacing(true);
		setContent(layout);
		ComponentContainerViewDisplay viewDisplay = new ComponentContainerViewDisplay(
				layout);
		navigator = new Navigator(UI.getCurrent(), viewDisplay);
		navigator.addView("", new TicketsView(ticketsSQLContainerInstance));
		navigator.addView(ADDRESSBOOKEXAMPLE, new AddressBookExample());
	}

}