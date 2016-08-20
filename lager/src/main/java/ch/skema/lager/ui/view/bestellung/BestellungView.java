package ch.skema.lager.ui.view.bestellung;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

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

import ch.skema.lager.domain.Bestellung;
import ch.skema.lager.event.LagerEvent.BestellungEvent;
import ch.skema.lager.event.LagerEventBus;
import ch.skema.lager.repository.BestellungRepository;

@SpringView(name = BestellungView.VIEW_NAME)
@UIScope
public class BestellungView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	/*
	 * This view is registered automatically based on the @SpringView
	 * annotation. As it has an empty string as its view name, it will be shown
	 * when navigating to the Homepage
	 */
	public static final String VIEW_NAME = "";
	@Autowired
	private BestellungRepository repo;
	@Autowired
	private BestellungEditor editor;
	private TextField filter;
	private Button addNewBtn;

	private Grid grid;

	@PostConstruct
	void init() {
		this.grid = new Grid();
		this.addNewBtn = new Button("Neue Bestellung", FontAwesome.PLUS);
		this.filter = new TextField();
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

	private void buildLayout() {
		HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);
		toolbar.setSpacing(true);
		grid.setColumns("kunde.name", "erledigt");
		grid.getColumn("kunde.name").setHeaderCaption("Kunde");
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
		filter.addTextChangeListener(e -> listData());

		// Connect selected Customer to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			} else {
				editor.edit((Bestellung) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.edit(new Bestellung(new Date())));

		// Initialize listing
		listData();
	}

	private void listData() {
		grid.setContainerDataSource(createBeanItemContainer(repo.findAll()));
	}

	private BeanItemContainer<Bestellung> createBeanItemContainer(List<Bestellung> findAll) {
		BeanItemContainer<Bestellung> container = new BeanItemContainer<>(Bestellung.class, findAll);
		container.addNestedContainerProperty("kunde.name");
		return container;
	}

	@Subscribe
	public void processBestllungEvent(final BestellungEvent event) {
		editor.setVisible(false);
		listData();
	}

}
