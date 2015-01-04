package org.shepherd.vaadin.dashboard.view;

import org.vaadin.spring.UIScope;
import org.vaadin.spring.navigator.VaadinView;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

/*
 * Dashboard MainView is a simple HorizontalLayout that wraps the menu on the
 * left and creates a simple container for the navigator on the right.
 */
@SuppressWarnings("serial")
@VaadinView(name="mainView")
@UIScope
public class MainView extends HorizontalLayout implements View{

    public static final String STYLE_NAME = "mainview";

	public MainView() {
        setSizeFull();
        addStyleName(STYLE_NAME);

		addComponent(new DashboardMenu());

		ComponentContainer content = new CssLayout();
		content.addStyleName("view-content");
		content.setSizeFull();
		addComponent(content);
		setExpandRatio(content, 1.0f);

		new Navigator(UI.getCurrent(), content);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
}