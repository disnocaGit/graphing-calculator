package graphplotter.graphics;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JComponent;

import functionComponents.Function;

@SuppressWarnings("serial")
public class GraphicsDrawer extends JComponent {
	
	private Dimension size;
	BufferedImage referentialGraphic;
	ArrayList<FunctionGraphic> functionGraphics;
	

	public GraphicsDrawer(Dimension size) {
		this.size = size;
		functionGraphics = new ArrayList<>();
	}
	
	public void setReferentialGraphic() {
		referentialGraphic = new ReferentialGraphic(size);
	}
	
	public void addFunctionGraphic(Function function) {
		FunctionGraphic functionGraphic = new FunctionGraphic(size, function);
		functionGraphics.add(functionGraphic);
	}
	
	@Override
    public void paintComponent(Graphics g){
		// referential is always the bottom layer
		g.drawImage(referentialGraphic, 0, 0, size.width, size.height, null);
		
        for(FunctionGraphic layer : functionGraphics)
            g.drawImage(layer, 0, 0, size.width, size.height, null);
    }
	
	public String getFunctionExpression(int pos) {
		return functionGraphics.get(pos).getExpression();
	}

	public Color getFunctionColor(int pos) {
		return functionGraphics.get(pos).getColor();
	}
	
}
