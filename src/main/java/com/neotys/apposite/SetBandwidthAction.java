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

public class SetBandwidthAction implements Action {
    public static final String BandwidthValue = "BandwidthValue";
    public static final String BandwidthUnit = "BandwidthUnit";
    public static final String BandwidthLimitation_Enabled="BandwidthLimitation_Enabled";
    public static final String Netropy_HOSTNAME = "Netropy_HOSTNAME";
    public static final String Netropy_ENGINE_IDX = "Netropy_ENGINE_IDX";
    public static final String Netropy_PATH_IDX = "Netropy_PATH_IDX";
    public static final String Netropy_PORT = "Netropy_PORT";
    private static final String BUNDLE_NAME = "com.neotys.apposite.bundle";
    private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displaySetBandwidth");
    private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");

    private static final ImageIcon LOGO_ICON;

    @Override
    public String getType() {
        return "Netropy_SetBandwidth";
    }

    @Override
    public List<ActionParameter> getDefaultActionParameters() {
        final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
        parameters.add(new ActionParameter(Netropy_HOSTNAME,"localhost"));
        parameters.add(new ActionParameter(Netropy_ENGINE_IDX,"1"));
        parameters.add(new ActionParameter(Netropy_PATH_IDX,"2"));
        parameters.add(new ActionParameter(Netropy_PORT,"2"));
        parameters.add(new ActionParameter(BandwidthValue,"2"));
        parameters.add(new ActionParameter(BandwidthUnit,"Gbps"));
        parameters.add(new ActionParameter(BandwidthLimitation_Enabled,"true"));


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
        return SetBandwidthActionEngine.class;
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
        description.append("Netropy_SetBandwidth will limit the bandwidth on the specific port of the netropy appliance.\n");
        description.append("Netropy_SetBandwidth requires the following parameters\n");
        description.append("\tNetropy_HOSTNAME : Ip of hostname of the netropy appliance\n");
        description.append("\tNetropy_ENGINE_IDX : Engine id of the appliance\n");
        description.append("\tNetropy_PATH_IDX : path id of the appliance\n");
        description.append("\tNetropy_PORT : port numnber of the appliance where you would like to limit the bandwidth\n");
        description.append("\tBandwidthValue : Value of the bandwidth\n");
        description.append("\tBandwidthUnit : Unit , value possible : bps|Kbps|Mbps|Gbps");
        description.append("\tBandwidthLimitation_Enabled : boolean (true or false) , Enable or disable the bandwidth limitation");

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
