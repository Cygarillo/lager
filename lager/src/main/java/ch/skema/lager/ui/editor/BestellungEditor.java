package ch.skema.lager.ui.editor;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import ch.skema.lager.domain.Bestellung;
import ch.skema.lager.event.EventSystem;
import ch.skema.lager.event.KategorieEvent;
import ch.skema.lager.event.KategorieEvent.KategorieEventListener;
import ch.skema.lager.repository.BestellungRepository;

@SpringComponent
@UIScope
public class BestellungEditor extends VerticalLayout implements KategorieEventListener {
	private static final long serialVersionUID = 1L;

	@Autowired
	private BestellungRepository repository;
	@Autowired
	private EventSystem eventSystem;

	/**
	 * The currently edited entity
	 */
	private Bestellung bestellung;

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
	}

	public interface ChangeHandler {
		void onChange();
	}

	public final void edit(Bestellung c) {
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			bestellung = repository.findOne(c.getId());
		} else {
			bestellung = c;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(bestellung, this);
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

	@Override
	public void reloadEntries(KategorieEvent event) {
	}

}
