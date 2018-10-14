package com.neotys.apposite;

import com.apposite.api.Netropy;
import com.google.common.base.Strings;
import com.neotys.apposite.Util.NetropySession;
import com.neotys.apposite.Util.Validator;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

import java.util.List;

public class MonitoringActionEngine implements ActionEngine {
    private String netropy_HOSTNAME;
    private String netropy_ENGINE_IDX;
    private String neoLoadAPIHost;
    private String neoLoadAPIport;
    private String neoLoadKeyAPI;

    private NetropySession netropy;
    @Override
    public SampleResult execute(Context context, List<ActionParameter> list) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();

        for(ActionParameter parameter:list) {
            switch(parameter.getName()) {
                case MonitoringAction.Netropy_HOSTNAME:
                    netropy_HOSTNAME = parameter.getValue();
                    break;
                case MonitoringAction.Netropy_ENGINE_IDX:
                    netropy_ENGINE_IDX = parameter.getValue();
                    break;
                case MonitoringAction.NeoLoadAPIHost:
                    neoLoadAPIHost = parameter.getValue();
                    break;
                case MonitoringAction.NeoLoadAPIport:
                    neoLoadAPIport = parameter.getValue();
                    break;
                case MonitoringAction.NeoLoadKeyAPI:
                    neoLoadKeyAPI = parameter.getValue();
                    break;
            }
        }

        if (Strings.isNullOrEmpty(neoLoadAPIHost)) {
            return getErrorResult(context, sampleResult, "Invalid argument: NeoLoadAPIHost cannot be null "
                    + MonitoringAction.NeoLoadAPIHost + ".", null);
        }
        if (Strings.isNullOrEmpty(neoLoadAPIport)) {
            return getErrorResult(context, sampleResult, "Invalid argument: NeoLoadAPIport cannot be null "
                    + MonitoringAction.NeoLoadAPIport + ".", null);
        }
        else
        {
            if(!Validator.isaDigit(neoLoadAPIport))
                return getErrorResult(context, sampleResult, "Invalid argument: NeoLoadAPIport needs to be a digit "
                        + MonitoringAction.NeoLoadAPIport + ".", null);

        }
        if (Strings.isNullOrEmpty(netropy_HOSTNAME)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_HOSTNAME cannot be null "
                    + MonitoringAction.Netropy_HOSTNAME + ".", null);
        }

        if (Strings.isNullOrEmpty(netropy_ENGINE_IDX)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_Engine_IDX cannot be null "
                    + MonitoringAction.Netropy_ENGINE_IDX + ".", null);
        }
        try
        {
            netropy=(NetropySession)context.getCurrentVirtualUser().get(Validator.netropyObjectname+netropy_HOSTNAME);
            if(netropy!=null)
            {

            }
            else
            {
                return getErrorResult(context, sampleResult, "Netropy connection Error: There is no existing connection on this appliance",null);

            }
        }
        catch(Exception e)
        {
            return getErrorResult(context, sampleResult, "Technical Error:", e);

        }
        return sampleResult;
    }

    @Override
    public void stopExecute() {

    }

    private void appendLineToStringBuilder(final StringBuilder sb, final String line){
        sb.append(line).append("\n");
    }

    /**
     * This method allows to easily create an error result and log exception.
     */
    private static SampleResult getErrorResult(final Context context, final SampleResult result, final String errorMessage, final Exception exception) {
        result.setError(true);
        result.setStatusCode("NL-Netropy_Monitoring_ERROR");
        result.setResponseContent(errorMessage);
        if(exception != null){
            context.getLogger().error(errorMessage, exception);
        } else{
            context.getLogger().error(errorMessage);
        }
        return result;
    }

}
