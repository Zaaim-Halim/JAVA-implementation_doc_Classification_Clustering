package com.halim.clustring;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.log4j.Logger;

import com.halim.nlp.classification.Distance;

/**
 * @author ZAAIM HALIM
 *
 */
public class CentreMobile {
	static Logger log = Logger.getLogger(CentreMobile.class.getName());

	public CentreMobile() {
		super();
		// TODO Auto-generated constructor stub
	}

	             // class , docname, word . tfidf
	public Map<String, Map<String, Map<String, Float>>> clustring(Map<String, Map<String, Float>> tfIdfMap, int k,
			int numIter) {

		Map<String, Map<String, Map<String, Float>>> cluster = new HashMap<String, Map<String, Map<String, Float>>>();
		// initialization ##################################################
		Random random = new Random();
		for (int i = 1; i <= k; i++) {
			cluster.put("c" + i, new HashMap<String, Map<String, Float>>());
		}
		for (Entry<String, Map<String, Float>> entry : tfIdfMap.entrySet()) {
			int rd = random.nextInt(k) + 1;
			// Map<String,Map<String,Integer>> doc = new HashMap<>();

			cluster.get("c" + rd).put(entry.getKey(), entry.getValue());
		}
		log.info("####################### initial clusters #########################");
		for (Entry<String, Map<String, Map<String, Float>>> entry : cluster.entrySet()) {
			log.info(entry.getKey());
			log.info(entry.getValue().keySet().toString());
			log.info(entry.getKey() + " intial size : " + entry.getValue().size());
		}

		log.info("###################################################################");
		// ##################################################################
		log.info("determining the new clusters ......");
		while (numIter >= 0) {
			Map<String, Map<String, Map<String, Float>>> newCluster = new HashMap<String, Map<String, Map<String, Float>>>();
			for (int i = 1; i <= k; i++) {
				newCluster.put("c" + i, new HashMap<String, Map<String, Float>>());
			}

			Map<String, List<Double>> centers = new HashMap<String, List<Double>>();
			for (Entry<String, Map<String, Map<String, Float>>> entry : cluster.entrySet()) {

				centers.put(entry.getKey(), gravityCenter(entry.getValue()));
			}

			for (Entry<String, Map<String, Float>> entry : tfIdfMap.entrySet()) {
				String Klass = clossestClass(centers, entry.getValue());
				newCluster.get(Klass).put(entry.getKey(), entry.getValue());
			}

			cluster = newCluster;
			numIter--;
		}

		return cluster;
	}

	private List<Double> gravityCenter(Map<String, Map<String, Float>> klass) {
		int classSize = klass.size();
		int ss = biggestMap(klass);
		List<Double> w = new ArrayList<Double>();
		for (int i = 0; i < ss; i++) {
			w.add(0D);
		}
		for (Entry<String, Map<String, Float>> entry : klass.entrySet()) {
			List<Float> doc = new ArrayList<Float>(entry.getValue().values());
			for (int i = 0; i < doc.size(); i++) {
				w.set(i, w.get(i) + doc.get(i));
			}
		}

		// normilze // classSize

		for (int i = 0; i < w.size(); i++) {
			w.set(i, w.get(i) / classSize);
		}

		return w;
	}

	private int biggestMap(Map<String, Map<String, Float>> klass) {
		int max = 0;
		for (Entry<String, Map<String, Float>> entry : klass.entrySet()) {
			if (entry.getValue().size() > max) {
				max = entry.getValue().size();
			}
		}
		return max;
	}

	private String clossestClass(Map<String, List<Double>> centers, Map<String, Float> doc) {

		Float misDistance = 1000000000F;
		String klass = "";
		for (Entry<String, List<Double>> entry : centers.entrySet()) {
			Float dist = Distance.euclidienneDistance(entry.getValue(), doc);
			if (dist < misDistance) {
				misDistance = dist;
				klass = entry.getKey();
			}
		}

		return klass;
	}

}
