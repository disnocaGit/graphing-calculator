package graphplotter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import functionComponents.Function;
import graphplotter.graphics.GraphicsDrawer;
import graphplotter.popupWindows.ListFunctionsFrame;
import graphplotter.popupWindows.RemoveFunctionFrame;

@SuppressWarnings("serial")
public class GraphPlotterFrame extends JFrame implements ActionListener {
	
	private JMenuBar menubar;
	private JMenu menuFunc, menuVW, menuGS;
	private JMenuItem mfuncAdd, mfuncRemove, mfuncList;
	private JMenuItem vwDefault, vwSetValues, vwZoomIn, vwZoomOut;
	private JMenuItem gsRoot, gsMax, gsMin, gsYIntersect, gsIntersect, gsYCalc, gsXCalc, gsIntegral;
	
	private Dimension graphicsSize;
	private GraphicsDrawer graphicsDrawer;
	
	private RemoveFunctionFrame removeFunctionFrame;
	private ListFunctionsFrame listFunctionsFrame;
	
	private final int MAX_FUNCTIONS = 6;
	
	private final Color[] functionColors = {Color.BLUE, Color.RED, Color.GREEN, Color.CYAN, Color.MAGENTA, Color.ORANGE};
	private Stack<Color> colorStack;
	private HashMap<Color, Integer> colorIdsMap;
	
	private int functionCount;
	

