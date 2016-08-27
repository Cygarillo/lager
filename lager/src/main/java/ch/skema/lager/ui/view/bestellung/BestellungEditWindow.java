package ch.skema.lager.ui.view.bestellung;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import ch.skema.lager.domain.Bestellung;

@SuppressWarnings("serial")
public class BestellungEditWindow extends Window {

	public static final String ID = "BestellungEditWindow";

	private final BeanFieldGroup<Bestellung> fieldGroup;
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

	private BestellungEditWindow(final Bestellung bestellung) {
		setId(ID);

		setModal(true);
		setResizable(false);
		setClosable(false);
		setHeight(90.0f, Unit.PERCENTAGE);
		setWidth(70.0f, Unit.PERCENTAGE);

		VerticalLayout content = new VerticalLayout();
		content.setSizeFull();
		content.setMargin(new MarginInfo(true, false, false, false));
		setContent(content);

		Component main = buildMainComponent();
		content.addComponent(main);
		content.setExpandRatio(main, 1f);

		content.addComponent(buildFooter());

		fieldGroup = new BeanFieldGroup<>(Bestellung.class);
//		fieldGroup.bindMemberFields(this);
//		fieldGroup.setItemDataSource(bestellung);
	}

	private Component buildMainComponent() {
		HorizontalLayout root = new HorizontalLayout();
		root.setWidth(100.0f, Unit.PERCENTAGE);
		root.setSpacing(true);
		root.setMargin(true);

		VerticalLayout pic = new VerticalLayout();
		pic.setSizeUndefined();
		pic.setSpacing(true);
		Image profilePic = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
		profilePic.setWidth(100.0f, Unit.PIXELS);
		pic.addComponent(profilePic);

		Button upload = new Button("Change…", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("Not implemented in this demo");
			}
		});
		upload.addStyleName(ValoTheme.BUTTON_TINY);
		pic.addComponent(upload);

		root.addComponent(pic);

		FormLayout details = new FormLayout();
		details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		root.addComponent(details);
		root.setExpandRatio(details, 1);

		firstNameField = new TextField("First Name");
		details.addComponent(firstNameField);
		lastNameField = new TextField("Last Name");
		details.addComponent(lastNameField);

		titleField = new ComboBox("Title");
		titleField.setInputPrompt("Please specify");
		titleField.addItem("Mr.");
		titleField.addItem("Mrs.");
		titleField.addItem("Ms.");
		titleField.setNewItemsAllowed(true);
		details.addComponent(titleField);

		sexField = new OptionGroup("Sex");
		sexField.addItem(Boolean.FALSE);
		sexField.setItemCaption(Boolean.FALSE, "Female");
		sexField.addItem(Boolean.TRUE);
		sexField.setItemCaption(Boolean.TRUE, "Male");
		sexField.addStyleName("horizontal");
		details.addComponent(sexField);

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

//		newsletterField = new OptionalSelect<Integer>();
//		newsletterField.addOption(0, "Daily");
//		newsletterField.addOption(1, "Weekly");
//		newsletterField.addOption(2, "Monthly");
//		details.addComponent(newsletterField);

		section = new Label("Additional Info");
		section.addStyleName(ValoTheme.LABEL_H4);
		section.addStyleName(ValoTheme.LABEL_COLORED);
		details.addComponent(section);

		websiteField = new TextField("Website");
		websiteField.setInputPrompt("http://");
		websiteField.setWidth("100%");
		websiteField.setNullRepresentation("");
		details.addComponent(websiteField);

		bioField = new TextArea("Bio");
		bioField.setWidth("100%");
		bioField.setRows(4);
		bioField.setNullRepresentation("");
		details.addComponent(bioField);

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
			@Override
			public void buttonClick(ClickEvent event) {
				try {
//					fieldGroup.commit();
					// Updated user should also be persisted to database. But
					// not in this demo.

					Notification success = new Notification("Bestellung gespeichert");
					success.setDelayMsec(2000);
					success.setStyleName("bar success small");
					success.setPosition(Position.BOTTOM_CENTER);
					success.show(Page.getCurrent());

//					DashboardEventBus.post(new ProfileUpdatedEvent());
					close();
//				} catch (CommitException e) {
				} catch (Exception e) {
//					Notification.show("Error while updating profile", Type.ERROR_MESSAGE);
				}

			}
		});
		save.focus();
		Button cancel = new Button("Abbrechen");
		cancel.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);
		cancel.addClickListener(e -> close());
		footer.addComponents(save, cancel);

		footer.setComponentAlignment(save, Alignment.TOP_RIGHT);
		return footer;
	}

	public static void open(final Bestellung bestellung) {
		Window w = new BestellungEditWindow(bestellung);
		UI.getCurrent().addWindow(w);
		w.focus();
	}
}
