package com.apposite.api;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import net.sf.expectit.Expect;
import net.sf.expectit.ExpectBuilder;

import static net.sf.expectit.matcher.Matchers.times;
import static net.sf.expectit.matcher.Matchers.contains;
import static net.sf.expectit.matcher.Matchers.regexp;

public class Netropy {
	private Expect n;
	private String host;
	private String user;
	private String pass;
	private Integer timeout;

	private Session session;
	private Channel channel;

	private final String DEVICE_PROMPT = ".*(\\]> $)";
	private final String ERROR_PREFIX = "*** Error:";
	private final String UNKNOWN_CMD_PREFIX = "Usage: ";

	public Netropy(String host, String user, String pass, Integer timeout) {
		this.host = host;
		this.user = user;
		this.pass = pass;
		this.timeout = timeout;

		this.connectNetropy();
	}

	public void connectNetropy() {
		JSch jSch = new JSch();
		try {
			this.session = jSch.getSession(this.user, this.host);
			Properties config = new Properties();
			this.session.setPassword(this.pass);
			config.put("StrictHostKeyChecking", "no");
			this.session.setConfig(config);
			this.session.connect();
			this.channel = this.session.openChannel("shell");
			this.channel.connect();
		} catch (JSchException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			this.n = new ExpectBuilder().withOutput(this.channel.getOutputStream())
					.withInputs(this.channel.getInputStream(), this.channel.getExtInputStream())
					.withTimeout(this.timeout, TimeUnit.SECONDS).withExceptionOnFailure().build();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			this.n.sendLine("");
			this.n.expect(regexp(DEVICE_PROMPT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// if session isn't terminated, it will stay open and consume resources
	public void disconnectNetropy() {
		try {
			this.n.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.channel.disconnect();
		this.session.disconnect();
	}

	public String getEngineCmd(String engineIdx, String cmd) {
		Object[] args = new Object[] { engineIdx, cmd };
		String template = "engine %s %s";
		String cmdToSend = String.format(template, args);
		return cmdToSend;
	}

	public String getPathCmd(String engineIdx, String pathIdx, String cmd) {
		Object[] args = new Object[] { engineIdx, pathIdx, cmd };
		String template = "engine %s path %s %s";
		String cmdToSend = String.format(template, args);
		return cmdToSend;
	}

	public String getCloudCmd(String engineIdx, String pathIdx, String cmd, String subCmd, String src_port,
			String dst_port) {
		Object[] args = new Object[] { engineIdx, pathIdx, cmd, subCmd, src_port, dst_port };
		String template = "engine %s path %s set %s %s port %s to port %s";
		String cmdToSend = String.format(template, args);
		return cmdToSend;
	}

	public String getAccessCmd(String engine, String pathIdx, String cmd, String subCmd, String port,
			String direction) {
		Object[] args = new Object[] { engine, pathIdx, cmd, subCmd, port, direction };
		String template = "engine %s path %s set %s %s port %s %s";
		String cmdToSend = String.format(template, args);
		return cmdToSend;
	}

	public String sendCliCmd(String cmd) throws NetropyException {
		String output = null;
		try {
			this.n.sendLine(cmd);
			// this skips the sent command by skipping one line
			this.n.expect(times(1, contains("\n")));
			output = this.n.expect(regexp(DEVICE_PROMPT)).getBefore().trim();
			if (output.contains(ERROR_PREFIX)) {
				throw new NetropyException(output);
			} else if (output.contains(UNKNOWN_CMD_PREFIX)) {
				throw new NetropyException("Bad Command: '" + cmd);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}

	public void setEmulationOn(String engineIdx) throws NetropyException {
		String cliCmd = getEngineCmd(engineIdx, "emulation on");
		sendCliCmd(cliCmd);
	}

	public void setEmulationOff(String engineIdx) throws NetropyException {
		String cliCmd = getEngineCmd(engineIdx, "emulation off");
		sendCliCmd(cliCmd);
	}

	public String getPathConfig(String engineIdx, String pathIdx) throws NetropyException {
		String cliCmd = getPathCmd(engineIdx, pathIdx, "show");
		String output = sendCliCmd(cliCmd);
		return PathConfigToJson.getJsonFormat(output);
	}

	public String getPathConfigOriginalFormat(String engineIdx, String pathIdx) throws NetropyException {
		String cliCmd = getPathCmd(engineIdx, pathIdx, "show");
		return sendCliCmd(cliCmd);
	}

	public void setDelay(String engineIdx, String pathIdx, String subCmd, String src_port, String dst_port)
			throws NetropyException {
		String cliCmd = getCloudCmd(engineIdx, pathIdx, "delay", subCmd, src_port, dst_port);
		sendCliCmd(cliCmd);
	}

	public void setLoss(String engineIdx, String pathIdx, String subCmd, String src_port, String dst_port)
			throws NetropyException {
		String cliCmd = getCloudCmd(engineIdx, pathIdx, "loss", subCmd, src_port, dst_port);
		sendCliCmd(cliCmd);
	}

	public void setNetworkOutage(String engineIdx, String pathIdx, String subCmd, String src_port, String dst_port)
			throws NetropyException {
		String cliCmd = getCloudCmd(engineIdx, pathIdx, "network-outage", subCmd, src_port, dst_port);
		sendCliCmd(cliCmd);
	}

	public void setBandwidth(String engineIdx, String pathIdx, String subCmd, String port, String direction)
			throws NetropyException {
		String cliCmd = getAccessCmd(engineIdx, pathIdx, "bw", subCmd, port, direction);
		sendCliCmd(cliCmd);
	}

	// the bitrates given are the total bytes * 8 / time
	// they are not current bitrates
	public String getTotalStats(String engineIdx) throws NetropyException {
		String cliCmd = getEngineCmd(engineIdx, "statistics total");
		String output = sendCliCmd(cliCmd);
		return StatsToJson.getJsonFormat(output);
	}

	// historical data will be too large to process for expect
	// e.g. overnight testing, since the interval is one second
//    public String getIntervalStats(String engine) throws NetropyException {
//    	String cliCmd = getEngineCmd(engine, "statistics interval");
//	   	String output = sendCli(cliCmd);
//	   	return StatsToJson.getJsonFormat(output);
//   	}
//    
//    public String getCumulativeStats(String engine) throws NetropyException {
//    	String cliCmd = getEngineCmd(engine, "statistics cumulative");
//	   	String output = sendCli(cliCmd);
//	   	return output;
//   	}

	public void resetStats(String engineIdx) throws NetropyException {
		String cliCmd = getEngineCmd(engineIdx, "statistics reset");
		sendCliCmd(cliCmd);
	}
}