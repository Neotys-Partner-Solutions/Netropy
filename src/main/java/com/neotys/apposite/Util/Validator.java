package com.neotys.apposite.Util;

import java.util.Arrays;
import java.util.List;

public class Validator {
    private static final List<String> units = Arrays.asList("bps", "Kbps", "Mbps", "Gbps");
    public static final String netropyObjectname="Netropy_";
    public static boolean isaDigit(String value)
    {
        double dvalue;
        try
        {
            dvalue=Double.parseDouble(value);
            return true;
        }
        catch (NumberFormatException e)
        {
            return false;
        }
    }
    public static boolean isaUnit(String value)
    {
        return units.contains(value);
    }
    public static boolean isaBoolan(String value)
    {
        if(!value.equalsIgnoreCase("TRUE")&& value.equalsIgnoreCase("FALSE"))
            return false;
        else
            return true;
    }

    public static boolean getBooleanValue(String value)
    {
        if(value.equalsIgnoreCase("TRUE"))
            return true;
        else
            return false;
    }
}
