package ro.sarsa.assocrules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import ro.sarsa.assocrules.attributetypes.AttributeType;
import ro.sarsa.assocrules.attributetypes.AttributeTypeFactory;
import ro.sarsa.assocrules.generators.ByJoinAdaptivVers2Generator;
import ro.sarsa.assocrules.generators.ByJoinAdaptiveRuleGenerator;
import ro.sarsa.assocrules.generators.ByJoinRuleGenerator;
import ro.sarsa.assocrules.relations.DoubleDoubleGreater;
import ro.sarsa.assocrules.relations.DoubleDoubleLessOrEqual;

public class Main {

	static int naMic, naMare; // nr atribute mic, nr atribute mare
	static EntitySet esM;

	// Daca un atribut are vaoare nula (ex 0 pentru Double) pp ca este valoare
	// lipsa

	private void makeEntity(List<String> an, List<String> at, String line, Entity e, int na) throws Exception {
		// construieste o entitate

		StringTokenizer st = new StringTokenizer(line, " ");
		// StringTokenizer st = new StringTokenizer(line, ",");

		// System.out.println("line "+line+", na="+na);
		for (int i = 0; i < na/* an.size() */; i++) {
			String typeName = (String) at.get(i);
			AttributeType atrType = AttributeTypeFactory.getForName(typeName);
			Attribute a = new Attribute(an.get(i), atrType, atrType.createValue(st.nextToken()), i);
			e.addAttr(a);

		}
	}

	public EntitySet readData(String f, int na, int maxNrEntities) throws Exception {
		// construieste setul de entitati din fisier
		FileInputStream istr = new FileInputStream(new File(f));
		InputStreamReader irdr = new InputStreamReader(istr);
		BufferedReader brdr = new BufferedReader(irdr);
		List<String> an = new ArrayList<String>(); // sirul numelor atributelor
		List<String> at = new ArrayList<String>(); // sirul tipurilor
													// atributelor

		EntitySet es = new EntitySet();

		String line;
		line = brdr.readLine();
		StringTokenizer st = new StringTokenizer(line, "\t ");

		while (st.hasMoreTokens()) {
			String t = st.nextToken();
			an.add(t);
			t = st.nextToken();
			at.add(t);
		}
		int k = 0;
		while ((line = brdr.readLine()) != null && k < maxNrEntities) {
			k = k + 1;
			// System.out.println(k);
			if (line.trim().length() == 0)
				continue;
			Entity e = new Entity();
			makeEntity(an, at, line, e, na);
			// System.out.println(k);
			es.addEntity(e);
		}

		istr.close();

		// scalare (valori numerice)
		es = es.createNormalized();
		return es;
	}

	public static void main(String args[]) throws Exception {
		// args[0] - fisierul cu entitatile
		// args[1] - suport minim
		// args[2] - confidenta minima
		// args[3] - maximal daca vreau reguli maximale
		// args[4] - 2 daca vreau reguli binare
		// args[5] - numar mic attribute
		// args[6] - numar mare attribute

		String fisEnt = args[0];

		System.out.println(fisEnt);

		double minSup = new Double(args[1]).doubleValue();

		double minConf = new Double(args[2]).doubleValue();

		String maximal = args[3];

		int binar = new Integer(args[4]).intValue();

		naMic = new Integer(args[5]).intValue();

		naMare = new Integer(args[6]).intValue();

		naMare = 37;
		naMic = 30;
		for (int nrMaxEntities = 644; nrMaxEntities > 10; nrMaxEntities -= 50) {
			Main m = new Main(0, fisEnt, minSup, minConf, binar, maximal, nrMaxEntities);
		}

		// int maxEntities = 344;
		// naMare = 37;
		// int k = 0;
		// for (naMic = 15; naMic < 16/* naMare */; naMic += 5) {
		// System.out.println();
		// System.out.println("Numar mic=" + naMic);
		// Main m = new Main(k, fisEnt, minSup, minConf, binar, maximal,
		// maxEntities);
		// k++;
		// }
	}

