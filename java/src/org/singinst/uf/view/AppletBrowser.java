package org.singinst.uf.view;

import java.applet.Applet;
import java.rmi.UnexpectedException;

import netscape.javascript.JSObject;

import org.singinst.uf.common.LogUtil;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;
import org.w3c.dom.html.HTMLIFrameElement;

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
	
	/**
	 * Switch shown help page.
	 */
	public void loadPage(final String helpName) {
		
		if (service != null) {
		    try {
				service.invokeAndWait(new DOMAction()
				{
					/**
					 * Firefox on Mac has some issues with accessing HTML elements
					 * from applets and is currently not working. 
					 * 
					 * See http://forums.sun.com/thread.jspa?threadID=5391691.
					 * 
					 * This method is an approach that doesn't work, but might be used
					 * as a starting point.
					 */
					private void getMacSidebar() {
//				         element.getOwnerDocument() // Get document from element
//				         applet.getDocumentBase() // Get URL for applet base
				         JSObject w = JSObject.getWindow(applet);
				         Object o = w.eval("getSidebar()");
				         Object o2 = w.eval("document.getElementById('sidebar')");
				         LogUtil.info("getSidebar(): " + o);
				         LogUtil.info("document.getElementById('sidebar'): " + o2 + ", " + o2.getClass());
				         LogUtil.info("sidebar object has class: " + o.getClass());
				         if (o instanceof HTMLIFrameElement) {
				        	 HTMLIFrameElement e = (HTMLIFrameElement) o;				        
				        	 e.setAttribute("class", "fooBar");
				         }
				         Class c = o.getClass();
					}
				    public Object run(DOMAccessor accessor)
				    {
				    	 LogUtil.info("Applet: " + applet);
				         HTMLDocument doc = (HTMLDocument) accessor.getDocument(applet);
				         LogUtil.info("HTML document: " + doc);

//				         if (ViewUtil.runningOnApple()) {
//				        	 LogUtil.info("Running on OS X");
//				         } else {
//				        	 LogUtil.info("Running on ~OS X");
//				         }

				         if (doc == null) {
				        	 LogUtil.error(new UnexpectedException("HTML document could not be accessed!"));
				        	 return null;
				         }
				         Element element = doc.getElementById("sidebar");				         
				         LogUtil.info("Element with id sidebar: " + element);
				         if (element.getAttribute("name").startsWith("ufHelp")) {
				        	 String pageName = "ufHelp/" + helpName + ".html";
				        	 element.setAttribute("src", pageName);
				         }
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
