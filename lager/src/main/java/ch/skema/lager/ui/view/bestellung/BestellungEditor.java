package ch.skema.lager.ui.view.bestellung;

import java.util.List;

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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ch.skema.lager.domain.BestellPosition;
import ch.skema.lager.domain.Bestellung;
import ch.skema.lager.domain.Kunde;
import ch.skema.lager.event.LagerEvent.BestellungEvent;
import ch.skema.lager.event.LagerEvent.KundeEvent;
import ch.skema.lager.event.LagerEventBus;
import ch.skema.lager.repository.BestellungRepository;
import ch.skema.lager.repository.KundeRepository;

@SpringComponent
@UIScope
public class BestellungEditor extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	@Autowired
	private BestellungRepository bestellRepo;
	@Autowired
	private KundeRepository kundeRepo;

	/**
	 * The currently edited entity
	 */
	private Bestellung bestellung;
	private ComboBox kunde = new ComboBox("Wähle Kunde");

	/* Action buttons */
	private Button save = new Button("Speichern", FontAwesome.SAVE);
	private Button cancel = new Button("Abbrechen");
	private CssLayout actions = new CssLayout(save, cancel);
	private Grid bestellpositionGrid = new Grid();

	@PostConstruct
	public void init() {
		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		save.addClickListener(e -> {
			if (isValid()) {
				bestellRepo.save(bestellung);
				LagerEventBus.post(new BestellungEvent());
			}
		});
		// kunde
		kunde.setItemCaptionPropertyId("name");
		kunde.setNullSelectionAllowed(false);
		kunde.setFilteringMode(FilteringMode.CONTAINS);
		kunde.setSizeFull();
		loadKunden();

		// bestellposition
		bestellpositionGrid.setColumns("produkt.name", "anzahl");
		bestellpositionGrid.setSizeFull();

		addComponents(kunde, bestellpositionGrid, actions);
		forEach(e -> {
			e.setSizeFull();
		});
		setVisible(false);
		LagerEventBus.register(this);
	}

	private void loadBestellposition() {
		List<BestellPosition> bestellPosition = bestellung.getBestellPosition();
		BeanItemContainer<BestellPosition> container = new BeanItemContainer<>(BestellPosition.class, bestellPosition);
		container.addNestedContainerProperty("produkt.name");
		bestellpositionGrid.setContainerDataSource(container);
	}

	private boolean isValid() {
		return kunde.isValid();
	}

	private void loadKunden() {
		BeanItemContainer<Kunde> container = new BeanItemContainer<>(Kunde.class, kundeRepo.findAll());
		kunde.setContainerDataSource(container);
		kunde.select(container.firstItemId());
	}

	@PreDestroy
	public void destroy() {
		LagerEventBus.unregister(this);
	}

	public final void edit(Bestellung bestellung) {
		final boolean persisted = bestellung.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			this.bestellung = bestellRepo.findOne(bestellung.getId());
		} else {
			this.bestellung = bestellung;
			this.bestellung.setKunde((Kunde) kunde.getConvertedValue());
		}
		loadBestellposition();
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(this.bestellung, this);
		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
//		name.selectAll();
	}

	@Subscribe
	public void processKundeEvent(final KundeEvent event) {
		loadKunden();
	}

}
