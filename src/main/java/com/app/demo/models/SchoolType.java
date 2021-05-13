package com.app.demo.models;

import java.util.HashMap;
import java.util.Map;

public enum SchoolType {
	CONG_LAP("Công lập"),NGOAI_CONG_LAP("Ngoài công lập");
	 public final  String label;
	 public String getValues() {
		  return label;
	  }
	    private SchoolType(String label) {
	        this.label = label;
	    }
	    private static final Map<String, SchoolType> BY_LABEL = new HashMap<>();
	    
	    static {
	        for (SchoolType e: values()) {
	            BY_LABEL.put(e.label, e);
	        }
	    }
	    
	    public static SchoolType valueOfLabel(String label) {
	        return BY_LABEL.get(label);
	    }
}
