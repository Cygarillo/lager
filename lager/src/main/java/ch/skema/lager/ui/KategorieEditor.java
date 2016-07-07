package ch.skema.lager.ui;

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

import ch.skema.lager.domain.Kategorie;
import ch.skema.lager.event.EventSystem;
import ch.skema.lager.event.KategorieEvent;
import ch.skema.lager.repository.KategorieRepository;

@SpringComponent
@UIScope
public class KategorieEditor extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private final KategorieRepository repository;

	private Kategorie kategorie;
	@Autowired
	EventSystem eventSystem;

	/* Fields to edit properties in Customer entity */
	TextField name = new TextField("Name");

	/* Action buttons */
	Button save = new Button("Speichern", FontAwesome.SAVE);
	Button cancel = new Button("Abbrechen");
	CssLayout actions = new CssLayout(save, cancel);

	@Autowired
	public KategorieEditor(KategorieRepository repository) {
		this.repository = repository;

		addComponents(name, actions);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			repository.save(kategorie);
			eventSystem.fire(new KategorieEvent());
		});
		cancel.addClickListener(e -> edit(kategorie));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void edit(Kategorie c) {
		final boolean persisted = c.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			kategorie = repository.findOne(c.getId());
		} else {
			kategorie = c;
		}
		cancel.setVisible(persisted);

		// Bind customer properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(kategorie, this);

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
	}

}
