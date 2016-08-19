package ch.skema.lager.ui.view.kategorie;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ch.skema.lager.domain.Kategorie;
import ch.skema.lager.event.LagerEvent.KategorieEvent;
import ch.skema.lager.event.LagerEventBus;
import ch.skema.lager.repository.KategorieRepository;

@SpringView(name = KategorieView.VIEW_NAME)
@UIScope
public class KategorieView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	/*
	 * This view is registered automatically based on the @SpringView
	 * annotation. As it has an empty string as its view name, it will be shown
	 * when navigating to the Homepage
	 */
	public static final String VIEW_NAME = "KategorieView";

	@PostConstruct
	void init() {
		this.grid = new Grid();
		this.filter = new TextField();
		this.addNewBtn = new Button("Neue Kategorie", FontAwesome.PLUS);
		buildLayout();
		LagerEventBus.register(this);
	}

	@Override
	public void detach() {
		super.detach();
		LagerEventBus.unregister(this);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// the view is constructed in the init() method()
	}

	@Autowired
	private KategorieRepository repo;
	@Autowired
	private KategorieEditor editor;
	private Grid grid;
	private TextField filter;
	private Button addNewBtn;

	private void buildLayout() {
		HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);
		toolbar.setSpacing(true);

		HorizontalLayout main = new HorizontalLayout(grid, editor);
		main.setSpacing(true);
		main.setSizeFull();

		main.setExpandRatio(grid, 2);
		main.setExpandRatio(editor, 1);

		// build layout
		addComponent(toolbar);
		addComponent(main);

		// Configure layouts and components
		setMargin(true);
		setSpacing(true);
		grid.setSizeFull();
		grid.setColumns("name");

		filter.setInputPrompt("Nach Name filtern:");

		// Hook logic to components

		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listEntity(e.getText()));

		// Connect selected Customer to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			} else {
				editor.edit((Kategorie) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.edit(new Kategorie("")));

		// Initialize listing
		listEntity(null);
	}

	private void listEntity(String text) {
		if (StringUtils.isEmpty(text)) {
			grid.setContainerDataSource(new BeanItemContainer<>(Kategorie.class, repo.findAll()));
		} else {
			grid.setContainerDataSource(new BeanItemContainer<>(Kategorie.class, repo.findByNameStartsWithIgnoreCase(text)));
		}
	}

	@Subscribe
	public void processKategorieEvent(final KategorieEvent event) {
		editor.setVisible(false);
		listEntity(filter.getValue());
	}

}
