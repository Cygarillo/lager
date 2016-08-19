package ch.skema.lager.ui.editor;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ch.skema.lager.domain.Bestellung;
import ch.skema.lager.repository.BestellungRepository;

@SpringComponent
@UIScope
public class BestellungEditor extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	@Autowired
	private BestellungRepository repository;

	/**
	 * The currently edited entity
	 */
	private Bestellung bestellung;

	private ComboBox kunde = new ComboBox("Wähle Kunde");

	/* Action buttons */
	private Button save = new Button("Speichern", FontAwesome.SAVE);
	private Button cancel = new Button("Abbrechen");
	private CssLayout actions = new CssLayout(save, cancel);

	public BestellungEditor() {
		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		addComponents(kunde, actions);

		setVisible(false);
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void edit(Bestellung bestellung) {
		final boolean persisted = bestellung.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			this.bestellung = repository.findOne(bestellung.getId());
		} else {
			this.bestellung = bestellung;
		}
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

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
//		deactivate.addClickListener(e -> h.onChange());
	}

}
