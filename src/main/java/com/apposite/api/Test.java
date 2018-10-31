package com.apposite.api;
import java.util.concurrent.TimeUnit;

class Test {
	private static final String HOSTNAME = "netropy-ve";
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "";
	private static final Integer TIMEOUT = 10;
	private static final String ENGINE_IDX = "1";
	private static final String PATH_IDX = "1";
	private static final String PORT_1 = "1";
	private static final String PORT_2 = "2";

	public static void main(String[] args) {
		try {
			FullTest();
			StatsTest();
			ConfigTest();
			CustomCmdTest();
			ErrorTest();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	SUBCOMMANDS
//	---------
//	setBandwidth
//		fixed <value> [bps|Kbps|Mbps|Gbps]
//			"fixed 1 Gbps"
//	
//	setDelay
//		none
//		constant <ms>
//			"constant 10"
//		uniform <min> <max> [reordering {enabled|disabled}]
//			"uniform 5 10"
//		normal <mean> <stddev> [reordering {enabled|disabled}]
//			"normal 50 2"
//		exponential <min> <max> [reordering {enabled|disabled}]
//			"exponential 5 10"
//		accumulate-and-burst <count> [extra-delay <ms>] [timeout <ms>]
//			"accumulate-and-burst 5 extra-delay 10 timeout 20"
//		recorded
//			*requires loaded recording
//	
//	setLoss
//		none
//		random <percent>
//			"random 0.05"
//		burst <percent> min <min-burst-packets> max <max-burstpackets>
//			"burst 0.01 min 5 max 10"
//		periodic <period> [burst <count>]
//			"periodic 5 10"
//		ber <rate (1e-18 – 9.999999e-1)>
//			"ber 1e-18"
//		gilbert-elliott good-state loss <pct> change <pct> bad-state loss <pct> change <pct>
//			"gilbert-elliott good-state loss 0.01 change 0.1 bad-state loss 0.2 change 0.1"
//		recorded
//			*requires loaded recording
//	
//	setNetworkOutage
//		none
//		periodic interval <min>[-<max>] duration <min>[-<max>]
//			"period interval 5 duration 10" or "periodic interval 5-10 duration 10-15"

	public static void FullTest() throws InterruptedException {
		Netropy n = new Netropy(HOSTNAME, USERNAME, PASSWORD, TIMEOUT);

		try {
			n.setEmulationOn(ENGINE_IDX);

			// SET A CONFIG
			n.setDelay(ENGINE_IDX, PATH_IDX, "constant 25", PORT_1, PORT_2);
			n.setDelay(ENGINE_IDX, PATH_IDX, "uniform 5 10", PORT_1, PORT_2);
			n.setDelay(ENGINE_IDX, PATH_IDX, "normal 50 2", PORT_1, PORT_2);
			n.setDelay(ENGINE_IDX, PATH_IDX, "exponential 5 10", PORT_1, PORT_2);
			n.setDelay(ENGINE_IDX, PATH_IDX, "accumulate-and-burst 5 extra-delay 10 timeout 20", PORT_1, PORT_2);
//			n.setDelay(ENGINE_IDX, PATH_IDX, "recorded", PORT_1, PORT_2);

			n.setLoss(ENGINE_IDX, PATH_IDX, "random 0.05", PORT_1, PORT_2);
			n.setLoss(ENGINE_IDX, PATH_IDX, "burst 0.01 min 5 max 10", PORT_1, PORT_2);
			n.setLoss(ENGINE_IDX, PATH_IDX, "ber 1e-18", PORT_1, PORT_2);
			n.setLoss(ENGINE_IDX, PATH_IDX,
					"gilbert-elliott good-state loss 0.01 change 0.1 bad-state loss 0.2 change 0.1", PORT_1, PORT_2);
//			n.setLoss(ENGINE_IDX, PATH_IDX, "recorded", PORT_1, PORT_2);

			n.setBandwidth(ENGINE_IDX, PATH_IDX, "fixed 10 Gbps", PORT_1, "out");
			n.setBandwidth(ENGINE_IDX, PATH_IDX, "fixed 10 Gbps", PORT_2, "out");

			n.setNetworkOutage(ENGINE_IDX, PATH_IDX, "periodic interval 5 duration 10", PORT_1, PORT_2);
			n.setNetworkOutage(ENGINE_IDX, PATH_IDX, "periodic interval 5-10 duration 10-15", PORT_1, PORT_2);

			System.out.println(n.getPathConfig(ENGINE_IDX, PATH_IDX));

			TimeUnit.SECONDS.sleep(5);

			// RESET CONFIG
			n.setBandwidth(ENGINE_IDX, PATH_IDX, "fixed 1 Gbps", PORT_1, "out");
			n.setBandwidth(ENGINE_IDX, PATH_IDX, "fixed 1 Gbps", PORT_2, "out");
			n.setDelay(ENGINE_IDX, PATH_IDX, "none", PORT_1, PORT_2);
			n.setLoss(ENGINE_IDX, PATH_IDX, "none", PORT_1, PORT_2);
			n.setNetworkOutage(ENGINE_IDX, PATH_IDX, "none", "1", "2");
			n.setEmulationOff(ENGINE_IDX);

			System.out.println(n.getPathConfig(ENGINE_IDX, PATH_IDX));

			TimeUnit.SECONDS.sleep(5);

			// STAT FETCH
			System.out.println(n.getTotalStats(ENGINE_IDX));
			n.resetStats(ENGINE_IDX);

		} catch (NetropyException e) {
			e.printStackTrace();
		} finally {
			n.disconnectNetropy();
		}
	}

	public static void ErrorTest() throws InterruptedException {
		Netropy n = new Netropy(HOSTNAME, USERNAME, PASSWORD, TIMEOUT);

		try {
			// bubbles up Netropy error from CLI
//			n.emulationOn("5");
			// unknown command error
			n.setDelay(ENGINE_IDX, PATH_IDX, "?", PORT_1, PORT_2);
		} catch (NetropyException e) {
			e.printStackTrace();
		} finally {
			n.disconnectNetropy();
		}
	}

	public static void StatsTest() throws InterruptedException {
		Netropy n = new Netropy(HOSTNAME, USERNAME, PASSWORD, TIMEOUT);

		try {
			n.resetStats(ENGINE_IDX);
			// best to add a small break between reset and getStats
			// possible error can occur where there is no data available
			TimeUnit.SECONDS.sleep(1);
			String results = n.getTotalStats(ENGINE_IDX);
			System.out.println(results);
		} catch (NetropyException e) {
			e.printStackTrace();
		} finally {
			n.disconnectNetropy();
		}
	}

	public static void ConfigTest() throws InterruptedException {
		Netropy n = new Netropy(HOSTNAME, USERNAME, PASSWORD, TIMEOUT);

		try {
			String results = n.getPathConfig(ENGINE_IDX, PATH_IDX);
			System.out.println(results);
			System.out.println(n.getPathConfigOriginalFormat(ENGINE_IDX, PATH_IDX));
		} catch (NetropyException e) {
			e.printStackTrace();
		} finally {
			n.disconnectNetropy();
		}
	}

	// getCmds and send commands are intentionally left public to allow custom
	// commands
	public static void CustomCmdTest() throws InterruptedException {
		Netropy n = new Netropy(HOSTNAME, USERNAME, PASSWORD, TIMEOUT);
		try {
			String cmd = n.getCloudCmd(ENGINE_IDX, PATH_IDX, "duplication", "random 0.05", PORT_1, PORT_2);
			n.sendCliCmd(cmd);
			System.out.println(n.getPathConfigOriginalFormat(ENGINE_IDX, PATH_IDX));
			cmd = n.getCloudCmd(ENGINE_IDX, PATH_IDX, "duplication", "none", PORT_1, PORT_2);
			n.sendCliCmd(cmd);
			System.out.println(n.getPathConfigOriginalFormat(ENGINE_IDX, PATH_IDX));
		} catch (NetropyException e) {
			e.printStackTrace();
		} finally {
			n.disconnectNetropy();
		}
	}

	public static void print(String string) {
		System.out.println(string);
	}
}