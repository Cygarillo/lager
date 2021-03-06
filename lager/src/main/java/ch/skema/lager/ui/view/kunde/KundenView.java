package ch.skema.lager.ui.view.kunde;

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

import ch.skema.lager.domain.Kunde;
import ch.skema.lager.event.LagerEvent;
import ch.skema.lager.event.LagerEventBus;
import ch.skema.lager.repository.KundeRepository;

@SpringView(name = KundenView.VIEW_NAME)
@UIScope
public class KundenView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	/*
	 * This view is registered automatically based on the @SpringView
	 * annotation. As it has an empty string as its view name, it will be shown
	 * when navigating to the Homepage
	 */
	public static final String VIEW_NAME = "customerView";

	@PostConstruct
	public void init() {
		this.grid = new Grid();
		this.filter = new TextField();
		this.addNewBtn = new Button("Neuer Kunde", FontAwesome.PLUS);
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
	private KundeRepository repo;
	@Autowired
	private KundeEditor editor;
	private Grid grid;
	private TextField filter;
	private Button addNewBtn;

	private void buildLayout() {
		HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);
		toolbar.setSpacing(true);
		grid.setColumns("name");
		grid.setSizeFull();
		HorizontalLayout main = new HorizontalLayout(grid, editor);
		main.setSpacing(true);
		main.setSizeFull();

		main.setExpandRatio(grid, 2);
		main.setExpandRatio(editor, 1);

		// build layout
		addComponent(toolbar);
		addComponent(main);
		setSpacing(true);
		setMargin(true);

		filter.setInputPrompt("Nach Name filtern:");

		// Replace listing with filtered content when user changes filter
		filter.addTextChangeListener(e -> listCustomers(e.getText()));

		// Connect selected Customer to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			} else {
				editor.editCustomer((Kunde) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editCustomer(new Kunde("")));

		// Initialize listing
		listCustomers(null);
	}

	private void listCustomers(String text) {
		if (StringUtils.isEmpty(text)) {
			BeanItemContainer<Kunde> container = new BeanItemContainer<>(Kunde.class, repo.findAll());
			grid.setContainerDataSource(container);
			if (shouldRestoreSelection()) {
				grid.select(container.getItemIds().stream().filter(c -> c.getId().equals(editor.getCustomer().getId())).findFirst().get());
			}

		} else {
			grid.setContainerDataSource(new BeanItemContainer<>(Kunde.class, repo.findByNameStartsWithIgnoreCase(text)));
		}
	}

	private boolean shouldRestoreSelection() {
		return editor.isVisible() && editor.getCustomer() != null;
	}

	@Subscribe
	public void processKundeEvent(final LagerEvent.KundeEvent event) {
		editor.setVisible(false);
		listCustomers(filter.getValue());
	}

}