	public Main(int k, String f, double minSup, double minConf, int binar, String maximal, int maxNrEntities)
			throws Exception {

		// RelationSet.clear();
		RelationSet.addRelation(DoubleDoubleLessOrEqual.sing);
		RelationSet.addRelation(DoubleDoubleGreater.sing);

		System.out.println("Start pentru Numar entitati:" + maxNrEntities);
		EntitySet es = readData(f, naMic, maxNrEntities);

		if (k == 0) {
			esM = readData(f, naMare, maxNrEntities);

			// reguli prin join pe setul mare - from skretch
			double msijoin = System.currentTimeMillis();
			AssociationRuleSet.nrJoins = 0;
			ByJoinRuleGenerator ruleGen = new ByJoinRuleGenerator(esM, binar, maximal);
			AssociationRuleSet arsjM = ruleGen.genAssociationRulesByJoin(minSup, minConf);
			double msfjoin = System.currentTimeMillis();

			System.out.print("Reguli SCRATCH-->");
			arsjM.tiparire();
			double tsjoin = msfjoin - msijoin;
			System.out.println("Time from scratch cu join= " + tsjoin + "ms");
			System.out.println("Numar joinuri= " + AssociationRuleSet.nrJoins);
			System.out.println();

			// reguli from scratch cu varianta adaptiva
			double msiaS = System.currentTimeMillis();
			ByJoinAdaptivVers2Generator ruleGenAdaptScratch = new ByJoinAdaptivVers2Generator(new AssociationRuleSet(),
					esM, 0, naMare);
			AssociationRuleSet arsjSA = ruleGenAdaptScratch.adaptivVers2(minConf, minSup);
			double msfaS = System.currentTimeMillis();
			arsjSA.tiparire();
			System.out.println("Time from sratch adaptive = " + (msfaS - msiaS) + "ms");
			System.out.println();

			// scrieInFis(arsjM, "RezultatScratch.txt");
		}

		// reguli prin join adaptiv pe setul mic
		AssociationRuleSet.nrJoins = 0;
		ByJoinRuleGenerator arg = new ByJoinRuleGenerator(es, binar, "nemaximal");
		// reguli prin join pe setul mic
		AssociationRuleSet arsj = arg.genAssociationRulesByJoin(minSup, minConf);
		scrieInFis(arsj, "RezultatMic.txt");
		System.out.println("Numar joinuri reguli mici= " + AssociationRuleSet.nrJoins);
		System.out.println("Nr reguli mic = " + arsj.size());

		System.out.println("Reguli ADAPTIV-->");
		double msia = System.currentTimeMillis();
		ByJoinAdaptivVers2Generator ruleGenAdapt1 = new ByJoinAdaptivVers2Generator(arsj, esM, naMic, naMare);
		AssociationRuleSet arsjA = ruleGenAdapt1.adaptivVers2(minConf, minSup);
		double msfa = System.currentTimeMillis();
		arsjA.tiparire();
		System.out.println("Time adaptive = " + (msfa - msia) + "ms");
		System.out.println();

		System.out.print("Reguli ADAPTIV GABI-->");
		double msiaG = System.currentTimeMillis();
		ByJoinAdaptiveRuleGenerator ruleGenAdapt1G = new ByJoinAdaptiveRuleGenerator(esM, binar, maximal);
		AssociationRuleSet arsjAG = ruleGenAdapt1G.genAssociationRulesByJoinAdaptiv(arsj, minSup, minConf, naMic,
				naMare);
		double msfaG = System.currentTimeMillis();
		arsjAG.tiparire();
		System.out.println("Time adaptive GABI= " + (msfaG - msiaG) + "ms");
		System.out.println();

		// scrieInFis(arsjA, "RezultatAdaptiv.txt");
		System.out.println("Finish pentru Numar entitati:" + maxNrEntities);
	}

	private void scrieInFis(AssociationRuleSet arsjM, String numeFis) throws FileNotFoundException, IOException {

		// FileOutputStream fos5 = new FileOutputStream(numeFis);
		// PrintStream ps5 = new PrintStream(fos5);
		// ps5.println(arsjM.toString1());
		// fos5.close();
	}

}
