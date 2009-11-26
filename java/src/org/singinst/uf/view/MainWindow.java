package org.singinst.uf.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.singinst.uf.model.ConclusionReportGenerator;

import org.singinst.uf.model.NodeMetadata;
import org.singinst.uf.model.NodeMetadataFactory;
import org.singinst.uf.model.ScalarRelation;
import org.singinst.uf.model.ScalarSchema;
import org.singinst.uf.model.ScalarValueHolder;
import org.singinst.uf.model.SummarySource;
import org.singinst.uf.model.ValueListener;
import org.singinst.uf.presenter.Completion;
import org.singinst.uf.presenter.LineBounds;
import org.singinst.uf.presenter.NumericEntry;
import org.singinst.uf.presenter.Status;

class MainWindow {
	// TODO5
	// Required for adding actionlisteners to the previous and next buttons
	private JTabbedPane pane;

	Container createContainer() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBorder(border());

		List<NodeMetadata> nodeMetadataObjs = NodeMetadataFactory.createTheNetwork();

		final ArrayList<String> nodeNames = new ArrayList<String>();
		pane = new JTabbedPane();
		
		for (NodeMetadata nodeMetadata : nodeMetadataObjs) {
			pane.addTab(nodeMetadata.getNode().getIdString(), createPane(nodeMetadata, nodeNames));
			nodeNames.add(nodeMetadata.getNode().getIdString());
		}
		pane.setSelectedIndex(0);
		
		pane.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				AppletBrowser.getInstance().loadPage(nodeNames.get(pane.getSelectedIndex()));
				Status.singleton.setValue("");
			}
			
		});

		mainPanel.add(pane, BorderLayout.CENTER);

//		pane.setSelectedIndex(pane.getComponentCount() - 1);
		return mainPanel;
	}

	private Border border() {
		return BorderFactory.createEmptyBorder(3, 3, 3, 3);
	}

	private Border largeBorder() {
		return BorderFactory.createEmptyBorder(12, 12, 12, 12);
	}

	private Component createPane(final NodeMetadata nodeMetadata, final ArrayList<String> nodeNames) {
		Box vertical = Box.createVerticalBox();
		vertical.setBorder(border());
		vertical.setName(nodeMetadata.getNode().getIdString());

		Box summaries = Box.createVerticalBox();
		JPanel panePanel = new JPanel(new BorderLayout());
		
		panePanel.add(createUserQuestion(nodeMetadata, nodeNames), BorderLayout.NORTH);
		for (ScalarSchema simpleScalar : nodeMetadata.getSimpleScalars()) {
			vertical.add(createGraph(simpleScalar));
			for (Component summary : createSummaries(simpleScalar)) {
				summaries.add(summary);
			}
		}
		for (ScalarRelation relation : nodeMetadata.getScalarRelations()) {
			vertical.add(createGraph(relation));
			for (Component summary : createSummaries(relation)) {
				summaries.add(summary);
			}
		}
		
		//if (nodeMetadata.getNode().getIdString() != "A6")
		addProperties(nodeMetadata, vertical, nodeNames);
		vertical.add(summaries);
		
		Box statusBox = Box.createHorizontalBox();
		statusBox.setBorder(largeBorder());
		final JLabel label = new JLabel("Application loaded.");
		statusBox.add(label);
		Status.singleton.addListener(new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				label.setText(Status.singleton.getValue());
			}
			
		});
		vertical.add(statusBox);
		
