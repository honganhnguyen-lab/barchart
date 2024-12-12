import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Hashtable;

@SuppressWarnings("serial")
class BarChartFrame extends Frame
{
	Hashtable<String, Color> colorMap = new Hashtable<String, Color>();

	protected Vector<Integer>	data;
	protected Vector<String>	labels;
	protected Vector<Color>		colors;

	Choice 	colorSelect;
	TextField	labelSelect;
	TextField	dataSelect;

	BarChart chart;

	class BarChartFrameControl extends WindowAdapter implements ActionListener 
	{
		// SER515 #2: The line which adds an element to the colors vector
		// assumes the selected item is available in the colorMap. Is this always true?
		// -> selectedColor always not null but chosenColor could be nullable, so we need to check it
		public void actionPerformed(ActionEvent e)
		{
			if (e.getSource() instanceof Button)
			{
				String selectedColor = colorSelect.getSelectedItem();
				Color chosenColor = colorMap.get(selectedColor);

				if (chosenColor == null) {
					System.err.println("Warning: no such color, use default");
					chosenColor = Color.BLACK;
				}
				labels.addElement(labelSelect.getText());
				data.addElement(new Integer(dataSelect.getText()));
				colors.addElement(chosenColor);

				chart.setData(data);
				chart.setColors(colors);
				chart.setLabels(labels);
				chart.repaint();
			}
			else if (e.getSource() instanceof Menu)
			{
				BarChartFrame.this.dispose();
				System.exit(0);		// only option at this time
			}
		}
	}

	public void initData(String fname)
	{
		data = new Vector<Integer>();
		labels = new Vector<String>();
		colors = new Vector<Color>();

		Map<String, Color> colorMap = new HashMap<>();
		colorMap.put("red", Color.red);
		colorMap.put("green", Color.green);
		colorMap.put("blue", Color.blue);
		colorMap.put("magenta", Color.magenta);
		colorMap.put("gray", Color.gray);

		// SER515 #3: There are multiple problems here, ranging from input data validation
		// to data in the file matching what is in the color map to how exceptions are
		// handled. Improve the code to handle these 3 problems.
		try (BufferedReader reader = new BufferedReader(new FileReader(fname))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");

				if (parts.length != 3) {
					System.err.println("Invalid data format: " + line);
					continue;
				}

				try {
					int number = Integer.parseInt(parts[0].trim());
					String label = parts[1].trim();
					String colorName = parts[2].trim().toLowerCase();

					Color color = colorMap.get(colorName);
					if (color == null) {
						System.err.println("Unknown color: " + colorName);
						continue;
					}

					data.add(number);
					labels.add(label);
					colors.add(color);
				} catch (NumberFormatException e) {
					System.err.println("Invalid number format in line: " + line);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + fname);
			e.printStackTrace();
		}
	}

	public BarChartFrame(String fname) {
		BarChartFrameControl control = new BarChartFrameControl();

		initData(fname);

		setSize(350,350);
		setLayout(new BorderLayout());

		MenuBar mb = new MenuBar();
		Menu file = new Menu("File");
		file.addActionListener(control);
		file.add("Exit");
		mb.add(file);
		setMenuBar(mb);

		chart = new BarChart();

		chart.setData(data);
		chart.setLabels(labels);
		chart.setColors(colors);

		Panel components = new Panel();
		components.setSize(350,50);
		components.setLayout(new FlowLayout());

		colorSelect = new Choice();
		colorSelect.add ("red");
		colorSelect.add("green");
		colorSelect.add("blue");
		colorSelect.add("magenta");
		colorSelect.add("gray");
		colorSelect.add("orange");
		components.add(colorSelect);
		labelSelect = new TextField("label", 10);
		components.add(labelSelect);
		dataSelect = new TextField("data", 5);
		components.add(dataSelect);

		Button button = new Button("Add Data");
		button.addActionListener(control);
		components.add(button);

		setBackground(Color.lightGray);
		add(components, "South");
		add(chart, "North");
		chart.repaint();
		setVisible(true);
	}

}
