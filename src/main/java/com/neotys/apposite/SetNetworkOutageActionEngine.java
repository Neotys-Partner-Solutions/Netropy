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
import java.util.concurrent.ExecutionException;

public class SetNetworkOutageActionEngine implements ActionEngine {
    private String netropy_HOSTNAME;
    private String netropy_ENGINE_IDX;
    private String netropy_PATH_IDX;
    private String netropy_PORT_1;
    private String netropy_PORT_2;
    private String intervalMin;
    private String intervalMax;
    private String durationMin;
    private String durationMax;
    private boolean networkOutage_Enabled;
    private NetropySession netropy;

    @Override
    public SampleResult execute(Context context, List<ActionParameter> list) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();
        String vnetworkOutage_Enabled=null;

        for(ActionParameter parameter:list) {
            switch (parameter.getName()) {
                case SetNetworkOutageAction.DurationMax:
                    durationMax = parameter.getValue();
                    break;
                case SetNetworkOutageAction.DurationMin:
                    durationMin = parameter.getValue();
                    break;
                case SetNetworkOutageAction.IntervalMax:
                    intervalMax = parameter.getValue();
                    break;
                case SetNetworkOutageAction.IntervalMin:
                    intervalMin = parameter.getValue();
                    break;
                case SetNetworkOutageAction.Netropy_ENGINE_IDX:
                    netropy_ENGINE_IDX = parameter.getValue();
                    break;
                case SetNetworkOutageAction.Netropy_PATH_IDX:
                    netropy_PATH_IDX = parameter.getValue();
                    break;
                case SetNetworkOutageAction.NetworkOutage_Enabled:
                    vnetworkOutage_Enabled = parameter.getValue();
                    break;

                case SetNetworkOutageAction.Netropy_HOSTNAME:
                    netropy_HOSTNAME = parameter.getValue();
                    break;
                case SetNetworkOutageAction.Netropy_PORT_1:
                    netropy_PORT_1 = parameter.getValue();
                    break;
                case SetNetworkOutageAction.Netropy_PORT_2:
                    netropy_PORT_2 = parameter.getValue();
                    break;
            }
        }
        if (Strings.isNullOrEmpty(netropy_HOSTNAME)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_HOSTNAME cannot be null "
                    + SetNetworkOutageAction.Netropy_HOSTNAME + ".", null);
        }
        if (Strings.isNullOrEmpty(netropy_ENGINE_IDX)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_ENGINE_IDX cannot be null "
                    + SetNetworkOutageAction.Netropy_ENGINE_IDX + ".", null);
        }
        else
        {
            if(!Validator.isaDigit(netropy_ENGINE_IDX))
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_ENGINE_IDX needs to be a digit "
                        + SetNetworkOutageAction.Netropy_ENGINE_IDX + ".", null);

        }
        if (Strings.isNullOrEmpty(netropy_PORT_1)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_1 cannot be null "
                    + SetNetworkOutageAction.Netropy_PORT_1 + ".", null);
        }
        else
        {
            if(!Validator.isaDigit(netropy_PORT_1))
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_1 needs to be a digit "
                        + SetNetworkOutageAction.Netropy_PORT_1 + ".", null);

        }
        if (Strings.isNullOrEmpty(netropy_PORT_2)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_2 cannot be null "
                    + SetNetworkOutageAction.Netropy_PORT_2 + ".", null);
        }
        else
        {
            if(!Validator.isaDigit(netropy_PORT_2))
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PORT_2 needs to be a digit "
                        + SetNetworkOutageAction.Netropy_PORT_2 + ".", null);

        }

        if (Strings.isNullOrEmpty(vnetworkOutage_Enabled)) {
            return getErrorResult(context, sampleResult, "Invalid argument: NetworkOutage_Enabled cannot be null "
                    + SetNetworkOutageAction.NetworkOutage_Enabled + ".", null);
        }
        else
        {
            if(!Validator.isaBoolan(vnetworkOutage_Enabled))
                return getErrorResult(context, sampleResult, "Invalid argument: NetworkOutage_Enabled needs to equal to true or false "
                        + SetNetworkOutageAction.NetworkOutage_Enabled + ".", null);
            else
                networkOutage_Enabled=Validator.getBooleanValue(vnetworkOutage_Enabled);
        }
        if(networkOutage_Enabled)
        {
            if (Strings.isNullOrEmpty(durationMin)) {
                return getErrorResult(context, sampleResult, "Invalid argument: DurationMin cannot be null "
                        + SetNetworkOutageAction.DurationMin + ".", null);
            }
            else
            {
                if(!Validator.isaDigit(durationMin))
                    return getErrorResult(context, sampleResult, "Invalid argument: DurationMin needs to be a digit "
                            + SetNetworkOutageAction.DurationMin + ".", null);

            }
            if (Strings.isNullOrEmpty(intervalMin)) {
                return getErrorResult(context, sampleResult, "Invalid argument: DurationMin cannot be null "
                        + SetNetworkOutageAction.DurationMin + ".", null);
            }
            else
            {
                if(!Validator.isaDigit(intervalMin))
                    return getErrorResult(context, sampleResult, "Invalid argument: IntervalMin needs to be a digit "
                            + SetNetworkOutageAction.IntervalMin + ".", null);

            }
            if (!Strings.isNullOrEmpty(intervalMax)) {
                if(!Validator.isaDigit(intervalMax))
                    return getErrorResult(context, sampleResult, "Invalid argument: IntervalMax needs to be a digit "
                            + SetNetworkOutageAction.IntervalMax + ".", null);

            }
            if (!Strings.isNullOrEmpty(durationMax)) {
                if(!Validator.isaDigit(durationMax))
                    return getErrorResult(context, sampleResult, "Invalid argument: DurationMax needs to be a digit "
                            + SetNetworkOutageAction.DurationMax + ".", null);

            }
        }
        try
        {
          netropy=(NetropySession)context.getCurrentVirtualUser().get(Validator.netropyObjectname+netropy_HOSTNAME);
            if(netropy!=null)
            {
                sampleResult.sampleStart();
                netropy.setEngineInfo(netropy_ENGINE_IDX,netropy_PATH_IDX);
                netropy.enableEmulation();
                if(networkOutage_Enabled)
                {
                      String function;
                      function="periodic "+intervalMin;
                      if (!Strings.isNullOrEmpty(intervalMax)){
                          function+="-"+intervalMax;
                      }
                      function+=" duraction min "+durationMin;
                        if (!Strings.isNullOrEmpty(durationMax))
                       {
                            function+="-"+durationMax;
                        }

                    netropy.setNetworkOutage(intervalMin,intervalMax,durationMin,durationMax,netropy_PORT_1,netropy_PORT_2);
                    responseBuilder.append("Network Outage applied o portn "+netropy_PORT_1+" "+netropy_PORT_2);
                }
                else
                {
                    netropy.disableNetworkOutage(netropy_PORT_1,netropy_PORT_2);
                    responseBuilder.append("Network outage removed from port "+ netropy_PORT_1 + " and  "+netropy_PORT_2);
                }
                sampleResult.sampleEnd();
            }
            else
                return getErrorResult(context, sampleResult, "Netropy connection Error: There is no existing connection on this appliance",null);

        }
        catch (Exception e) {
            return getErrorResult(context, sampleResult, "Technical Error:", e);
        }
        sampleResult.setRequestContent(requestBuilder.toString());
        sampleResult.setResponseContent(responseBuilder.toString());
        return sampleResult;
    }

    @Override
    public void stopExecute() {
        if(netropy!=null)
        {
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
        result.setStatusCode("NL-Netropy_SetNetworkOutage_ERROR");
        result.setResponseContent(errorMessage);
        if(exception != null){
            context.getLogger().error(errorMessage, exception);
        } else{
            context.getLogger().error(errorMessage);
        }
        return result;
    }
}
