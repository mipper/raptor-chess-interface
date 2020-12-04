/**
 * New BSD License
 * http://www.opensource.org/licenses/bsd-license.php
 * Copyright 2009-2011 RaptorProject (http://code.google.com/p/raptor-chess-interface/)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the RaptorProject nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package raptor.alias;

import raptor.swt.chat.ChatConsoleController;

public class SetDebugLevelAlias extends RaptorAlias {

	public SetDebugLevelAlias() {
		super(
				"debuglevel",
				"Sets the current debug level of raptor. This will effect the "
						+ "debug messages being stored in $RAPTOR_HOME_DIR/logs/error.log.",
				"'debuglevel [loggerName] [DEBUG | INFO | WARN | ERROR]'. Example: 'debuglevel rootLogger DEBUG'");
	}

	@SuppressWarnings("deprecation")
	@Override
	public RaptorAliasResult apply(ChatConsoleController controller, String command) {
//		if (StringUtils.startsWith(command, "debuglevel")) {
//			RaptorStringTokenizer tok = new RaptorStringTokenizer(command, " ",
//					true);
//			tok.nextToken();
//			String logLevel = null;
//			String loggerName = null;
//
//			if (tok.hasMoreTokens()) {
//				loggerName = tok.nextToken();
//			} else {
//				return new RaptorAliasResult(null, "Invalid syntax: " + command
//						+ "\n" + getUsage());
//			}
//
//			if (tok.hasMoreTokens()) {
//				logLevel = tok.nextToken();
//			} else {
//				return new RaptorAliasResult(null, "Invalid syntax: " + command
//						+ "\n" + getUsage());
//			}
//
//			Logger logger = loggerName.equals("rootLogger") ? Logger
//					.getRootLogger() : Logger.getLogger(loggerName);
//
//			if (logLevel.equals("DEBUG")) {
//				logger.setLevel(Level.DEBUG);
//				((FileAppender) Logger.getRootLogger().getAppender("file"))
//						.setThreshold(Priority.DEBUG);
//			} else if (logLevel.equals("INFO")) {
//				logger.setLevel(Level.DEBUG);
//				((FileAppender) Logger.getRootLogger().getAppender("file"))
//						.setThreshold(Priority.INFO);
//			} else if (logLevel.equals("WARN")) {
//				logger.setLevel(Level.DEBUG);
//				((FileAppender) Logger.getRootLogger().getAppender("file"))
//						.setThreshold(Priority.WARN);
//			} else if (logLevel.equals("ERROR")) {
//				logger.setLevel(Level.DEBUG);
//				((FileAppender) Logger.getRootLogger().getAppender("file"))
//						.setThreshold(Priority.ERROR);
//			} else {
//				return new RaptorAliasResult(null, "Invalid syntax: " + command
//						+ "\n" + getUsage());
//			}
//
//			return new RaptorAliasResult(null, "Log level set to " + logLevel
//					+ " for logger " + loggerName);
//		}
		return null;
	}
}
