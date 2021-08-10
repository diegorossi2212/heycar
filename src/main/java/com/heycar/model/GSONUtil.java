package com.heycar.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heycar.exception.UtilityClassInstantiationException;


public final class GSONUtil {

	public static final String LONG_DATE_PATTERN = "dd-MM-yyyy HH:mm:ss.SSS";
	
    public static final Gson DEFAULT_GSON = new GsonBuilder().disableHtmlEscaping().setDateFormat(LONG_DATE_PATTERN).create();

    private GSONUtil() {
        throw new UtilityClassInstantiationException();
    }

}
