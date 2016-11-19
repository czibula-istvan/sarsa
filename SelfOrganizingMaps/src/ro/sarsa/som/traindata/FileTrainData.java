package ro.sarsa.som.traindata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileTrainData implements SOMTrainData {

	private double[][] data;
	private String[] ids;
	private String fileName;

	public FileTrainData(String fileName) {
		this.fileName = fileName;
		try {
			data = readFromFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int size() {
		return data.length;
	}

	@Override
	public double[] get(int inputIndex) {
		return data[inputIndex];
	}

	@Override
	public int getDataDimension() {
		return data[0].length;
	}

	@Override
	public Object getLabel(int inputIndex) {
		int row = inputIndex % data.length;
		return ids[row];

	}

	private double[][] readFromFile() throws IOException {
		double data[][] = null;

		BufferedReader reader = new BufferedReader(new FileReader(this.fileName));

		int nrInstances = Integer.parseInt(reader.readLine());
		int nrAttr = Integer.parseInt(reader.readLine());

		System.out.println(nrInstances);
		System.out.println(nrAttr);

		ids = new String[nrInstances];
		data = new double[nrInstances][nrAttr];
		// System.out.println("LEN = " + data.length);

		String line = null;
		int contor = 0;
		while ((line = reader.readLine()) != null) {

			// System.out.println("LINE = " + line);

			String[] parts = line.split(",");
			int i;
			ids[contor] = parts[0];// .substring(0, 1);

			// System.out.println("Id[" + contor + "] = " + ids[contor][0]);
			// System.out.println(parts[0]);

			int k = 0;
			for (i = 1; i <= nrAttr; i++) {

				System.out.println("contor = " + contor + " ");
				// System.out.println("ID = " + ids[contor][0]);

				data[contor][k] = Double.parseDouble(parts[i]);
				k++;
				// System.out.print(parts[i]+" ");

			}

			contor++;
		}

		reader.close();

		return data;
	}

}
