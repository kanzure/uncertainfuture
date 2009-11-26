package org.singinst.uf.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.RootPaneContainer;

public class SwingHelp {
	private static final SwingHelp instance = new SwingHelp();
	private final JPanel helpPanel = new JPanel(new BorderLayout());
	private final JEditorPane helpText = new JEditorPane();
	private RootPaneContainer root;
	private Component oldGlassPane;

	public void init(final RootPaneContainer root, String mainHelpString) {
		this.root = root;
		helpText.setEditable(false);
		helpText.setContentType("text/html");
		helpPanel.add(new JScrollPane(helpText));
		helpPanel.addMouseListener(new MouseAdapter() {});
		Box southBox = Box.createHorizontalBox();

		JButton helpButton = new JButton("OK");
		helpButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				root.setGlassPane(oldGlassPane);
				oldGlassPane.setVisible(false);
			}
			
		});

		southBox.add(Box.createHorizontalGlue());
		southBox.add(helpButton);
		southBox.add(Box.createHorizontalGlue());
		helpPanel.add(southBox, BorderLayout.SOUTH);
		helpPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		if (mainHelpString != null) {
			help(mainHelpString);
		}
	}

	public void help(String helpString) {
		helpText.setText(helpString);
		oldGlassPane = root.getGlassPane();
		root.setGlassPane(helpPanel);
		helpPanel.setVisible(true);
	}

	public static SwingHelp getInstance() {
		return instance;
	}

}
