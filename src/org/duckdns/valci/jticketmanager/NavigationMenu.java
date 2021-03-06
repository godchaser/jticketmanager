package org.duckdns.valci.jticketmanager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;

public class NavigationMenu extends CustomComponent {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    static final Logger LOG = LoggerFactory.getLogger(NavigationMenu.class);

    public NavigationMenu() {
        MenuBar navigationMenu = new MenuBar();
        navigationMenu.setId("NavigationMenu");
        navigationMenu.setWidth("100%");
        navigationMenu.addItem("Logout", menuLogoutCommand);
        setSizeUndefined();
        setCompositionRoot(navigationMenu);
    }

    private final Command menuLogoutCommand = new Command() {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;

        @Override
        public void menuSelected(final MenuItem selectedItem) {
            LOG.trace("Logout button clicked - closing session");
            getUI().getSession().close();
            LOG.trace("Redirecting to logout view");
            getUI().getPage().setLocation(getLogoutPath());
        }
    };

    private String getLogoutPath() {
        return getUI().getPage().getLocation().getPath();
    }

};
