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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import ch.skema.lager.domain.Kategorie;
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
		initLayout();
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

	private void initLayout() {
		// build layout
		addComponent(filter);
		addComponent(addNewBtn);
		addComponent(grid);
		addComponent(editor);

		// Configure layouts and components
		setMargin(true);
		setSpacing(true);
		grid.setWidth(70, Unit.PERCENTAGE);
		grid.setHeight(320, Unit.PIXELS);
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

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listEntity(filter.getValue());
		});

		// Initialize listing
		listEntity(null);
	}

	private void listEntity(String text) {
		if (StringUtils.isEmpty(text)) {
			grid.setContainerDataSource(new BeanItemContainer(Kategorie.class, repo.findAll()));
		} else {
			grid.setContainerDataSource(new BeanItemContainer(Kategorie.class, repo.findByNameStartsWithIgnoreCase(text)));
		}
	}

}
