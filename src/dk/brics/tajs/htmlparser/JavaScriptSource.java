/*
 * Copyright 2009-2013 Aarhus University
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dk.brics.tajs.htmlparser;

/**
 * JavaScript code snippet with meta-information.
 */
public interface JavaScriptSource {

	/**
	 * JavaScript code embedded in a 'script' element in an HTML file.
	 */
	public static class EmbeddedJavaScriptSource implements JavaScriptSource {

		private final String fileName;
		
		private final String javaScript;
		
		private final int lineNumberOffset; // TODO: also need column number (for the first line)

		/**
		 * Constructs a new code snippet descriptor.
		 */
		public EmbeddedJavaScriptSource(String fileName, String javaScript, int lineNumberOffset) {
			this.fileName = fileName;
			this.javaScript = javaScript;
			this.lineNumberOffset = lineNumberOffset;
		}

		@Override
		public <T> T apply(JavaScriptSourceVisitor<T> v) {
			return v.visit(this);
		}

		@Override
		public String getFileName() {
			return fileName;
		}

		@Override
		public String getJavaScript() {
			return javaScript;
		}

		/**
		 * Returns the starting line number.
		 */
		public int getLineNumberOffset() {
			return lineNumberOffset;
		}

		@Override
		public String toString() {
			return String.format("%s(fileName: %s, lineNumber: %s)", getClass().getSimpleName(), fileName, lineNumberOffset);
		}
	}

	/**
	 * JavaScript code embedded in a "javascript:" event handler attribute in an HTML file.
	 */
	public static class EventHandlerJavaScriptSource implements JavaScriptSource {
		
		private final String fileName;

		private final String javaScript;

		private final int lineNumberOffset;
		
		private final String eventName;

		/**
		 * Constructs a new code snippet descriptor.
		 */
		public EventHandlerJavaScriptSource(String fileName, String javaScript, int lineNumber, String eventName) {
			this.fileName = fileName;
			this.javaScript = javaScript;
			this.lineNumberOffset = lineNumber;
			this.eventName = eventName;
		}

		@Override
		public <T> T apply(JavaScriptSourceVisitor<T> v) {
			return v.visit(this);
		}

		/**
		 * Returns the event name.
		 */
		public String getEventName() {
			return eventName;
		}

		@Override
		public String getFileName() {
			return fileName;
		}

		@Override
		public String getJavaScript() {
			return javaScript;
		}

		/**
		 * Returns the starting line number.
		 */
		public int getLineNumberOffset() {
			return lineNumberOffset;
		}

		@Override
		public String toString() {
			return String.format("%s(fileName: %s, lineNumber: %s)", getClass().getSimpleName(), fileName, lineNumberOffset);
		}
	}

	/**
	 * JavaScript code referenced from a 'script' element in an HTML file.
	 */
	public static class ExternalJavaScriptSource implements JavaScriptSource {

		private final String fileName;
		
		private final String javaScript;

		/**
		 * Constructs a new code snippet descriptor.
		 */
		public ExternalJavaScriptSource(String fileName, String javaScript) {
			this.fileName = fileName;
			this.javaScript = javaScript;
		}

		@Override
		public <T> T apply(JavaScriptSourceVisitor<T> v) {
			return v.visit(this);
		}

		@Override
		public String getFileName() {
			return fileName;
		}

		@Override
		public String getJavaScript() {
			return javaScript;
		}

		@Override
		public String toString() {
			return String.format("%s(fileName: %s)", getClass().getSimpleName(), fileName);
		}

	}

	/**
	 * Visitor interface for the different kinds of JavaScript source code.
	 */
	public interface JavaScriptSourceVisitor<T> {

		T visit(EmbeddedJavaScriptSource s);

		T visit(EventHandlerJavaScriptSource s);

		T visit(ExternalJavaScriptSource s);
	}

	public <T> T apply(JavaScriptSourceVisitor<T> v);

	/**
	 * Returns the file name associated with the code.
	 */
	public String getFileName();

	/**
	 * Returns the code.
	 */
	public String getJavaScript();
}
