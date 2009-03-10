package com.umlet.control.io;

import java.io.File;
import java.io.FilenameFilter;

public class PaletteFilenameFilter implements FilenameFilter {
	public boolean accept(File arg0, String name) {
		return name.endsWith(".uxf");
	}
}
