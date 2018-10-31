package com.neotys.apposite.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class NetropyData {

    private String paths;

    private String timeIdx;
    private String from;
    private String type;
    private String value;
    private String unit;

    public NetropyData(String paths, String timeIdx, String from, String type, String value) {
        this.paths = paths;
        this.timeIdx = timeIdx;
        this.from = from;
        this.type = type;
        if(type.contains("_"))
        {
            String[] unittable=type.split("_");
            if(unittable.length==2)
                this.unit=unittable[1];
            else
                this.unit=null;
        }
        else
            unit=null;
        this.value = value;
    }

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }

    public String getTimeIdx() {
        return timeIdx;
    }

    public String getUnit() {
        return unit;
    }

    public void setTimeIdx(String timeIdx) {
        this.timeIdx = timeIdx;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
