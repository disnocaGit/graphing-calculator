package graphplotter.popupWindows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Stack;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import functionComponents.Function;
import graphplotter.SwingFunctions;
import graphplotter.graphics.GraphicsDrawer;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

@SuppressWarnings("serial")
public class AddFunctionWindow extends PopupWindow {

	private JTextField textField;
	private JButton addButton;
	
	private GraphicsDrawer graphicsDrawer;
	private Stack<Color> colorStack;
	private Dimension graphicsSize;
	
	
	public AddFunctionWindow(JFrame parent, String title, GraphicsDrawer graphicsDrawer, Stack<Color> colorStack, Dimension graphicsSize) {
		super(parent, title);
		this.graphicsDrawer = graphicsDrawer;
		this.colorStack = colorStack;
		this.graphicsSize = graphicsSize;
	}
	
	@Override
	protected void addComponents(Container contentPane) {
		textField = new JTextField();
		
		addButton = new JButton("Add");
		addButton.setFocusable(false);
		addButton.addActionListener(this);
		addButton.getInputMap().put(KeyStroke.getKeyStroke("released ENTER"), "enter");		// not working
		addButton.getActionMap().put("enter", new SimulateButtonPressAction(addButton));
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setFocusable(false);
		cancelButton.addActionListener(this);
		
		JPanel inputPane = new JPanel();
		inputPane.setLayout(new BoxLayout(inputPane, BoxLayout.PAGE_AXIS));
		
		JLabel label = new JLabel("Enter the function's expression:");
		inputPane.add(label);
		inputPane.add(Box.createRigidArea(new Dimension(0,10)));
		inputPane.add(textField);
		inputPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(addButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		buttonPane.add(cancelButton);
		
		contentPane.add(inputPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);
		
		SwingFunctions.evenButtonsWidth(addButton, cancelButton);
	}
	
	
	private void addFunction(String expression) {
		Color color = colorStack.pop();
		Function function = new Function(graphicsSize, expression, color);
		graphicsDrawer.addFunction(function);
		SwingFunctions.updateFrameContents(parent);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// always closes window after a button is pressed unless user tried to add nothing or an invalid function
		// in which case exits this method and window remains open
		if(e.getSource() == addButton) {
			try {
				addFunction(textField.getText().trim());
			} catch(UnknownFunctionOrVariableException e1) {
				SwingFunctions.showErrorMessage(this, "Invalid function");
				return;
			} catch(IllegalArgumentException e2) {
				SwingFunctions.showErrorMessage(this, "Please enter a function");
				return;
			}
		}
		
		parent.setEnabled(true);
		this.dispose();
	}

}