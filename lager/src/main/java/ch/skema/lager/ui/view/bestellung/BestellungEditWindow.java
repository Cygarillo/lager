package ch.skema.lager.ui.view.bestellung;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ch.skema.lager.domain.BestellPosition;
import ch.skema.lager.domain.Bestellung;
import ch.skema.lager.domain.Kunde;
import ch.skema.lager.domain.Produkt;
import ch.skema.lager.event.LagerEvent;
import ch.skema.lager.event.LagerEventBus;
import ch.skema.lager.repository.BestellungRepository;
import ch.skema.lager.repository.KundeRepository;
import ch.skema.lager.repository.ProduktRepository;

@UIScope
@SpringComponent
public class BestellungEditWindow extends Window {
	private static final long serialVersionUID = 1L;

	public static final String ID = "BestellungEditWindow";

	private BeanFieldGroup<Bestellung> fieldGroup;
	/*
	 * Fields for editing the User object are defined here as class members.
	 * They are later bound to a FieldGroup by calling
	 * fieldGroup.bindMemberFields(this). The Fields' values don't need to be
	 * explicitly set, calling fieldGroup.setItemDataSource(user) synchronizes
	 * the fields with the user object.
	 */
	@PropertyId("firstName")
	private TextField firstNameField;
	@PropertyId("lastName")
	private TextField lastNameField;
	@PropertyId("title")
	private ComboBox titleField;
	@PropertyId("male")
	private OptionGroup sexField;
	@PropertyId("email")
	private TextField emailField;
	@PropertyId("location")
	private TextField locationField;
	@PropertyId("phone")
	private TextField phoneField;
//	@PropertyId("newsletterSubscription")
//	private OptionalSelect<Integer> newsletterField;
	@PropertyId("website")
	private TextField websiteField;
	@PropertyId("bio")
	private TextArea bioField;

	@PropertyId("kunde")
	private ComboBox kunde;
	@PropertyId("erledigt")
	private CheckBox erledigt;

	private Bestellung bestellung;

	@Autowired
	private KundeRepository kundeRepo;
	@Autowired
	private BestellungRepository bestellRepo;
	@Autowired
	private ProduktRepository produktRepo;

	public BestellungEditWindow() {
		setId(ID);

		setModal(true);
		setResizable(false);
		setClosable(false);
		setHeight(90.0f, Unit.PERCENTAGE);
		setWidth(70.0f, Unit.PERCENTAGE);

	}

	private Component buildMainComponent() {
		HorizontalLayout root = new HorizontalLayout();
		root.setWidth(100.0f, Unit.PERCENTAGE);
		root.setSpacing(true);
		root.setMargin(true);

		FormLayout details = new FormLayout();
		details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		root.addComponent(details);
		root.setExpandRatio(details, 1);

		Label section = new Label("Contact Info");
		section.addStyleName(ValoTheme.LABEL_H4);
		section.addStyleName(ValoTheme.LABEL_COLORED);
		details.addComponent(section);

		emailField = new TextField("Email");
		emailField.setWidth("100%");
		emailField.setRequired(true);
		emailField.setNullRepresentation("");
		details.addComponent(emailField);

		locationField = new TextField("Location");
		locationField.setWidth("100%");
		locationField.setNullRepresentation("");
		locationField.setComponentError(new UserError("This address doesn't exist"));
		details.addComponent(locationField);

		phoneField = new TextField("Phone");
		phoneField.setWidth("100%");
		phoneField.setNullRepresentation("");
		details.addComponent(phoneField);

		section = new Label(bestellung.getId() == null ? "Bestellung erfassen" : "Bestellung editieren");
		section.addStyleName(ValoTheme.LABEL_H4);
		section.addStyleName(ValoTheme.LABEL_COLORED);
		details.addComponent(section);

		kunde = new ComboBox("Kunde");
		kunde.setInvalidAllowed(false);
		kunde.setNewItemsAllowed(false);
		kunde.setNullSelectionAllowed(false);
		kunde.setItemCaptionPropertyId("name");
		details.addComponent(kunde);

		List<Kunde> kunden = kundeRepo.findAll();
		BeanItemContainer<Kunde> container = new BeanItemContainer<>(Kunde.class, kunden);
		kunde.setContainerDataSource(container);

		erledigt = new CheckBox("Erledigt");
		details.addComponent(erledigt);

		Grid grid = new Grid();
		grid.setColumns("produkt.name");
		grid.setColumns("anzahl");
		details.addComponent(grid);

		BeanItemContainer<BestellPosition> con = new BeanItemContainer<>(BestellPosition.class, bestellung.getBestellPosition());
		con.addNestedContainerProperty("produkt.name");
		grid.setContainerDataSource(con);

		Button addbestellPosButton = new Button("Neue Bestellung", FontAwesome.PLUS);
		addbestellPosButton.addClickListener(e -> {
			BestellPosition pos = new BestellPosition();
			pos.setAnzahl(42L);
			pos.setRabatt(BigDecimal.TEN);
			pos.setStueckpreis(new BigDecimal(18));
			Produkt produkt = produktRepo.findAll().get(0);
			pos.setProdukt(produkt);
			bestellung.getBestellPosition().add(pos);
		});
		details.addComponent(addbestellPosButton);

		return root;
	}

	private Component buildFooter() {
		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth(100.0f, Unit.PERCENTAGE);

		Button save = new Button("Speichern", FontAwesome.SAVE);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		save.addStyleName(ValoTheme.BUTTON_PRIMARY);
		save.addClickListener(new ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					// TODO only if valid
					fieldGroup.commit();
					bestellRepo.save(fieldGroup.getItemDataSource().getBean());
					LagerEventBus.post(new LagerEvent.BestellungEvent());

					Notification success = new Notification("Bestellung gespeichert");
					success.setDelayMsec(2000);
					success.setStyleName("bar success small");
					success.setPosition(Position.BOTTOM_CENTER);
					success.show(Page.getCurrent());

					close();
				} catch (Exception e) {
					Notification.show("Error while updating profile", Type.ERROR_MESSAGE);
				}

			}
		});
		save.focus();
		Button cancel = new Button("Abbrechen");
		cancel.addClickListener(e -> close());
		footer.addComponents(save, cancel);

		footer.setComponentAlignment(save, Alignment.TOP_RIGHT);
		return footer;
	}

	public void open(final Bestellung bestellung) {
		UI.getCurrent().addWindow(this);
		this.bestellung = bestellung;
		VerticalLayout content = new VerticalLayout();
		content.setSizeFull();
		content.setMargin(new MarginInfo(true, false, false, false));
		setContent(content);

		Component main = buildMainComponent();
		content.addComponent(main);
		content.setExpandRatio(main, 1f);

		content.addComponent(buildFooter());

		fieldGroup = new BeanFieldGroup<>(Bestellung.class);
		fieldGroup.bindMemberFields(this);
		fieldGroup.setItemDataSource(bestellung);
		this.focus();
	}
}
