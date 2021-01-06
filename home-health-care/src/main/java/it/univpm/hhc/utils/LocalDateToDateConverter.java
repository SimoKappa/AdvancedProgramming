package it.univpm.hhc.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class LocalDateToDateConverter implements Converter<LocalDate, Date> {

	@Override
	public Date convert(LocalDate source) {
		Date res = null;
		
		if (source != null) {
			res = Date.from(source.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		}
		
		return res;
	}
		
}