	public GraphPlotterFrame() {
		super("Graph Plotter");
	    this.setTitle("Graph Plotter");
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setResizable(false);
	    
	    graphicsSize = new Dimension(900,900);
	    functionCount = 0;
	    
	    initFunctionColors();
		addMenuBar();
		initGraphics();
		initSecondaryWindows();
		
		this.setSize(graphicsSize);
		adjustSize();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	private void initFunctionColors() {
		colorStack = new Stack<>();
		colorIdsMap = new HashMap<>();
		
		for(int i = 0; i < MAX_FUNCTIONS; i++) {
			// colors must be pushed in reverse order for stack
			colorStack.push(functionColors[MAX_FUNCTIONS-1-i]);
			colorIdsMap.put(functionColors[i], i);
		}
	}
	
	private void initSecondaryWindows() {
		removeFunctionFrame = new RemoveFunctionFrame(this, "Remove Functions", graphicsDrawer, colorStack);
		listFunctionsFrame = new ListFunctionsFrame(this, "Functions List");
	}
	
	private void addMenuBar() {
		menubar = new JMenuBar();
		
		menuFunc = new JMenu("Function");
	    menuVW = new JMenu("View Window");
	    menuGS = new JMenu("G-Solve");
	    
	    mfuncAdd = new JMenuItem("Add");
	    mfuncRemove = new JMenuItem("Remove");
	    mfuncList = new JMenuItem("List");
	    
	    vwDefault = new JMenuItem("Default");
	    vwSetValues = new JMenuItem("Set Values");
	    vwZoomIn = new JMenuItem("Zoom In");
	    vwZoomOut = new JMenuItem("Zoom Out");
	    
	    gsRoot = new JMenuItem("Root");
	    gsMax = new JMenuItem("Maximum");
	    gsMin = new JMenuItem("Minimum");
	    gsYIntersect = new JMenuItem("Intersection with the Y-Axis");
	    gsIntersect = new JMenuItem("Intersection between two functions");
	    gsYCalc = new JMenuItem("Y-Value");
	    gsXCalc = new JMenuItem("X-Value");
	    gsIntegral = new JMenuItem("Integral");
	    
	    mfuncAdd.addActionListener(this);
	    mfuncRemove.addActionListener(this);
	    mfuncList.addActionListener(this);
	    vwDefault.addActionListener(this);
	    vwSetValues.addActionListener(this);
	    vwZoomIn.addActionListener(this);
	    vwZoomOut.addActionListener(this);
	    gsRoot.addActionListener(this);
	    gsMax.addActionListener(this);
	    gsMin.addActionListener(this);
	    gsYIntersect.addActionListener(this);
	    gsIntersect.addActionListener(this);
	    gsYCalc.addActionListener(this);
	    gsXCalc.addActionListener(this);
	    gsIntegral.addActionListener(this);
	    
	    menuFunc.add(mfuncAdd);
	    menuFunc.add(mfuncRemove);
	    menuFunc.add(mfuncList);
	    menuVW.add(vwDefault);
	    menuVW.add(vwSetValues);
	    menuVW.add(vwZoomIn);
	    menuVW.add(vwZoomOut);    
	    menuGS.add(gsRoot);
	    menuGS.add(gsMax);
	    menuGS.add(gsMin);
	    menuGS.add(gsYIntersect);
	    menuGS.add(gsIntersect);
	    menuGS.add(gsYCalc);
	    menuGS.add(gsXCalc);
	    menuGS.add(gsIntegral);
	    
	    menubar.add(menuFunc);
	    menubar.add(menuVW);
	    menubar.add(menuGS);
	    this.setJMenuBar(menubar);
	}
	
	private void initGraphics() {
		graphicsDrawer = new GraphicsDrawer(graphicsSize);
		graphicsDrawer.setReferentialGraphic();
		this.add(graphicsDrawer);
	}
	
	private void addFunction(String expression) {
		try {
			Color color = colorStack.pop();
			Function function = new Function(graphicsSize, expression, color);
			graphicsDrawer.addFunctionGraphic(function);
			functionCount++;
			SwingFunctions.updateFrameContents(this);
		}
		
		// exception caught if expression has unknown symbols or is an empty string
		catch(IllegalArgumentException e) {
			SwingFunctions.showErrorMessage(this, "Invalid function");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == mfuncAdd) {		//TODO: verify if function is not duplicate
			if(functionCount >= MAX_FUNCTIONS) {
				SwingFunctions.showErrorMessage(this, "Maximum functions limit reached. Remove a function before adding a new one.");
				return;
			}
			
			// gets user input
			String expression = (String)JOptionPane.showInputDialog(this ,"Enter the function's expression:", "Add Function", JOptionPane.PLAIN_MESSAGE);
			
			// expression is null if user pressed Cancel or closed pop-up, in which case does nothing
			if(expression != null)
				this.addFunction(expression);
		}
		
		if(e.getSource() == mfuncRemove) {
			if(functionCount == 0)
				SwingFunctions.showErrorMessage(this, "There are no functions to remove.");
			else
				removeFunctionFrame.showWindow();
		}
		
		if(e.getSource() == mfuncList) {
			if(functionCount == 0)
				SwingFunctions.showErrorMessage(this, "There are no functions to list.");
			else
				listFunctionsFrame.showWindow();
		}
		
		if(e.getSource() == vwDefault) {
			
		}
		
		if(e.getSource() == vwSetValues) {
			
		}
		
		if(e.getSource() == vwZoomIn) {
			
		}
		
		if(e.getSource() == vwZoomOut) {
			
		}
		
		if(e.getSource() == gsRoot) {
			
		}
		
		if(e.getSource() == gsMax) {
			
		}
		
		if(e.getSource() == gsMin) {
			
		}
		
		if(e.getSource() == gsYIntersect) {
			
		}
		
		if(e.getSource() == gsIntersect) {
			
		}
		
		if(e.getSource() == gsYCalc) {
			
		}
		
		if(e.getSource() == gsXCalc) {
			
		}
		
		if(e.getSource() == gsIntegral) {
			
		}
		
	}
	
	// for some reason the frame's size does not match the graphic's size visually, even though the values are the same
	// this function adjust the frame's size with a bias that makes it visually the same as the graphic's
	private void adjustSize() {
		Dimension size = this.getSize();
		size.width += 21;
		size.height += 63;
		this.setSize(size);
	}
	
	public static void main(String[] args) {
		new GraphPlotterFrame();
	}
	
}
