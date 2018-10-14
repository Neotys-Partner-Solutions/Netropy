package com.neotys.apposite;

import com.google.common.base.Strings;
import com.neotys.apposite.Util.NetropySession;
import com.neotys.apposite.Util.Validator;
import com.neotys.extensions.action.ActionParameter;
import com.neotys.extensions.action.engine.ActionEngine;
import com.neotys.extensions.action.engine.Context;
import com.neotys.extensions.action.engine.SampleResult;

import java.util.List;

public class NetropyDisconnectActionEngine implements ActionEngine {
    private String netropy_HOSTNAME;
    private NetropySession netropySession;
    private String netropy_ENGINE_IDX;
    private String netropy_PATH_IDX;
    @Override
    public SampleResult execute(Context context, List<ActionParameter> list) {
        final SampleResult sampleResult = new SampleResult();
        final StringBuilder requestBuilder = new StringBuilder();
        final StringBuilder responseBuilder = new StringBuilder();

        for(ActionParameter parameter:list) {
            switch(parameter.getName()) {
                case NetropyDisconnectAction.Netropy_HOSTNAME:
                    netropy_HOSTNAME = parameter.getValue();
                    break;
                case NetropyDisconnectAction.Netropy_ENGINE_IDX:
                    netropy_ENGINE_IDX = parameter.getValue();
                    break;
                case  NetropyDisconnectAction.Netropy_PATH_IDX:
                    netropy_PATH_IDX = parameter.getValue();
                    break;
            }
        }
        if (Strings.isNullOrEmpty(netropy_HOSTNAME)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_HOSTNAME cannot be null "
                    + NetropyDisconnectAction.Netropy_HOSTNAME + ".", null);
        }
        if (Strings.isNullOrEmpty(netropy_ENGINE_IDX)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_ENGINE_IDX cannot be null "
                    + NetropyDisconnectAction.Netropy_ENGINE_IDX + ".", null);
        }
        else
        {
            if(!Validator.isaDigit(netropy_ENGINE_IDX))
                return getErrorResult(context, sampleResult, "Invalid argument: Netropy_ENGINE_IDX needs to be a digit "
                        + NetropyDisconnectAction.Netropy_ENGINE_IDX + ".", null);

        }
        if (Strings.isNullOrEmpty(netropy_PATH_IDX)) {
            return getErrorResult(context, sampleResult, "Invalid argument: Netropy_PATH_IDX cannot be null "
                    + NetropyDisconnectAction.Netropy_PATH_IDX + ".", null);
        }
        try {
            netropySession = (NetropySession) context.getCurrentVirtualUser().get(Validator.netropyObjectname + netropy_HOSTNAME);
            if(netropySession!=null)
            {
                netropySession.setEngineInfo(netropy_ENGINE_IDX, netropy_PATH_IDX);
                sampleResult.sampleStart();
                if (netropySession != null) {
                    netropySession.disconnect();
                }
            }
            sampleResult.sampleEnd();
        }
        catch (Exception e)
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
        result.setStatusCode("NL-Netropy_DisConnect_ERROR");
        result.setResponseContent(errorMessage);
        if(exception != null){
            context.getLogger().error(errorMessage, exception);
        } else{
            context.getLogger().error(errorMessage);
        }
        return result;
    }
}
