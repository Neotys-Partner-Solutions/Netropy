package com.neotys.apposite.codec;

import com.neotys.apposite.data.NetropyData;
import javafx.scene.control.ListCell;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.boot.json.JsonParserFactory;

import java.util.*;

public class NetroyMonitoringReader {

/*
{
	"First one": {
		"Port 1 Access - outbound": {
			"Enabled": "True",
			"Link Type": "Single Link",
			"Bandwidth": "1 Gbps",
			"Background Utilization": "None",
			"Queue": "Default (250ms)",
			"Queuing Strategy": "FIFO",
			"MTU Limit": "None",
			"Frame Overhead": "38"
		},
		"Port 1 Access - inbound": {
			"Enabled": "False"
		},
		"Port 2 Access - outbound": {
			"Enabled": "True",
			"Link Type": "Single Link",
			"Bandwidth": "1 Gbps",
			"Background Utilization": "None",
			"Queue": "Default (250ms)",
			"Queuing Strategy": "FIFO",
			"MTU Limit": "None",
			"Frame Overhead": "38"
		},
		"Port 2 Access - inbound": {
			"Enabled": "False"
		},
		"WAN Conditions - Port 1 to Port 2": {
			"Loss": "None",
			"Corruption": "None",
			"Delay": "None",
			"Reordering": "None",
			"Duplication": "None",
			"Network outage": "None"
		},
		"WAN Conditions - Port 2 to Port 1": {
			"Loss": "None",
			"Corruption": "None",
			"Delay": "None",
			"Reordering": "None",
			"Duplication": "None",
			"Network outage": "None"
		}
	}
}
 */


/*
{
	"Bypass": {
		"489": {
			"aToB": {
				"tx_frames": "350426",
				"tx_rate": "8702603",
				"tx_bytes": "531946668"
			},
			"bToA": {
				"tx_frames": "21935",
				"tx_rate": "25120",
				"tx_bytes": "1535474"
			}
		}
	},
	"First one": {
		"489": {
			"aToB": {
				"tx_frames": "38898059",
				"drops": "598",
				"tx_rate": "978734765",
				"tx_bytes": "59825162542"
			},
			"bToA": {
				"tx_frames": "2471339",
				"drops": "0",
				"tx_rate": "3639468",
				"tx_bytes": "222462482"
			}
		}
	}
}
 */

    /*public static void main(String [ ] args) {
        String Monitorig="{\n" +
                "\t\"Bypass\": {\n" +
                "\t\t\"489\": {\n" +
                "\t\t\t\"aToB\": {\n" +
                "\t\t\t\t\"tx_frames\": \"350426\",\n" +
                "\t\t\t\t\"tx_rate\": \"8702603\",\n" +
                "\t\t\t\t\"tx_bytes\": \"531946668\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"bToA\": {\n" +
                "\t\t\t\t\"tx_frames\": \"21935\",\n" +
                "\t\t\t\t\"tx_rate\": \"25120\",\n" +
                "\t\t\t\t\"tx_bytes\": \"1535474\"\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"First one\": {\n" +
                "\t\t\"489\": {\n" +
                "\t\t\t\"aToB\": {\n" +
                "\t\t\t\t\"tx_frames\": \"38898059\",\n" +
                "\t\t\t\t\"drops\": \"598\",\n" +
                "\t\t\t\t\"tx_rate\": \"978734765\",\n" +
                "\t\t\t\t\"tx_bytes\": \"59825162542\"\n" +
                "\t\t\t},\n" +
                "\t\t\t\"bToA\": {\n" +
                "\t\t\t\t\"tx_frames\": \"2471339\",\n" +
                "\t\t\t\t\"drops\": \"0\",\n" +
                "\t\t\t\t\"tx_rate\": \"3639468\",\n" +
                "\t\t\t\t\"tx_bytes\": \"222462482\"\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t}\n" +
                "}\n";
        monitoringReader(Monitorig);
    }
*/



    public static List<NetropyData> monitoringReader(String jsontextcontent)
    {
        final Map<String, Object> json = JsonParserFactory.getJsonParser().parseMap(jsontextcontent);
        final String output="";
        List<NetropyData> metricsmap=new ArrayList<>();
        getVariableJsonPathMapRec(json,output,metricsmap);

        return metricsmap;

    }

    private static void getVariableJsonPathMapRec(final Object object,
                                                  final String currentJsonPath,final List<NetropyData> netropyDataeList) throws JSONException {
        if (object instanceof Map) {
            final JSONObject jsonObject = new JSONObject((Map<String, ?>) object);

            for (Object childKey : jsonObject.keySet()) {
                final String newCurrentJsonPath = currentJsonPath + "/'" + childKey ;
                getVariableJsonPathMapRec(jsonObject.get((String) childKey), newCurrentJsonPath,netropyDataeList);
            }

        } else if (object instanceof JSONArray) {
            final JSONArray jsonArray = (JSONArray) object;
            for (int i = 0; i < jsonArray.length(); i++) {
                final Object childObject = jsonArray.get(i);
                final String newCurrentJsonPath = currentJsonPath + "/" + i ;
                getVariableJsonPathMapRec(childObject, newCurrentJsonPath,netropyDataeList);
            }
        } else if(object instanceof JSONObject)
        {
            final JSONObject jsonObject =(JSONObject) object;

            for (Object childKey : jsonObject.keySet()) {

                final String newCurrentJsonPath = currentJsonPath + "/" + childKey ;
                getVariableJsonPathMapRec(jsonObject.get((String) childKey), newCurrentJsonPath,netropyDataeList);
                // TODO end of path
            }
        }
        else if (object instanceof String)
        {
            final String value=(String)object;
            String[] path=currentJsonPath.split("/");
            if (path.length==4)
            {
                NetropyData data=new NetropyData(path[0],path[1],path[2],path[3],value);
                netropyDataeList.add(data);
            }

        }
    }
}
