package dk.brics.tajs.test;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.util.AnalysisException;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;

import java.io.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Misc {

    private static Logger log = Logger.getLogger(Misc.class);

    static ByteArrayOutputStream os;

	static PrintStream ps;

	static String method_name;
	
	public static void init() {
        Main.initLogging(); // TODO: different log4j configuration for tests?
		StackTraceElement[] s = Thread.currentThread().getStackTrace();
		method_name = s[2].getMethodName();
		log.info("=========== " + method_name + " ===========");
	}

	public static void run(String[] args) throws AnalysisException {
		try {
			Analysis a = Main.init(args, null);
			if (a == null)
				throw new AnalysisException("Error during initialization");
			Main.run(a);
			Main.reset();
		} catch (AnalysisException e) {
			log.info(e.getMessage());
			throw e;
		}
	}

	@SuppressWarnings("resource")
	public static void checkOutput(String s) {
		s = s.replace(System.getProperty("line.separator"), "\n").replace("\r\n", "\n");
		String filename = "test/expected/" + method_name + ".out";
		try {
			FileInputStream in = new FileInputStream(filename);
			try {
				byte[] b = new byte[in.available()];
				in.read(b);
				String sb = new String(b, "UTF-8");
				sb = sb.replace("\r\n", "\n");
			    assertEquals(sb, s);
			} finally {
				in.close();
			}
		} catch (FileNotFoundException e) {
			try {
				FileOutputStream out = new FileOutputStream(filename);
				try {
					out.write(s.getBytes("UTF-8"));
				} finally {
					out.close();
				}
				log.info(filename + " generated");
			} catch (Exception f) {
				log.error("Unable to write " + filename + ", " + f.getMessage());
				System.exit(-1);
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}				
	}

	public static String fix(String s) {
		return s.replaceAll("\r\n", "\n");
	}
	
	public static void captureSystemOutput() {
		os = new ByteArrayOutputStream();
		try {
			ps = new PrintStream(os, false, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AnalysisException(e);
		}
		Logger rootlogger = Logger.getRootLogger();
		Appender old = rootlogger.getAppender("test");
		if (old != null)
			rootlogger.removeAppender(old);
		Appender a = new WriterAppender(new PatternLayout("%m%n"), ps);
		a.setName("test");
		rootlogger.addAppender(a);
	}
	
	public static void checkSystemOutput() {
		ps.close();
		try {
			Misc.checkOutput(fix(os.toString("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new AnalysisException(e);
		}
	}
	
	public static void runSource(String... src) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < src.length; i++) {
			sb.append(src[i] + "\n");
		}
		final File file;
		try {
			file = File.createTempFile("temp-source-file", ".js");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try (PrintWriter writer = new PrintWriter(file)) {
			writer.write(sb.toString());
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		String[] args = new String[] { file.getPath() };
		Misc.run(args);
		file.deleteOnExit();
	}
}
