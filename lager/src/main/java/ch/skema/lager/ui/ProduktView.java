package ch.skema.lager.ui;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

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

	@PostConstruct
	void init() {
		this.grid = new Grid();
		this.filter = new TextField();
		this.addNewBtn = new Button("Neues Produkt", FontAwesome.PLUS);
		buildLayout();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// the view is constructed in the init() method()
	}

	@Autowired
	private ProduktRepository repo;
	@Autowired
	private ProduktEditor editor;
	private Grid grid;
	private TextField filter;
	private Button addNewBtn;

	private void buildLayout() {
		HorizontalLayout toolbar = new HorizontalLayout(filter, addNewBtn);
		toolbar.setSpacing(true);
		grid.setColumns("name", "kategorie.name", "verkaufspreis", "einkaufspreisSl", "einkaufspreisBern", "abgaben", "aktiv");
		grid.getColumn("kategorie.name").setHeaderCaption("Kategorie");

		grid.setSizeFull();
		HorizontalLayout main = new HorizontalLayout(grid, editor);
		main.setSpacing(true);
		main.setSizeFull();

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
				editor.editProdukt((Produkt) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Customer the new button is clicked
		addNewBtn.addClickListener(e -> editor.editProdukt(new Produkt("")));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listCustomers(filter.getValue());
		});

		// Initialize listing
		listCustomers(null);
	}

	private void listCustomers(String text) {
		if (StringUtils.isEmpty(text)) {
			BeanItemContainer<Produkt> container = new BeanItemContainer<>(Produkt.class, repo.findAll());
			container.addNestedContainerProperty("kategorie.name");
			grid.setContainerDataSource(container);
		} else {
			grid.setContainerDataSource(new BeanItemContainer<>(Produkt.class, repo.findByNameStartsWithIgnoreCase(text)));
		}

	}

}
