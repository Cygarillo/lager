package ch.skema.lager.ui.editor;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ch.skema.lager.domain.Kunde;
import ch.skema.lager.event.LagerEvent;
import ch.skema.lager.event.LagerEventBus;
import ch.skema.lager.repository.KundeRepository;

@SpringComponent
@UIScope
public class KundeEditor extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private final KundeRepository repository;

	/**
	 * The currently edited customer
	 */
	private Kunde customer;

	/* Fields to edit properties in Customer entity */
	TextField name = new TextField("Name");

	/* Action buttons */
	Button save = new Button("Speichern", FontAwesome.SAVE);
	Button cancel = new Button("Abbrechen");
	CssLayout actions = new CssLayout(save, cancel);

	@Autowired
	public KundeEditor(KundeRepository repository) {
		this.repository = repository;

		addComponents(name, actions);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			repository.save(customer);
			LagerEventBus.post(new LagerEvent.KundeEvent());
		});
		cancel.addClickListener(e -> editCustomer(customer));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editCustomer(Kunde c) {
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			customer = repository.findOne(c.getId());
		} else {
			customer = c;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(customer, this);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		name.selectAll();
	}

}
