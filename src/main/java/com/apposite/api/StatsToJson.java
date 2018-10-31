package com.apposite.api;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gson.Gson;

public class StatsToJson {
	public StatsToJson() {
	}

//	{
//		"pathName":{
//			"timeIdx":{
//				"aToB":{
//					"type":"value",
//					...
//				}
//				"bToA":{
//					"type":"value",
//					...
//				}
//			},
//			...
//		},
//		...
//	}

	private static class Direction {
		HashMap<String, String> aToB = new HashMap<String, String>();
		HashMap<String, String> bToA = new HashMap<String, String>();

		public void setAtoB(String type, String value) {
			aToB.put(type, value);
		}

		public void setBtoA(String type, String value) {
			bToA.put(type, value);
		}
	}

	private static String cleanString(String s) {
		return s.replaceAll("\r", "").trim();
	}

	private static String formatType(String s) {
		String[] typeList = new String[] { "tx_rate", "tx_bytes", "tx_frames", "drops" };
		for (String type : typeList) {
			if (s.indexOf(type) > -1) {
				return type;
			}
		}
		return s;
	}

	public static String getJsonFormat(String output) throws NetropyException {
		String[] lines = output.split("\n");

		String[] pathNames = lines[0].split(",");
		String[] directions = lines[1].split(",");
		String[] types = lines[2].split(",");

		HashMap<String, LinkedHashMap<String, Direction>> stats = new HashMap<String, LinkedHashMap<String, Direction>>();
		for (int colIdx = 0; colIdx < pathNames.length; colIdx++) {
			String pathName = cleanString(pathNames[colIdx]);
			if (!pathName.isEmpty()) {
				// add in path if it doesn't exist
				if (!stats.containsKey(pathName))
					stats.put(pathName, new LinkedHashMap<String, Direction>());
				// add in time, direction if it doesn't exist
				for (int rowIdx = 3; rowIdx < lines.length; rowIdx++) {
					String[] splitRow = lines[rowIdx].split(",");
					// catch for missing data, occurs if reset and get are called too quickly after
					// each other
					if (splitRow[0].indexOf("ERROR") > -1)
						throw new NetropyException("No statistics data");
					// assumed to look at first column for time
					String timeIdx = splitRow[0];
					if (!stats.get(pathName).containsKey(timeIdx)) {
						stats.get(pathName).put(timeIdx, new Direction());
					}
					String direction = cleanString(directions[colIdx]);
					String type = cleanString(types[colIdx]);
					type = formatType(type);
					String value = cleanString(splitRow[colIdx]);
					Direction dir = stats.get(pathName).get(timeIdx);
					// assumes only single engine
					if (direction.indexOf("Port 1") < direction.indexOf("Port 2")) {
						dir.setAtoB(type, value);
					} else if (direction.indexOf("Port 2") < direction.indexOf("Port 1")) {
						dir.setBtoA(type, value);
					}
				}
			}
		}
		Gson gson = new Gson();
		return gson.toJson(stats);
	}
}
