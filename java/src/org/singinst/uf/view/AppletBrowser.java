package org.singinst.uf.view;

import java.applet.Applet;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLDocument;

import com.sun.java.browser.dom.DOMAccessor;
import com.sun.java.browser.dom.DOMAction;
import com.sun.java.browser.dom.DOMService;

public class AppletBrowser {

	private DOMService service;
	private static AppletBrowser instance;
	private final Applet applet;
	private static BufferedWriter log = null;
	public AppletBrowser(Applet applet) {
		this.applet = applet;
		try {
			service = DOMService.getService(applet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	private void log(String msg) {
//		if (log == null) {			
////			File logFile = new File("uf-log" + new SimpleDateFormat("yyMMdd").format(new Date()) + ".log");
//			File logFile = null;
//			try {
//				logFile = File.createTempFile("uf-log", "log");
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//			OutputStream outStream = null;
//			try {
//				outStream = new FileOutputStream(logFile);
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			}
//			log = new BufferedWriter(new OutputStreamWriter(outStream));
//			System.out.println("FIXME: Created log file " + logFile.getAbsolutePath());
//		}
//		try {
//			log.write(msg + "\n");
//			log.flush();
//			System.out.println("FIXME: " + msg);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
////		try {
////			log.close();
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//	}
	
	public void loadPage(final String helpName) {
//		log("loadPage(" + helpName + ")");
		
		if (service != null) {
//			log("service != null");
		    try {
				service.invokeAndWait(new DOMAction()
				{
				    public Object run(DOMAccessor accessor)
				    {
				         HTMLDocument doc = (HTMLDocument) accessor.getDocument(applet);
				         Element element = doc.getElementById("sidebar");
//				         log("doc: " + doc + ", element: " + element);
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
