package org.singinst.uf.view;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.JApplet;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.singinst.uf.model.ScalarValueHolder;
import org.singinst.uf.presenter.Completion;
import org.singinst.uf.presenter.Status;
import org.singinst.uf.presenter.Store;

@SuppressWarnings("serial")
public class UfApplet extends JApplet {
	
    @Override
	public void init() {
        try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
			        createGUI();
			    }
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    public void loadData(String jsonSubset) {
    	Store.getInstance().loadData(jsonSubset);
    }

	/**
	 * 
	 */
	private void createGUI() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel"); 
		}
		catch (Exception e) {
			try {
				UIManager.setLookAndFeel("com.sun.java.swing.plaf.metal.OceanTheme");
			}
			catch (Exception e1) {}
		}
		
		Store.setInstance(new AppletStore(getAppletContext()));
		AppletBrowser.init(this);

		MainWindow mainWindow = new MainWindow();
		setContentPane(mainWindow.createContainer());
		JPanel glassPane = new JPanel();
		glassPane.add(mainWindow.createGlassPane(this));
		setGlassPane(glassPane);
		SwingHelp.getInstance().init(this, null);
//		SwingHelp.getInstance().init(this, UfHelp.getMainHelpString());
	}

	@Override
	public void destroy() {
		System.err.println("destroying applet and removing listeners");
		removeListenersAndChildren(this);
		Store.setInstance(null);
		Status.singleton.destroy();
		Completion.getInstance().destroy();
		ScalarValueHolder.destroyAll();
	}

	private void removeListenersAndChildren(Component component) {
		for (MouseListener listener : component.getMouseListeners()) {
			component.removeMouseListener(listener);
		}
		for (MouseMotionListener listener : component.getMouseMotionListeners()) {
			component.removeMouseMotionListener(listener);
		}
		for (PropertyChangeListener listener : component.getPropertyChangeListeners()) {
			component.removePropertyChangeListener(listener);
		}
		
		if (component instanceof AbstractButton) {
			AbstractButton button = (AbstractButton) component;
			for (ActionListener listener : button.getActionListeners()) {
				button.removeActionListener(listener);
			}
		}
		
		if (component instanceof Container) {
			Container container = (Container) component;
			for (Component child : container.getComponents()) {
				removeListenersAndChildren(child);
				remove(child);
			}
		}
	}
}
