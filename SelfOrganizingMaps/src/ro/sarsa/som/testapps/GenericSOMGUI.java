package ro.sarsa.som.testapps;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import ro.sarsa.som.SOM;
import ro.sarsa.som.SOMTrainingListeners;
import ro.sarsa.som.Trainer2Phase;
import ro.sarsa.som.clustering.SOMClusterGUI;
import ro.sarsa.som.clustering.SOMClusteringHierarhic;
import ro.sarsa.som.topology.SOMTopology;
import ro.sarsa.som.topology.TorusSOMTopology;
import ro.sarsa.som.traindata.RandomColorSetTrainingData;
import ro.sarsa.som.traindata.SOMTrainData;
import ro.sarsa.som.umatrix.UMatrixPanel;

public class GenericSOMGUI extends JFrame {
	private JButton jbStart = new JButton("Train");
	private JButton jbStartClustering = new JButton("Clustering");
	private JButton jbInit = new JButton("Init SOM");
	private JCheckBox jCB2Phase = new JCheckBox("2 phase training");
	private JCheckBox jCBUmatrix = new JCheckBox("View UMatrix");
	private JCheckBox jCBViewClusters = new JCheckBox("View clusters");
	private JTextField jTFNrClusteri = new JTextField("0.3", 5);

	private JTextField jtFNrIteration = new JTextField("3000", 5);
	private JTextField jTFLearningR = new JTextField("0.7", 5);

	private JTextField jTFNrRows = new JTextField("16", 5);
	private JTextField jTFNrCols = new JTextField("10", 5);
	private JPanel jpMain;
	private JPanel jpView;
	// private GenericSOMNetworkView somView;
	private JLabel jLStatus = new JLabel();
	private SOMTrainData trData;
	private INetworkDraw drawNetw;
	private SOMTopology topo;
	private SOM som;
	private GenericSOMNetworkView somView;
	private UMatrixPanel umP;
	private SOMClusterGUI cluGUI;

	public GenericSOMGUI(SOMTrainData trData, INetworkDraw drawNetw) {
		this.trData = trData;
		this.drawNetw = drawNetw;
		initGUI();
		addListeners();
	}

