package game;
import java.io.File;
import java.io.FileWriter;

public class Logger {
	
	FileWriter writer;
	private static Logger single_instance = null;
	private File outputFile;
	
	public static Logger getInstance(String logfile)
    {
        if (single_instance == null) { single_instance = new Logger(logfile); }
        return single_instance;
    }
	
	private Logger(String logfile) {
		writer = null;
		try {
			String current_dir = new java.io.File(".").getCanonicalPath();
			outputFile = new File(current_dir + "\\src\\" + logfile);
			writer = new FileWriter(outputFile);
		}
		catch (Exception e) { e.printStackTrace(); }
		finally { try { writer.close(); } catch (Exception e) { e.printStackTrace(); } }
		
	}
	
	protected void log(String event) {
		try {
			writer = new FileWriter(outputFile, true);
			writer.write(System.currentTimeMillis() + " (Logger): " + event +"\r\n");
			writer.flush();
		}
		catch (Exception e) { e.printStackTrace(); }
		finally { try { writer.close(); } catch (Exception e) { e.printStackTrace(); } }
	}
}
