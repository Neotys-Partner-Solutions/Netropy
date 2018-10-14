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

public class MonitoringAction implements Action {
    public static final String Netropy_HOSTNAME = "Netropy_HOSTNAME";
    public static final String Netropy_ENGINE_IDX = "Netropy_ENGINE_IDX";
    public static final String NeoLoadAPIHost="NeoLoadAPIHost";
    public static final String NeoLoadAPIport="NeoLoadAPIport";
    public static final String NeoLoadKeyAPI="NeoLoadKeyAPI";
    private static final String BUNDLE_NAME = "com.neotys.apposite.bundle";
    private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayName");
    private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");

    private static final ImageIcon LOGO_ICON;

    @Override
    public String getType() {
        return "Netropy_Monitoring";
    }

    @Override
    public List<ActionParameter> getDefaultActionParameters() {
        final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
        parameters.add(new ActionParameter(Netropy_HOSTNAME,"localhost"));
        parameters.add(new ActionParameter(Netropy_ENGINE_IDX,"1"));
        parameters.add(new ActionParameter(NeoLoadAPIHost,"localhost"));
        parameters.add(new ActionParameter(NeoLoadAPIport,"7400"));



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
        return MonitoringActionEngine.class;
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
        description.append("\tNeoLoadAPIHost : Ip of hostname of the NeoLoad DataExchange API\n");
        description.append("\tNeoLoadAPIport : port of the DataExchangeAPi ( default 7400) \n");
        description.append("\t(Optional) NeoLoadKeyAPI : NeoLoad's APi Key\n");

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
