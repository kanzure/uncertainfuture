package org.singinst.uf.view;

import java.applet.AppletContext;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.singinst.uf.presenter.Store;

public class AppletStore extends Store {

	private final AppletContext appletContext;

	public AppletStore(AppletContext appletContext) {
		this.appletContext = appletContext;
	}

	@Override
	public Double get(String key) {
		InputStream stream = appletContext.getStream(key);
		if (stream == null) {
			return null;
		}
		try {
			String string = new BufferedReader(new InputStreamReader(stream, "UTF-8")).readLine();
			return Double.valueOf(string);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void put(String key, double value) {
		try {
			appletContext.setStream(key, new ByteArrayInputStream(("" + value).getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
