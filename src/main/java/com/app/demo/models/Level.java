package com.app.demo.models;

import java.util.HashMap;
import java.util.Map;

public enum Level {
	TH("Tiểu Học"), THCS("THCS"), THPT("THPT");
	 public final  String label;
	 public String getValues() {
		  return label;
	  }
	    private Level(String label) {
	        this.label = label;
	    }
	    private static final Map<String, Level> BY_LABEL = new HashMap<>();
	    
	    static {
	        for (Level e: values()) {
	            BY_LABEL.put(e.label, e);
	        }
	    }
	    
	    public static Level valueOfLabel(String label) {
	        return BY_LABEL.get(label);
	    }
}
