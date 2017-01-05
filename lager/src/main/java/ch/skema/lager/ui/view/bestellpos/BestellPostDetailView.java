package ch.skema.lager.ui.view.bestellpos;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.eventbus.Subscribe;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import ch.skema.lager.domain.BestellPosition;
import ch.skema.lager.domain.Produkt;
import ch.skema.lager.event.LagerEvent.ProduktEvent;
import ch.skema.lager.event.LagerEventBus;
import ch.skema.lager.repository.BestellungRepository;
import ch.skema.lager.repository.ProduktRepository;

@SpringComponent
@UIScope
public class BestellPostDetailView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	@Autowired
	private BestellungRepository bestellRepo;
	@Autowired
	private ProduktRepository produktRepo;

	/**
	 * The currently edited entity
	 */
	private BestellPosition bestellPos;
	private ComboBox produkt = new ComboBox("Kunde");

	private Grid bestellpositionGrid = new Grid();

	@PostConstruct
	public void init() {
		// Configure and style components
		setSpacing(true);

		produkt.setItemCaptionPropertyId("name");
		produkt.setNullSelectionAllowed(false);
		produkt.setFilteringMode(FilteringMode.CONTAINS);
		loadProdukte();

		// bestellposition
		bestellpositionGrid.setColumns("produkt.name", "anzahl");
		bestellpositionGrid.setSizeFull();

		addComponents(produkt, bestellpositionGrid);
		forEach(e -> {
			e.setSizeFull();
		});

		setVisible(false);
		LagerEventBus.register(this);
	}

	private void loadBestellposition() {
//		List<BestellPosition> bestellPosition = bestellung.getBestellPosition();
//		BeanItemContainer<BestellPosition> container = new BeanItemContainer<>(BestellPosition.class, bestellPosition);
//		container.addNestedContainerProperty("produkt.name");
//		bestellpositionGrid.setContainerDataSource(container);
	}

	private void loadProdukte() {
		BeanItemContainer<Produkt> container = new BeanItemContainer<>(Produkt.class, produktRepo.findAll());
		produkt.setContainerDataSource(container);
		produkt.select(container.firstItemId());
	}

	@PreDestroy
	public void destroy() {
		LagerEventBus.unregister(this);
	}

	public final void edit(BestellPosition bestellPos) {
//		// Find fresh entity for editing
//		this.bestellPos = bestellRepo.findOne(bestellung.getId());
//		loadBestellposition();
//
//		// Bind customer properties to similarly named fields
//		// Could also use annotation or "manual binding" or programmatically
//		// moving values from fields to entities before saving
//		BeanFieldGroup.bindFieldsUnbuffered(this.bestellung, this);
//		setVisible(true);
//
//		produkt.setReadOnly(true);
//
//		// A hack to ensure the whole form is visible
		bestellpositionGrid.focus();
	}

	@Subscribe
	public void processProduktEvent(final ProduktEvent event) {
		loadProdukte();
	}

//	public Bestellung getBestellung() {
//		return bestellung;
//	}

}
