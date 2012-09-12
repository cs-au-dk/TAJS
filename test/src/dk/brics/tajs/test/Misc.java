package dk.brics.tajs.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import dk.brics.tajs.Main;
import dk.brics.tajs.analysis.Analysis;
import dk.brics.tajs.util.AnalysisException;


public class Misc {

	static ByteArrayOutputStream os;

	static PrintStream ps;

	static PrintStream old_out;
	
	static PrintStream old_err;
	
	static String method_name;
	
	public static void init() {
        Main.initLogging(); // TODO: different log4j configuration for tests?
		StackTraceElement[] s = Thread.currentThread().getStackTrace();
		method_name = s[2].getMethodName();
		System.out.println("=========== " + method_name + " ===========");
	}

	public static void run(String[] args) throws AnalysisException {
		Analysis a = Main.init(args, null);
		if (a == null)
			throw new AnalysisException("Error during initialization");
		Main.run(a);
		Main.reset();
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
				System.out.println(filename + " generated");
			} catch (Exception f) {
				System.err.println("Unable to write " + filename + ", " + f.getMessage());
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
			ps = new DumpingPrintStream(os, false, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AnalysisException(e);
		}
		restoreSystem();
		old_out = System.out;
		old_err = System.err;
		System.setOut(ps);
		System.setErr(ps);
	}
	
	public static void checkSystemOutput() {
		ps.close();
		restoreSystem();
		try {
			Misc.checkOutput(fix(os.toString("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			throw new AnalysisException(e);
		}
	}
	
	static private void restoreSystem() {
		if (old_out != null)
			System.setOut(old_out);
		if (old_err != null)
			System.setErr(old_err);
		old_out = null;
		old_err = null;
	}
	
	static class DumpingPrintStream extends PrintStream {
		
		public DumpingPrintStream(OutputStream out, boolean autoFlush, String encoding) throws UnsupportedEncodingException {
			super(out, autoFlush, encoding);
		}

		@Override
		public void write(byte[] buf, int off, int len)  { // TODO: need to override other methods?
			super.write(buf, off, len);
			old_out.write(buf, off, len);
		}

	}
}
