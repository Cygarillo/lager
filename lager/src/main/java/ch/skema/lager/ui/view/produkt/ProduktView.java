package ch.skema.lager.ui.view.produkt;

import java.util.List;

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

import ch.skema.lager.domain.Produkt;
import ch.skema.lager.event.LagerEvent.ProduktEvent;
import ch.skema.lager.event.LagerEventBus;
import ch.skema.lager.repository.ProduktRepository;

@SpringView(name = ProduktView.VIEW_NAME)
@UIScope
public class ProduktView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	/*
	 * This view is registered automatically based on the @SpringView
	 * annotation. As it has an empty string as its view name, it will be shown
	 * when navigating to the Homepage
	 */
	public static final String VIEW_NAME = "ProdukteView";
	@Autowired
	private ProduktRepository repo;
	@Autowired
	private ProduktEditor editor;
	private Grid grid;
	private TextField filter;
	private Button addNewBtn;

	@PostConstruct
	void init() {
		this.grid = new Grid();
		this.filter = new TextField();
		this.addNewBtn = new Button("Neues Produkt", FontAwesome.PLUS);
		buildLayout();
		LagerEventBus.register(this);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// the view is constructed in the init() method()
	}

	@Override
	public void detach() {
		super.detach();
		LagerEventBus.unregister(this);
	}

	private void buildLayout() {
		HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);
		toolbar.setSpacing(true);
		grid.setColumns("name", "kategorie.name", "verkaufspreis", "einkaufspreisSl", "einkaufspreisBern", "abgaben", "aktiv");
		grid.getColumn("kategorie.name").setHeaderCaption("Kategorie");
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
		filter.addTextChangeListener(e -> listData(e.getText()));

		// Connect selected Customer to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			} else {
				editor.editProdukt((Produkt) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editProdukt(new Produkt("")));

		// Initialize listing
		listData(null);
	}

	private void listData(String text) {
		if (StringUtils.isEmpty(text)) {
			grid.setContainerDataSource(createBeanItemContainer(repo.findAll()));
		} else {
			grid.setContainerDataSource(createBeanItemContainer(repo.findByNameStartsWithIgnoreCase(text)));
		}
	}

	@Subscribe
	public void processProduktEvent(final ProduktEvent event) {
		editor.setVisible(false);
		listData(filter.getValue());
	}

	private BeanItemContainer<Produkt> createBeanItemContainer(List<Produkt> findAll) {
		BeanItemContainer<Produkt> container = new BeanItemContainer<>(Produkt.class, findAll);
		container.addNestedContainerProperty("kategorie.name");
		return container;
	}

}
