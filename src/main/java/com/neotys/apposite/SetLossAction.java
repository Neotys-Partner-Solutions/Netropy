package com.neotys.apposite;


import com.google.common.base.Optional;
import com.neotys.extensions.action.Action;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class SetLossAction implements Action {
    public static final String LossPercent = "LossPercent";
    public static final String LossMin = "LossMin";
    public static final String LossMax = "LossMax";
    public static final String LossType="LossType";
    public static final String LossPeriod="LossPeriod";
    public static final String Losscount="Losscount";

    public static final String Netropy_HOSTNAME = "Netropy_HOSTNAME";
    public static final String Netropy_ENGINE_IDX = "Netropy_ENGINE_IDX";
    public static final String Netropy_PATH_IDX = "Netropy_PATH_IDX";
    public static final String Netropy_PORT_1 = "Netropy_PORT_1";
    public static final String Netropy_PORT_2 = "Netropy_PORT_2";

    private static final String BUNDLE_NAME = "com.neotys.apposite.bundle";
    private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displaySetLoss");
    private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");

    private static final ImageIcon LOGO_ICON;

    @Override
    public String getType() {
        return "Netropy_SetLoss";
    }

    @Override
    public List<ActionParameter> getDefaultActionParameters() {
        final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
        parameters.add(new ActionParameter(Netropy_HOSTNAME,"localhost"));
        parameters.add(new ActionParameter(Netropy_ENGINE_IDX,"1"));
        parameters.add(new ActionParameter(Netropy_PATH_IDX,"2"));
        parameters.add(new ActionParameter(Netropy_PORT_1,"1"));
        parameters.add(new ActionParameter(Netropy_PORT_2,"2"));
        parameters.add(new ActionParameter(LossType,"RANDOM"));
        parameters.add(new ActionParameter(LossPercent,"0.05"));


        // TODO Add default parameters.
        return parameters;
    }
    static {
        final URL iconURL = SetDelayAction.class.getResource("logo.png");
        if (iconURL != null) {
            LOGO_ICON = new ImageIcon(iconURL);
        }
        else {
            LOGO_ICON = null;
        }
    }
    @Override
    public Class<? extends ActionEngine> getEngineClass() {
        return SetLossActionEngine.class;
    }

    @Override
    public boolean getDefaultIsHit() {
        return false;
    }

    @Override
    public Icon getIcon() {
        return LOGO_ICON;
    }

    @Override
    public String getDescription() {


        final StringBuilder description = new StringBuilder();
        // TODO Add description
        description.append("Netropy_SetLoss will set packet loss between port1 and port 2\n");
        description.append("Netropy_SetLoss requires the following parameters\n");
        description.append("\tNetropy_HOSTNAME : Ip of hostname of the netropy appliance\n");
        description.append("\tNetropy_ENGINE_IDX : Engine id of the appliance\n");
        description.append("\tNetropy_PATH_IDX : path id of the appliance\n");
        description.append("\tNetropy_PORT_1 : port numnber of the appliance where you would like to apply packet loss from\n");
        description.append("\tNetropy_PORT_2 : port numnber of the appliance where you would like to apply packet loss to\n");
        description.append("\tLossType : Type of Packet Loss algorithm. Value possible : RANDOM,  BURST, PERIODIC,NONE \n");
        description.append("\tLossPercent : percentage value , value possible for the algorithm : RANDOM, BURST");
        description.append("\tLossMin : min burst value , value possible for the algorithm : BURST");
        description.append("\tLossMax : max burst value , value possible for the algorithm : BURST");
        description.append("\tLossPeriod : period value, value possible for the algorithm : PERIODIC");
        description.append("\tLosscount : Count value , value possible for the algorithm : PERIODIC");



        return description.toString();
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public String getDisplayPath() {
        return DISPLAY_PATH;
    }

    @Override
    public Optional<String> getMinimumNeoLoadVersion() {
        return Optional.of("6.3");
    }

    @Override
    public Optional<String> getMaximumNeoLoadVersion() {
        return Optional.absent();
    }
}
