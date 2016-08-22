package ch.skema.lager.ui.view.produkt;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ch.skema.lager.domain.Kategorie;
import ch.skema.lager.domain.Produkt;
import ch.skema.lager.event.LagerEvent;
import ch.skema.lager.event.LagerEventBus;
import ch.skema.lager.repository.KategorieRepository;
import ch.skema.lager.repository.ProduktRepository;

@SpringComponent
@UIScope
public class ProduktEditor extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ProduktRepository repository;
	@Autowired
	private KategorieRepository kategorieRepository;

	/**
	 * The currently edited customer
	 */
	private Produkt product;

	/* Fields to edit properties in Product entity */
	TextField name = new TextField("Produktname");
	TextField verkaufspreis = new TextField("Verkaufspreis an Schüler");
	TextField einkaufspreisSl = new TextField("Einkaufspreis Schulleiter");
	TextField einkaufspreisBern = new TextField("Einkaufspreis Bern");
	TextField abgaben = new TextField("Abgaben");
	CheckBox aktiv = new CheckBox("Aktiv");
	ComboBox kategorie = new ComboBox("Wähle Kategorie");

	/* Action buttons */
	Button save = new Button("Speichern", FontAwesome.SAVE);
	Button cancel = new Button("Abbrechen");
	CssLayout actions = new CssLayout(save, cancel);

	@PostConstruct
	public void init() {
		verkaufspreis.setNullRepresentation("");
		einkaufspreisSl.setNullRepresentation("");
		einkaufspreisBern.setNullRepresentation("");
		abgaben.setNullRepresentation("");

		kategorie.setInvalidAllowed(false);
		kategorie.setNullSelectionAllowed(false);
		kategorie.setFilteringMode(FilteringMode.CONTAINS);
		kategorie.setItemCaptionPropertyId("name");
		loadKategories();

		addComponents(name, kategorie, verkaufspreis, einkaufspreisSl, einkaufspreisBern, abgaben, aktiv, actions);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			if (isValid()) {
				repository.save(product);
				LagerEventBus.post(new LagerEvent.ProduktEvent());
			}
		});
		cancel.addClickListener(e -> editProdukt(product));
		setVisible(false);
		LagerEventBus.register(this);

		forEach(e -> {
			e.setSizeFull();
		});

	}

	private boolean isValid() {
		return kategorie.isValid();
	}

	@PreDestroy
	public void destroy() {
		LagerEventBus.unregister(this);
	}

	public void loadKategories() {
		BeanItemContainer<Kategorie> container = new BeanItemContainer<>(Kategorie.class, kategorieRepository.findAll());
		kategorie.setContainerDataSource(container);
//		kategorie.select(container.firstItemId());
	}

	public final void editProdukt(Produkt c) {
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			product = repository.findOne(c.getId());
		} else {
			product = c;
			product.setKategorie((Kategorie) kategorie.getConvertedValue());// default
																			// kategorie
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(product, this);
		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		kategorie.setImmediate(true);
		// Select all text in firstName field automatically
		name.selectAll();
	}

	@Subscribe
	public void reloadKategories(final LagerEvent.KategorieEvent event) {
		loadKategories();
	}

	public Produkt getProduct() {
		return product;
	}
}
