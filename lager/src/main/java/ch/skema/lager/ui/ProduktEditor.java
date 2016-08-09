package ch.skema.lager.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
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
import ch.skema.lager.event.EventSystem;
import ch.skema.lager.event.KategorieEvent;
import ch.skema.lager.event.KategorieEvent.KategorieEventListener;
import ch.skema.lager.repository.KategorieRepository;
import ch.skema.lager.repository.ProduktRepository;

@SpringComponent
@UIScope
public class ProduktEditor extends VerticalLayout implements KategorieEventListener {
	private static final long serialVersionUID = 1L;

	private final ProduktRepository repository;
	private final KategorieRepository kategorieRepository;

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

	EventSystem eventSystem;

	// TextField aktiv = new TextField("Produktname");;

	/* Action buttons */
	Button save = new Button("Speichern", FontAwesome.SAVE);
	Button cancel = new Button("Abbrechen");
	CssLayout actions = new CssLayout(save, cancel);

	@Autowired
	public ProduktEditor(ProduktRepository repository, KategorieRepository katRepo, EventSystem eventSystem) {
		this.repository = repository;
		this.kategorieRepository = katRepo;
		this.eventSystem = eventSystem;
		verkaufspreis.setNullRepresentation("");
		einkaufspreisSl.setNullRepresentation("");
		einkaufspreisBern.setNullRepresentation("");
		abgaben.setNullRepresentation("");

		kategorie.setInvalidAllowed(false);
		kategorie.setNullSelectionAllowed(false);

		reloadKategories();

		kategorie.setItemCaptionPropertyId("name");

		addComponents(name, kategorie, verkaufspreis, einkaufspreisSl, einkaufspreisBern, abgaben, aktiv, actions);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> repository.save(product));
		cancel.addClickListener(e -> editProdukt(product));
		setVisible(false);

		eventSystem.addListener(this);

	}

	private void reloadKategories() {
		BeanItemContainer<Kategorie> container = new BeanItemContainer<>(Kategorie.class, kategorieRepository.findAll());
		kategorie.setContainerDataSource(container);
		kategorie.select(container.firstItemId());
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void editProdukt(Produkt c) {
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			product = repository.findOne(c.getId());
		} else {
			product = c;
			product.setKategorie((Kategorie) kategorie.getConvertedValue());//default kategorie
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(product, this);
		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		name.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
//		deactivate.addClickListener(e -> h.onChange());
	}

	@Override
	public void reloadEntries(KategorieEvent event) {
		reloadKategories();
	}

}
