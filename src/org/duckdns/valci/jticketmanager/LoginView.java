package org.duckdns.valci.jticketmanager;

import org.duckdns.valci.jticketmanager.data.DatabaseHelper;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class LoginView extends VerticalLayout implements View {
    public LoginView() {    
        setSizeFull();
        setSpacing(true);
        Label labelWelcome = new Label("JTicket Manager web service");
        labelWelcome.addStyleName(ValoTheme.LABEL_H1);
        Label labelIntro = new Label(
                "Welcome. Please enter your information below to log in. (or leave blank for demo purposes)");
        TextField username = new TextField("Username");
        username.setId("usernameTextBoxLoginView");
        TextField password = new TextField("Password");
        password.setId("passwordTextBoxLoginView");
        addComponent(labelWelcome);
        addComponent(labelIntro);
        addComponent(username);
        addComponent(password);
        addComponent(loginButton());
        addComponent(fillDabaseButton());
    }

    private Button loginButton() {
        Button button = new Button("Log In", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                DatabaseHelper dh = DatabaseHelper.getInstance();
                getUI().getNavigator().navigateTo(JticketmanagerUI.JTICKETMANAGER);
            }
        });
        button.setId("loginButton");
        return button;
    }
    
    private Button fillDabaseButton() {
        Button button = new Button("Fill Database", new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                DatabaseHelper dh = DatabaseHelper.getInstance();
                dh.fillTestData();
            }
        });
        button.setId("fillDabaseButton");
        return button;
    }

    @Override
    public void enter(ViewChangeEvent event) {
        // TODO Auto-generated method stub
    }
}
