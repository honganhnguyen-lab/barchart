import java.awt.*;
import java.util.Vector;

@SuppressWarnings("serial")
class BarChart extends Panel
{			   
	private int	barWidth = 20;

	private Vector<Integer>	data;
	private Vector<String>	dataLabels;
	private Vector<Color>	dataColors;

       /**
       SER515 #1: Design By Contract - what is the assumed precondition
       of the Vectors in use in the for loop?
		-> all value need to have data, can not be nullable
		-> all vectors need to be in same size with each other
		Add a check to avoid any errors.
       Design an approach to gracefully handle any errors
       */
	public void paint(Graphics g)
	{
		if (data == null || dataLabels == null || dataColors == null) {
			System.err.println("Error: Data Vectors are not initialized");
			return;
		}

		if (data.size() != dataLabels.size() || data.size() != dataColors.size()) {
			System.err.println("Error: One or more data Vectors are empty");
			return;
		}

		setSize(200,250);
		Image duke = Toolkit.getDefaultToolkit().getImage("duke2.gif");
		g.drawImage(duke, 80, 10, this);

		for (int i = 0; i < data.size(); i++)
		{				  
			int yposition = 100+i*barWidth;

			Color color = dataColors.elementAt(i);
			Integer value = data.elementAt(i);
			String label = dataLabels.elementAt(i);

			if(color != null) {
				g.setColor(dataColors.elementAt(i));
				int barLength = (value != null) ? value.intValue() : 0;
				g.fillOval(100, yposition, barLength, barWidth);
			}

			if (label != null) {
				g.setColor(Color.black);
				g.drawString(label, 20, yposition + 10);
			}
		}
	}

	public void setData(Vector<Integer> dataValues)
	{
		data = dataValues != null ? dataValues : new Vector<>();
	}

	public void setLabels(Vector<String> labels)
	{
		dataLabels = labels != null ? labels : new Vector<>();
	}

	public void setColors(Vector<Color> colors)
	{
		dataColors = colors != null ? colors : new Vector<>();
	}
}
