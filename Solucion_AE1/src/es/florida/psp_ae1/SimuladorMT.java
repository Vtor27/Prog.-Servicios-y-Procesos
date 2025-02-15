package es.florida.psp_ae1;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SimuladorMT implements Runnable {

	private int type, order;
	
	SimuladorMT(int type, int order) {
		this.type = type;
		this.order = order;
	}
	
	@Override
	public void run() {
		String t1 = getFormattedTimestamp();
		double calc = simulation(type);
		String t2 = getFormattedTimestamp();
		String diff = calculateDiff(t1, t2);
		writeFile("MT", type, order, t1, t2, diff, calc);
	}
	
	public static double simulation (int type) {
	    double calc = 0.0;
	    double simulationTime = Math.pow(5, type);
	    double startTime = System.currentTimeMillis();
	    double endTime = startTime + simulationTime;
	    while (System.currentTimeMillis() < endTime) {
	    	calc = Math.sin(Math.pow(Math.random(),2));
	    }
	    return calc;
	}

	public static String getFormattedTimestamp() {
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timestamp = sdf.format(now);
        long milliseconds = now.getTime() % 1000;
        long centiseconds = milliseconds / 10;
        timestamp += String.format("_%02d", centiseconds);
        return timestamp;
    }

	private static String calculateDiff(String t1, String t2) {
		int s1 = Integer.parseInt(t1.substring(13, 15));
		int s2 = Integer.parseInt(t2.substring(13, 15));
		int cs1 = 100 * s1 + Integer.parseInt(t1.split("_")[2]);
		int cs2 = 100 * s2 + Integer.parseInt(t2.split("_")[2]);
		int diffcs = cs2 - cs1;
		return (diffcs/100 + "_" + String.format("%02d", diffcs%100));
	}
	
	private static void writeFile(String option, int type, int order, String t1, String t2, String diff, double calc) {
		String title = "PROT_" + option + "_" + type + "_n" + order + "_" + t1 + ".sim";
		try {
			FileWriter fw = new FileWriter(title);
			fw.write(t1 + "\n");
			fw.write(t2 + "\n");
			fw.write(diff + "\n");
			fw.write(String.valueOf(calc));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
