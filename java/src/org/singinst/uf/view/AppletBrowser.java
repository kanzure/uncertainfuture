package org.singinst.uf.view;

import java.applet.Applet;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.html.HTMLDocument;

import com.sun.java.browser.dom.DOMAccessor;
import com.sun.java.browser.dom.DOMAction;
import com.sun.java.browser.dom.DOMService;

public class AppletBrowser {

	private DOMService service;
	private static AppletBrowser instance;
	private final Applet applet;
	public AppletBrowser(Applet applet) {
		this.applet = applet;
		try {
			service = DOMService.getService(applet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void loadPage(final String helpName) {
		if (service != null) {
		    try {
				service.invokeAndWait(new DOMAction()
				{
				    public Object run(DOMAccessor accessor)
				    {
				         HTMLDocument doc = (HTMLDocument) accessor.getDocument(applet);
				         Element element = doc.getElementById("sidebar");
				         //for (int i = 0; i < nodeList.getLength(); i++) {
				        	 //Node node = nodeList.item(i);
				        	 //if (node instanceof Element) {
				        		 //Element element = (Element) node;
				        			 if (element.getAttribute("name").startsWith("ufHelp")) {
				        				 String pageName = "ufHelp/" + helpName + ".html";
				        				 element.setAttribute("src", pageName);
				        			 }
				    		//}
				         //}
				         return null;
				    }
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void init(Applet applet) {
		instance = new AppletBrowser(applet);
	}

	public static AppletBrowser getInstance() {
		return instance;
	}

}
