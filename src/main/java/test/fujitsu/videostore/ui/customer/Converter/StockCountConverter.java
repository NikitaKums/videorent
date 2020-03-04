package test.fujitsu.videostore.ui.customer.Converter;

import com.vaadin.flow.data.converter.StringToIntegerConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class StockCountConverter extends StringToIntegerConverter {

    public StockCountConverter() {
        super(0, "Could not convert value to " + Integer.class.getName() + ".");
    }

    @Override
    protected NumberFormat getFormat(Locale locale) {
        DecimalFormat format = new DecimalFormat();
        format.setMaximumFractionDigits(0);
        format.setDecimalSeparatorAlwaysShown(false);
        format.setParseIntegerOnly(true);
        format.setGroupingUsed(false);
        return format;
    }
}
