package ro.sarsa.neuronal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class ProgramDeTest {

	private static int nrClase;

	public static void main(String[] args) throws NumberFormatException, IOException {
		// citesc din fisier datele
		float[][] inputData = readInputData();
		for (;;) {
			testare(inputData);
		}

	}

	private static void testare(float[][] inputData) {
		int nrIntrari = inputData[0].length - 1;
		int nrIesiri = nrClase;
		int nrHidden = (int) Math.sqrt(nrIesiri * nrIntrari);

		// nrHidden=(int)(1+(2.0*nrIntrari)/3);

		System.out.println("nrInput:" + nrIntrari);
		System.out.println("nrHidden:" + nrHidden);
		System.out.println("nrOutput:" + nrIesiri);

		// creez un NeuronalNetwork
		NeuronalNetwork netw = NetworkCreator.createRandom(nrIntrari, nrHidden, nrIesiri, 0.5f);
		// creez antrenoruil de retea
		float learningRate = 0.3f;
		NetworkTrainer trainer = new NetworkTrainer(netw, learningRate);

		// int nrDateDeAntrenare = (inputData.length * 2) / 3;
		int nrDateDeAntrenare = (inputData.length * 3) / 4;

		// construiesc datele de antrenare
		float[][] in = new float[nrDateDeAntrenare][];
		float[][] expectedOut = new float[nrDateDeAntrenare][];
		for (int i = 0; i < nrDateDeAntrenare; i++) {
			in[i] = new float[inputData[i].length - 1];
			for (int j = 0; j < inputData[i].length - 1; j++) {
				in[i][j] = inputData[i][j];
			}
			expectedOut[i] = new float[6];
			expectedOut[i][(int) inputData[i][inputData[i].length - 1]] = 1f;
		}

		// fac antrenarea
		int nrMaxIterations = 5000;
		float errThreshold = 0.00001f;
		trainer.train(in, expectedOut, nrMaxIterations, errThreshold);

		// fac predictie.. folosesc tot datele de antrenare
		int nrCorectPpedictions = 0;
		for (int i = 0; i < nrDateDeAntrenare; i++) {
			float[] inValues = in[i];
			float[] predict = netw.predict(inValues);
			// System.out.println(predict[0] + " " + predict[1]);
			if (expectedOut[i][(int) predict[0]] == 1f) {
				nrCorectPpedictions++;
			}
		}
		System.out.println("Precizie:" + (((float) nrCorectPpedictions / (float) nrDateDeAntrenare) * 100d));
		System.out.println("nrCorectPpedictions:" + nrCorectPpedictions + " nrDateDeTest:" + nrDateDeAntrenare);

		// fac predictie.. folosesc tot datele care nu au fost folosite la
		// antrenare
		nrCorectPpedictions = 0;
		for (int i = nrDateDeAntrenare; i < inputData.length; i++) {
			float[] inValues = new float[inputData[i].length - 1];
			for (int j = 0; j < inputData[i].length - 1; j++) {
				inValues[j] = inputData[i][j];
			}
			float[] predict = netw.predict(inValues);

			// System.out.println(predict[0] + " " + predict[1]);
			if (inputData[i][inputData[i].length - 1] == predict[0]) {
				nrCorectPpedictions++;
			}
		}
		System.out.println("Precizie:"
				+ (((double) nrCorectPpedictions / (double) (inputData.length - nrDateDeAntrenare)) * 100d));
		System.out.println("nrCorectPpedictions:" + nrCorectPpedictions + " nrDateDeTest:"
				+ (inputData.length - nrDateDeAntrenare));
	}

	public static float[][] readInputData() throws NumberFormatException, IOException {
		File f = new File("res/CancerData.txt");
		FileReader fr = new FileReader(f);
		BufferedReader bufR = new BufferedReader(fr);
		int nrInreg = Integer.parseInt(bufR.readLine());
		int nrAtr = Integer.parseInt(bufR.readLine());
		float[][] attrs = new float[nrInreg][];

		float[] max = new float[nrAtr + 1];
		float[] min = new float[nrAtr + 1];
		for (int j = 0; j <= nrAtr; j++) {
			max[j] = Float.MIN_VALUE;
			min[j] = Float.MAX_VALUE;
		}
		for (int i = 0; i < nrInreg; i++) {
			attrs[i] = new float[nrAtr + 1];
			String line = bufR.readLine();
			StringTokenizer st = new StringTokenizer(line, " ");
			st.nextToken();
			for (int j = 0; j <= nrAtr; j++) {
				attrs[i][j] = Float.parseFloat(st.nextToken());
				if (attrs[i][j] > max[j]) {
					max[j] = attrs[i][j];
				}
				if (attrs[i][j] < min[j]) {
					min[j] = attrs[i][j];
				}
			}

		}
		nrClase = Integer.parseInt(bufR.readLine());
		bufR.close();

		// normalizam datele
		for (int j = 0; j < nrAtr; j++) {
			float aux = max[j] - min[j];
			for (int i = 0; i < nrInreg; i++) {
				attrs[i][j] = (attrs[i][j] - min[j]) / aux;
			}
		}
		return attrs;
	}
}
