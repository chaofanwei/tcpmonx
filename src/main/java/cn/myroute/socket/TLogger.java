package cn.myroute.socket;

import java.io.IOException;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TLogger {
	static Logger log = Logger.getLogger("lavasoft");

	static{
		log.setLevel(Level.INFO);

		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
	//	log.addHandler(consoleHandler);
		FileHandler fileHandler;
		try {
			fileHandler = new FileHandler("log.log");
			fileHandler.setLevel(Level.INFO);
			fileHandler.setFormatter(new MyLogHander());
			log.addHandler(fileHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void log(String msg) {
		log.info(msg);
	}


	public static void main(String[] args) throws IOException {
		log.info("aaabbbb");
		log.info("dddd");
	}
	
}

class MyLogHander extends Formatter {
	@Override
	public String format(LogRecord record) {
		return new Date(record.getMillis()).toLocaleString() 
				 + " ," + record.getLevel() + ":"
				+ record.getMessage() + " , "+ record.getSourceClassName() + "\r\n";
	}

}