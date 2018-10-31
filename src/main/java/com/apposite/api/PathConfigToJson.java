package com.apposite.api;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.google.gson.Gson;

public class PathConfigToJson {
	public PathConfigToJson() {
	}

//	{ PathName: {
//		"Port 1 Access - inbound":{
//			condition: value,
//			...
//		},
//		"Port 1 Access - outbound":{
//			condition: value,
//			...
//		},
//		"Port 2 Access - inbound":{
//			condition: value,
//			...
//		},
//		"Port 2 Access - outbound":{
//			condition: value,
//			...
//		},
//		"WAN Conditions - Port 1 to Port 2": {
//			condition: value,
//			...
//		},
//		"WAN Conditions - Port 2 to Port 1": {
//			condition: values
//			...
//		}
//	} }

	private static String getPathName(String title) {
		return title.split(": ")[1];
	}

	private static String getAccessKey(String accessString) {
		String title = accessString.split("\\R")[0];
		title = title.split("WAN Access: ")[1];
		String[] parts = title.split(",");
		String port = parts[0];
		String direction = parts[1];
		if (direction.indexOf("in") > -1) {
			direction = "inbound";
		} else {
			direction = "outbound";
		}
		return port + " Access - " + direction;
	}

	private static String getWANKey(String wanString) {
		String title = wanString.split("\\R")[0];
		title = title.substring(0, title.length() - 1);
		title = title.replace(": ", " - ");
		return title;
	}

	private static HashMap<String, String> getAccessMap(String accessString) {
		HashMap<String, String> accessMap = new LinkedHashMap<String, String>();

		String[] lines = accessString.split("\\R");
		if (lines[0].indexOf("not enabled") > -1) {
			accessMap.put("Enabled", "False");
			return accessMap;
		} else {
			accessMap.put("Enabled", "True");
		}
		String linkType = lines[1].trim();
		accessMap.put("Link Type", linkType);
		for (int idx = 2; idx < lines.length; idx++) {
			String line = lines[idx].trim();
			String[] parts = line.split(": ");
			accessMap.put(parts[0], parts[1]);
		}
		return accessMap;
	}

	// exception since the Gilbert-Elliot condition is split between 3 lines
//	Loss: Gilbert-Elliott:
//		  Good state: Loss probability: 0%, Change probability: 0%
//		  Bad state: Loss probability: 0%, Change probability: 0%
	// gilbert elliot states are preceded by two spaces, not tabs
	private static String handleGilbertElliot(String[] geStringList) {
		String value = "Gilbert-Elliott, " + geStringList[1].trim() + ", " + geStringList[2].trim();
		return value;
	}

	private static HashMap<String, String> getWANMap(String wanString) {
		HashMap<String, String> wanMap = new LinkedHashMap<String, String>();

		String[] lines = wanString.split("\\R");
		for (int idx = 1; idx < lines.length; idx++) {
			String line = lines[idx].trim();
			if (line.indexOf("Gilbert-Elliott") > -1) {
				String value = handleGilbertElliot(Arrays.copyOfRange(lines, idx, idx + 3));
				wanMap.put("Loss", value);
				idx += 2;
				continue;
			}
			String[] parts = line.split(": ");
			wanMap.put(parts[0], parts[1]);
		}
		return wanMap;
	}

	public static String getJsonFormat(String output) {
		String regex = "(\\R\\R\\R)+";
		String[] sections = output.split(regex);

		String pathName = getPathName(sections[0]);
		String wanAccessAout = sections[1];
		String wanAccessAin = sections[2];
		String wanAccessBout = sections[3];
		String wanAccessBin = sections[4];
		String wanAtoB = sections[5];
		String wanBtoA = sections[6];

		LinkedHashMap<String, HashMap<String, String>> pathConfig = new LinkedHashMap<String, HashMap<String, String>>();
		pathConfig.put(getAccessKey(wanAccessAout), getAccessMap(wanAccessAout));
		pathConfig.put(getAccessKey(wanAccessAin), getAccessMap(wanAccessAin));
		pathConfig.put(getAccessKey(wanAccessBout), getAccessMap(wanAccessBout));
		pathConfig.put(getAccessKey(wanAccessBin), getAccessMap(wanAccessBin));
		pathConfig.put(getWANKey(wanAtoB), getWANMap(wanAtoB));
		pathConfig.put(getWANKey(wanBtoA), getWANMap(wanBtoA));

		Gson gson = new Gson();
		HashMap<String, HashMap> json = new HashMap<String, HashMap>();
		json.put(pathName, pathConfig);

		return gson.toJson(json);
	}
}
