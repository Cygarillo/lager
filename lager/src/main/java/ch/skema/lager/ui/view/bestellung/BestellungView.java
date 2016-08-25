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
import com.vaadin.ui.renderers.HtmlRenderer;

import ch.skema.lager.domain.Bestellung;
import ch.skema.lager.event.LagerEvent.BestellungEvent;
import ch.skema.lager.event.LagerEventBus;
import ch.skema.lager.repository.BestellungRepository;
import ch.skema.lager.ui.converter.IconStringToBooleanConverter;

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
	private BestellungDetailView editor;
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
		HorizontalLayout gridToolbar = new HorizontalLayout(filter, addNewBtn);
		gridToolbar.setSpacing(true);
		grid.setColumns("kunde.name");
		grid.addColumn("erledigt", Boolean.class).setRenderer(new HtmlRenderer(), new IconStringToBooleanConverter());
		grid.getColumn("kunde.name").setHeaderCaption("Kunde");
		grid.setSizeFull();

		VerticalLayout gridLayout = new VerticalLayout(gridToolbar, grid);
		gridLayout.setSpacing(true);
		HorizontalLayout main = new HorizontalLayout(gridLayout, editor);
		main.setSpacing(true);
		main.setSizeFull();

		main.setExpandRatio(gridLayout, 1);
		main.setExpandRatio(editor, 2);

		// build layout
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
		BeanItemContainer<Bestellung> container = createBeanItemContainer(repo.findAll());
		grid.setContainerDataSource(container);
		if (shouldRestoreSelection()) {
			grid.select(container.getItemIds().stream().filter(i -> editor.getBestellung().getId().equals(i.getId())).findFirst().get());
		} else {
			grid.select(container.firstItemId());
		}
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

	private boolean shouldRestoreSelection() {
		return editor.isVisible() && editor.getBestellung() != null;
	}
}
