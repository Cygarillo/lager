package ch.skema.lager.ui.converter;

import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.server.FontAwesome;

public class IconStringToBooleanConverter extends StringToBooleanConverter {
	private static final long serialVersionUID = 1L;

	public IconStringToBooleanConverter() {
		super(FontAwesome.CHECK_CIRCLE_O.getHtml(), FontAwesome.CIRCLE_O.getHtml());
	}

}
