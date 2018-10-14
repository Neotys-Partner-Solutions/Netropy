package com.neotys.apposite;

import com.apposite.api.Netropy;
import com.apposite.api.NetropyException;
import com.google.common.base.Strings;
import com.neotys.apposite.Util.NetropySession;
import com.neotys.apposite.Util.Validator;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

import java.util.List;

public class NetropyConnectActionEngine implements ActionEngine {
    private String netropy_HOSTNAME;
    private String netropy_USERNAME;
    private String netropy_PASSWORD ;
    private int timeout;
    private NetropySession netropy;

    @Override
    public SampleResult execute(Context context, List<ActionParameter> list) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();
        String vtimeout=null;

        for(ActionParameter parameter:list) {
            switch(parameter.getName()) {
                case NetropyConnectAction.Netropy_HOSTNAME:
                    netropy_HOSTNAME = parameter.getValue();
                    break;
                case NetropyConnectAction.Netropy_PASSWORD:
                    netropy_PASSWORD = parameter.getValue();
                    break;
                case NetropyConnectAction.Netropy_USERNAME:
                    netropy_USERNAME = parameter.getValue();
                    break;
                case NetropyConnectAction.TIMEOUT:
                    vtimeout = parameter.getValue();
                    break;
            }
        }
        if (Strings.isNullOrEmpty(netropy_HOSTNAME)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_HOSTNAME cannot be null "
                    + NetropyConnectAction.Netropy_HOSTNAME + ".", null);
        }

        if (Strings.isNullOrEmpty(netropy_USERNAME)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_USERNAME cannot be null "
                    + NetropyConnectAction.Netropy_USERNAME + ".", null);
        }

        if (Strings.isNullOrEmpty(netropy_PASSWORD)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PASSWORD cannot be null "
                    + NetropyConnectAction.Netropy_PASSWORD + ".", null);
        }
        if (Strings.isNullOrEmpty(vtimeout)) {
            return getErrorResult(context, sampleResult, "Invalid argument: TIMEOUT cannot be null "
                    + NetropyConnectAction.TIMEOUT + ".", null);
        }
        else {
            if(!Validator.isaDigit(vtimeout))
                return getErrorResult(context, sampleResult, "Invalid argument: TIMEOUT needs to be digit "
                        + NetropyConnectAction.TIMEOUT + ".", null);
            else
            {
                timeout=Integer.parseInt(vtimeout);
            }
        }

        try
        {
            sampleResult.sampleStart();
            netropy = new NetropySession(netropy_HOSTNAME, netropy_USERNAME, netropy_PASSWORD, timeout);
            responseBuilder.append("Connected to netropy on host :"+netropy_HOSTNAME);
            sampleResult.sampleEnd();
            context.getCurrentVirtualUser().put(Validator.netropyObjectname + netropy_HOSTNAME, netropy);
        }
        catch (Exception e)
        {
            return getErrorResult(context, sampleResult, "Technical Error:", e);

        }
        sampleResult.setRequestContent(requestBuilder.toString());
        sampleResult.setResponseContent(responseBuilder.toString());
        return sampleResult;
    }

    @Override
    public void stopExecute() {
        if(netropy!=null) {
            try {
                netropy.disconnect();
            } catch (NetropyException e) {
                e.printStackTrace();
            }
        }
    }
    private void appendLineToStringBuilder(final StringBuilder sb, final String line){
        sb.append(line).append("\n");
    }

    /**
     * This method allows to easily create an error result and log exception.
     */
    private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
        result.setError(true);
        result.setStatusCode("NL-Netropy_Connect_ERROR");
        result.setResponseContent(errorMessage);
        if(exception != null){
            context.getLogger().error(errorMessage, exception);
        } else{
            context.getLogger().error(errorMessage);
        }
        return result;
    }
}