//		Box helpBox = Box.createHorizontalBox();
//		helpBox.setBorder(largeBorder());
//		JButton helpButton = new JButton("Help");
//		helpButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				SwingHelp.getInstance().help(nodeMetadata.getUserText().getHelp());
//			}
//		});
//		helpBox.add(helpButton);
//		vertical.add(helpBox);

		vertical.add(Box.createGlue());
		panePanel.add(vertical, BorderLayout.CENTER);
		return panePanel;
	}

	private void addProperties(NodeMetadata nodeMetadata, Box vertical, final ArrayList<String> nodeNames) {
		Box properties = null;

		int i = 0;
		for (ScalarSchema scalarSchema : nodeMetadata.getScalars()) {
			if (i % 3 == 0) {
				properties = attachHorizontalBox(vertical);
			}
			Component newProp = createPropertyPanel(scalarSchema);
			if (newProp != null) {
				properties.add(newProp);
				i++;
			}
		}
	}

	private Box attachHorizontalBox(Box vertical) {
		Box properties = Box.createHorizontalBox();
		vertical.add(properties);
		return properties;
	}

	private Component createGraph(ScalarRelation relation) {
		return new Graph(relation).getPanel();
	}

	
	private List<Component> createSummaries(final SummarySource summarySource) {
		final List<Component> labels = new ArrayList<Component>();
		for (final ConclusionReportGenerator conclusionReportGenerator : summarySource.getConclusionGenerators()) {
			final JLabel label = new JLabel("<html>Calculating...<br></html>");
			label.setMinimumSize(new Dimension(100, 15));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setAlignmentX(0.5f);
			labels.add(label);
			for (final ScalarValueHolder scalarValueHolder : summarySource.getScalarValues()) {
				scalarValueHolder.addUpdateListener(new ValueListener() {
					public void fireUpdate(double value) {
						String text = conclusionReportGenerator.getText(scalarValueHolder, value);
						label.setText("<html>" + text + "</html>");
					}
				});
			}
		}
		return labels;
	}

	private Component createUserQuestion(NodeMetadata nodeMetadata, final ArrayList<String> nodeNames) {
		//JLabel question = new JLabel(nodeMetadata.getUserText().getQuestion(), SwingConstants.CENTER);
		JLabel question = new JLabel(nodeMetadata.getUserText().getQuestion());
		//question.setEditable(false);
		question.setForeground(Color.RED);
		//question.setLineWrap(true);
		//question.setWrapStyleWord(true);
		question.setMaximumSize(question.getPreferredSize());
		
		JPanel prevQuestionNextBox = new JPanel(new BorderLayout());//Box.createHorizontalBox();
		
		JButton prev = new JButton("Previous");
		prev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = pane.getSelectedIndex();
				if (index > 0) {
					AppletBrowser.getInstance().loadPage(nodeNames.get(index-1));
					pane.setSelectedIndex(index-1);
				}
				Status.singleton.setValue("");
			}
			
		});
		prevQuestionNextBox.add(prev, BorderLayout.WEST);
		
		prevQuestionNextBox.add(question, BorderLayout.CENTER);
		
		JButton next = new JButton("Next");
		next.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = pane.getSelectedIndex();
				if (index+1 < nodeNames.size()) {
					AppletBrowser.getInstance().loadPage(nodeNames.get(index+1));
					pane.setSelectedIndex(index+1);
				}
				Status.singleton.setValue("");
			}
			
		});
		prevQuestionNextBox.add(next, BorderLayout.EAST);
		
		return prevQuestionNextBox;
	}

	/**
	 * 
	 * 
	 * @param scalarSchema
	 * @return
	 * 	THe new property to add if scalarSchema.visibleProperty is set to true, else returns null.
	 */
	private Component createPropertyPanel(ScalarSchema scalarSchema) {
		if (!scalarSchema.displayProperty())
			return null;
		JFormattedTextField propertyValueDisplay = new JFormattedTextField(
				NumericEntry.getScalarFormat());
		propertyValueDisplay.setColumns(NumericEntry.maxColumns());
		propertyValueDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
		propertyValueDisplay.setMaximumSize(propertyValueDisplay.getMinimumSize());

		Box propertyPanel = Box.createHorizontalBox();
		propertyPanel.add(new JLabel("<html>" + scalarSchema.getPrefix() + "</html>", SwingConstants.RIGHT));
		propertyPanel.add(propertyValueDisplay);
		propertyPanel.add(new JLabel(scalarSchema.getUnit() + scalarSchema.getSuffix()));
		propertyPanel.setAlignmentX(0.5f);
		propertyPanel.setBorder(largeBorder());
		propertyPanel.setMaximumSize(propertyPanel.getPreferredSize());

		new TextScalarBinder().bind(scalarSchema.getScalarValueHolder(), propertyValueDisplay);
		propertyPanel.setPreferredSize(propertyPanel.getMinimumSize());
		return propertyPanel;
	}

	private Component createGraph(ScalarSchema scalarSchema) {
		LineBounds bounds = scalarSchema.getLineBounds();
		JSlider slider = new JSlider((int) bounds.getLowerBound(), (int) bounds.getUpperBound());
		new RangeScalarBinder().bind(scalarSchema.getScalarValueHolder(), slider.getModel());
		return slider;
	}

	public Component createGlassPane(final RootPaneContainer container) {
		final JPanel glassPane = new JPanel();
		final JProgressBar bar = new JProgressBar();
		final JLabel label = new JLabel();
		glassPane.add(label);
		glassPane.add(bar);
		glassPane.addMouseListener(new MouseAdapter() {});
		final Completion completion = Completion.getInstance();
		// TODO5 update listener
		completion.addUpdateListener(new ValueListener() {
			public void fireUpdate(double value) {
				if (value == 0) {
					container.setGlassPane(glassPane);
					glassPane.setVisible(true);
				}

				int completed = completion.getCompleted();
				int numStages = completion.getNumStages();
				if (completed < numStages) {
					label.setText(completion.getDisplay());
					bar.setMaximum(numStages);
					bar.setValue(completed);
				} else {
					glassPane.setVisible(false);
				}
			}
			
		});
		return glassPane;
	}


}