	private void addListeners() {
		jbStartClustering.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				startClustering();
			}

		});
		jbInit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				initTopoAndGUI();
			}

		});
		jbStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				new Thread(new Runnable() {

					@Override
					public void run() {
						if (somView == null) {
							// nu s-a ininitializat inca
							initTopoAndGUI();
						}
						SOMTrainingListeners lst = new SOMTrainingListeners(somView);
						lst.addListener(umP);
						lst.addListener(cluGUI);
						jLStatus.setText("Start Training");
						long time = System.currentTimeMillis();
						Trainer2Phase.train2Phase(som, Integer.parseInt(jtFNrIteration.getText()), trData,
								Double.parseDouble(jTFLearningR.getText()), topo.getMaxRadius() / 3, lst);
						jLStatus.setText("Time:" + (System.currentTimeMillis() - time));
					}

				}).start();
			}
		});
	}

	protected void startClustering() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				SOMClusteringHierarhic clu = new SOMClusteringHierarhic(som, topo, trData,
						Integer.parseInt(jTFNrClusteri.getText()), cluGUI);
				clu.neuronCluster();
			}
		}).start();

	}

	private void initTopoAndGUI() {
		jpView.removeAll();
		topo = createTopo();
		som = new SOM(trData.getDataDimension(), topo);
		somView = new GenericSOMNetworkView(som, drawNetw);
		JSplitPane sp = null;
		JSplitPane sp2 = null;
		if (jCBUmatrix.isSelected()) {
			umP = new UMatrixPanel(som, trData);
			sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
			sp.add(somView);
			if (jCBViewClusters.isSelected()) {

				cluGUI = new SOMClusterGUI(som, trData, null);
				sp2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
				sp2.add(umP);
				sp2.add(cluGUI);
				sp.add(sp2);
			} else {
				sp.add(umP);
			}
			jpView.add(sp, BorderLayout.CENTER);

		} else {
			umP = null;
			cluGUI = null;
			jpView.add(somView, BorderLayout.CENTER);
			getContentPane().validate();
			getContentPane().repaint();
		}
		getContentPane().validate();
		getContentPane().repaint();
		if (sp != null) {
			sp.setDividerLocation(0.5d);
		}
		if (sp2 != null) {
			sp2.setDividerLocation(0.5d);
		}
	}

	private SOMTopology createTopo() {
		// Latice2DSOMTopology topo = new Latice2DSOMTopology(Integer
		// .parseInt(jTFNrRows.getText()), Integer.parseInt(jTFNrCols
		// .getText()), trData.getDataDimension());
		TorusSOMTopology topo = new TorusSOMTopology(Integer.parseInt(jTFNrRows.getText()),
				Integer.parseInt(jTFNrCols.getText()), trData.getDataDimension());
		topo.initRandom(0, 1);
		return topo;
	}

	private void initGUI() {
		JPanel jPTrainCFG = new JPanel();
		BoxLayout bl = new BoxLayout(jPTrainCFG, BoxLayout.Y_AXIS);
		jPTrainCFG.setLayout(bl);
		jPTrainCFG.add(jCB2Phase);
		JPanel aux = new JPanel();
		aux.add(new JLabel("Nr Iteratii:"));
		aux.add(jtFNrIteration);
		jPTrainCFG.add(aux);
		aux = new JPanel();
		aux.add(new JLabel("Learning Rate:"));
		aux.add(jTFLearningR);
		jPTrainCFG.add(aux);
		aux = new JPanel();
		aux.add(new JLabel("Netw Rows:"));
		aux.add(jTFNrRows);
		jPTrainCFG.add(aux);
		aux = new JPanel();
		aux.add(new JLabel("Netw Cols:"));
		aux.add(jTFNrCols);
		jPTrainCFG.add(aux);

		jPTrainCFG.add(jbInit);
		jPTrainCFG.add(jbStart);

		jPTrainCFG.add(jCBUmatrix);

		jPTrainCFG.add(jCBViewClusters);
		aux = new JPanel();
		aux.add(new JLabel("Numar Clusteri:"));
		aux.add(jTFNrClusteri);
		jPTrainCFG.add(aux);
		jPTrainCFG.add(jbStartClustering);

		jpView = new JPanel();
		jpView.setLayout(new BorderLayout());
		jpMain = new JPanel(new BorderLayout());
		jpMain.add(jPTrainCFG, BorderLayout.EAST);
		jpMain.add(jLStatus, BorderLayout.SOUTH);
		jpMain.add(jpView, BorderLayout.CENTER);
		getContentPane().add(jpMain);
		setSize(600, 700);
	}

	public static void main(String[] args) throws IOException {
		// GenericSOMGUI gui = new GenericSOMGUI(new RandomTrainingData(2, -1,
		// 1),
		// new Draw2DNetworkNet());
		GenericSOMGUI gui = new GenericSOMGUI(new RandomColorSetTrainingData(),
				new DrawColorSOMNetw(new RandomColorSetTrainingData()));
		// GenericSOMGUI gui = new GenericSOMGUI(new
		// RandomColorSetTrainingData(),
		// new Draw2DNetworkNet());

		// GeneralInfoCollector infCol = GeneralInfoCollector.testFromClass(
		// "ro/sarsa/somrefactoringex", new String[] {
		// "ro/sarsa/somrefactoringex/Class_A.class",
		// "ro/sarsa/somrefactoringex/Class_B.class" });
		// SoftwareTrainingData td = new SoftwareTrainingData(infCol,
		// new DistanceRefactoring(infCol));
		// GenericSOMGUI gui = new GenericSOMGUI(td, new Draw2DNetworkNet());

		gui.setVisible(true);
	}
}
