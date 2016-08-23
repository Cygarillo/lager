package ch.skema.lager.ui.view.kategorie;

import javax.annotation.PostConstruct;

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
import ch.skema.lager.event.LagerEvent;
import ch.skema.lager.event.LagerEventBus;
import ch.skema.lager.repository.KategorieRepository;

@SpringComponent
@UIScope
public class KategorieEditor extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	@Autowired
	private KategorieRepository repository;

	private Kategorie kategorie;

	/* Fields to edit properties in Customer entity */
	TextField name = new TextField("Name");

	/* Action buttons */
	Button save = new Button("Speichern", FontAwesome.SAVE);
	Button cancel = new Button("Abbrechen");
	CssLayout actions = new CssLayout(save, cancel);

	@PostConstruct
	void init() {
		name.setSizeFull();
		addComponents(name, actions);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> {
			repository.save(kategorie);
			LagerEventBus.post(new LagerEvent.KategorieEvent());
		});
		cancel.addClickListener(e -> edit(kategorie));
		setVisible(false);
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

	public Kategorie getKategorie() {
		return kategorie;
	}

}
