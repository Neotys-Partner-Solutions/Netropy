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

public class NetropyDisconnectAction implements Action {
    public static final String Netropy_HOSTNAME = "Netropy_HOSTNAME";
    private static final String BUNDLE_NAME = "com.neotys.apposite.bundle";
    private static final String DISPLAY_NAME = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayDisconnect");
    private static final String DISPLAY_PATH = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault()).getString("displayPath");
    public static final String Netropy_ENGINE_IDX = "Netropy_ENGINE_IDX";
    public static final String Netropy_PATH_IDX = "Netropy_PATH_IDX";

    private static final ImageIcon LOGO_ICON;
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
    public String getType() {
        return "Netropy_DisConnect";
    }

    @Override
    public List<ActionParameter> getDefaultActionParameters() {
        final List<ActionParameter> parameters = new ArrayList<ActionParameter>();
        parameters.add(new ActionParameter(Netropy_HOSTNAME,"localhost"));
        parameters.add(new ActionParameter(Netropy_ENGINE_IDX,"1"));
        parameters.add(new ActionParameter(Netropy_PATH_IDX,"2"));


        // TODO Add default parameters.
        return parameters;
    }

    @Override
    public Class<? extends ActionEngine> getEngineClass() {
        return NetropyDisconnectActionEngine.class;
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
        description.append("Netropy_DisConnect will disconnect to the NetropyAppliance.\n");
        description.append("Netropy_Connect requires the following parameters\n");
        description.append("\tNetropy_HOSTNAME : HostName or Ip of the Netropy Appliance\n");
        description.append("\tNetropy_ENGINE_IDX : Engine id of the appliance\n");
        description.append("\tNetropy_PATH_IDX : path id of the appliance\n");



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
        return null;
    }

    @Override
    public Optional<String> getMaximumNeoLoadVersion() {
        return null;
    }
}
