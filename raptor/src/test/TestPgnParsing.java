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

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import raptor.chess.Game;
import raptor.chess.MoveList;
import raptor.chess.Result;
import raptor.chess.pgn.ListMaintainingPgnParserListener;
import raptor.chess.pgn.Nag;
import raptor.chess.pgn.PgnParser;
import raptor.chess.pgn.PgnParserError;
import raptor.chess.pgn.PgnParserListener;
import raptor.chess.pgn.SimplePgnParser;
import raptor.chess.pgn.StreamingPgnParser;
import raptor.chess.pgn.chesspresso.ChesspressoPgnListener;
import raptor.chess.pgn.chesspresso.ChesspressoPgnParser;

public class TestPgnParsing {

	public static class LoggingPgnParserListener implements PgnParserListener {

		public void onAnnotation(PgnParser parser, String annotation) {
			System.out.println("onAnotation (" + parser.getLineNumber() + ") "
					+ annotation);
		}

		public void onGameEnd(PgnParser parser, Result result) {
			System.out.println("onGameEnd (" + parser.getLineNumber() + ") "
					+ result.name());
		}

		public void onGameStart(PgnParser parser) {
			System.out.println("onGameStart (" + parser.getLineNumber() + ") ");

		}

		public void onHeader(PgnParser parser, String headerName,
				String headerValue) {
			System.out.println("onHeader (" + parser.getLineNumber() + ") "
					+ headerName + " " + headerValue);
		}

		public void onMoveNag(PgnParser parser, Nag nag) {
			System.out.println("onMoveNag (" + parser.getLineNumber() + ") "
					+ nag.name());
		}

		public void onMoveNumber(PgnParser parser, int moveNumber) {
			System.out.println("onMoveNumber (" + parser.getLineNumber() + ") "
					+ moveNumber);
		}

		public void onMoveSublineEnd(PgnParser parser) {
			System.out.println("onMoveSublineEnd (" + parser.getLineNumber()
					+ ") ");
		}

		public void onMoveSublineStart(PgnParser parser) {
			System.out.println("onMoveSublineStart (" + parser.getLineNumber()
					+ ") ");
		}

		public void onMoveWord(PgnParser parser, String word) {
			System.out.println("onMoveWord (" + parser.getLineNumber() + ") "
					+ word);
		}

		public void onUnknown(PgnParser parser, String unknown) {
			System.out.println("onUnknown (" + parser.getLineNumber() + ") "
					+ unknown);

		}
	}

	public static class TimingPgnParserListener implements PgnParserListener {
		long lastStartTime = 0;

		public void onAnnotation(PgnParser parser, String annotation) {
		}

		public void onGameEnd(PgnParser parser, Result result) {
			System.out.println("onGameEnd duration="
					+ (System.currentTimeMillis() - lastStartTime));
		}

		public void onGameStart(PgnParser parser) {
			lastStartTime = System.currentTimeMillis();
		}

		public void onHeader(PgnParser parser, String headerName,
				String headerValue) {
		}

		public void onMoveNag(PgnParser parser, Nag nag) {
		}

		public void onMoveNumber(PgnParser parser, int moveNumber) {
		}

		public void onMoveSublineEnd(PgnParser parser) {
		}

		public void onMoveSublineStart(PgnParser parser) {
		}

		public void onMoveWord(PgnParser parser, String word) {
		}

		public void onUnknown(PgnParser parser, String unknown) {
		}
	}

	public static final String[] PGN_TEST_FILES = new String[] {// "error.pgn"};//,
	"nestedsublines.pgn" };// , "test2.pgn" };

	@Test
	public void speedTest() throws Exception {
		String pgn = pgnFileAsString("error.pgn");
		SimplePgnParser parser = new SimplePgnParser(pgn);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new LoggingPgnParserListener());
		parser.addPgnParserListener(listener);
		parser.parse();
	}

	@Test
	public void testAtomic() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/atomic.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);

		long startTime = System.currentTimeMillis();
		parser.parse();

		System.err.println("Parsed " + listener.getGames().size() + " games "
				+ " in " + (System.currentTimeMillis() - startTime) + "ms");

		System.err.println(listener.getErrors());
	}

	@Test
	public void testAtomicNotInCheckIfOppKingExplodes() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/atomicTest1.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);

		parser.parse();
		System.err.println(listener.getGames().get(0));
		listener.getGames().get(0).makeSanMove("e5");
	}

	@Test
	public void testAtomicQxf2() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/atomicTest2.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);

		parser.parse();
		System.err.println(listener.getGames().get(0));
		listener.getGames().get(0).makeSanMove("Qxf2#");
	}

	@Test
	public void testCrazyhosueFile() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/crazyhouseGames.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);

		long startTime = System.currentTimeMillis();
		parser.parse();

		System.err.println("Parsed " + listener.getGames().size() + " games "
				+ " in " + (System.currentTimeMillis() - startTime) + "ms");

		System.err.println(listener.getErrors());
	}

	@Test
	public void testFischerRandom() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/wildFrGames.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);
		long startTime = System.currentTimeMillis();
		parser.parse();
		System.err.println("Parsed " + listener.getGames().size() + " games "
				+ " in " + (System.currentTimeMillis() - startTime) + "ms");

		System.err.println(listener.getErrors());

		performRollbackTest(listener.getGames());
	}

	@Test
	public void testfr3() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/enpassantfr.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);

		long startTime = System.currentTimeMillis();
		parser.parse();

		System.err.println("Parsed " + listener.getGames().size() + " games "
				+ " in " + (System.currentTimeMillis() - startTime) + "ms");

		System.err.println(listener.getErrors());

		performRollbackTest(listener.getGames());
	}

	@Test
	public void testFRKingc8() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/frTest2.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);

		parser.parse();
		System.err.println(listener.getGames().get(0));
		System.err.println(listener.getGames().get(0));
		listener.getGames().get(0).makeSanMove("Kc8");
	}

	@Test
	public void testFRLongCastle() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/frTest1.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);

		parser.parse();
		System.err.println(listener.getGames().get(0));
		System.err.println(listener.getGames().get(0));
		listener.getGames().get(0).makeSanMove("O-O-O");
	}

	@Test
	public void testLargeFile() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/Alekhine4Pawns.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);

		long startTime = System.currentTimeMillis();
		parser.parse();

		ArrayList<Game> games = listener.getGames();

		System.err.println("Parsed " + listener.getGames().size() + " games "
				+ " in " + (System.currentTimeMillis() - startTime) + "ms");

		performRollbackTest(games);
	}

	@Test
	public void testLosersFile() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/losersgames.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);

		long startTime = System.currentTimeMillis();
		parser.parse();

		System.err.println("Parsed " + listener.getGames().size() + " games "
				+ " in " + (System.currentTimeMillis() - startTime) + "ms");

		System.err.println(listener.getErrors());
	}

	@Test
	public void testSuicideFile() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/suicidegames.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);

		long startTime = System.currentTimeMillis();
		parser.parse();

		System.err.println("Parsed " + listener.getGames().size() + " games "
				+ " in " + (System.currentTimeMillis() - startTime) + "ms");

		System.err.println(listener.getErrors());
	}



	@Test
	public void testChesspresso() throws Exception {
		ChesspressoPgnParser parser = new ChesspressoPgnParser(new FileReader(
				"projectFiles/test/ficsgames_search_484289.pgn"));
		ChesspressoPgnListener listener = new ChesspressoPgnListener();
		parser.addPgnParserListener(listener);

		long startTime = System.currentTimeMillis();
		parser.parse();

		ArrayList<chesspresso.game.Game> gms = listener.getGames();
		ArrayList<Game> games = new ArrayList<Game>(gms.size());
		for (chesspresso.game.Game g: gms) {
			games.add(ChesspressoPgnParser.convertToRaptorGame(g));
		}

		System.err.println("Parsed " + listener.getGames().size() + " games "
				+ " in " + (System.currentTimeMillis() - startTime) + "ms");

		performRollbackTest(games);
	}

	@Test
	public void testTestFiles() throws Exception {
		for (String element : PGN_TEST_FILES) {
			String pgn = pgnFileAsString(element);
			SimplePgnParser parser = new SimplePgnParser(pgn);
			ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();

			// parser.addPgnParserListener(new LoggingPgnParserListener());
			parser.addPgnParserListener(new TimingPgnParserListener());
			parser.addPgnParserListener(listener);

			// long time = System.currentTimeMillis();
			parser.parse();

			System.out.println("Errors:");
			for (PgnParserError error : listener.getErrors()) {
				System.out.println(error);
			}
			System.out.println("\n\nGames:");
			for (Game game : listener.getGames()) {
				System.out.println(game);
				System.out.println(game.toPgn());
			}
		}
	}

	@Test
	public void testWild5File() throws Exception {
		StreamingPgnParser parser = new StreamingPgnParser(new FileReader(
				"projectFiles/test/wild5games.pgn"), Integer.MAX_VALUE);
		ListMaintainingPgnParserListener listener = new ListMaintainingPgnParserListener();
		parser.addPgnParserListener(new TimingPgnParserListener());
		parser.addPgnParserListener(listener);

		long startTime = System.currentTimeMillis();
		parser.parse();

		System.err.println("Parsed " + listener.getGames().size() + " games "
				+ " in " + (System.currentTimeMillis() - startTime) + "ms");

		System.err.println(listener.getErrors());
	}

	/**
	 * Rolls back all the moves, does a to string at each move on the game
	 * (which gens legals), and tries to maek all the moves. This is slow but a
	 * great test.
	 */
	protected void performRollbackTest(List<Game> games) {
		for (int g = 0; g < games.size(); g++) {
			Game game = games.get(g);
			long gameStart = System.currentTimeMillis();
			MoveList moveList = game.getMoveList().deepCopy();
			while (game.getMoveList().getSize() > 0) {
				game.rollback();
			}
			for (int i = 0; i < moveList.getSize(); i++) {
				Assert.assertTrue(i + "/" + games.size() + " Move "
						+ moveList.get(i) + " was illegal\n" + game, game
						.move(moveList.get(i)));
			}
			System.err.println(g + "/" + games.size()
					+ " performRollbackTest duration="
					+ (System.currentTimeMillis() - gameStart));
		}
	}

	private String pgnFileAsString(String fileName) throws Exception {
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = new BufferedReader(new FileReader(
				"projectFiles/test/" + fileName));

		String currentLine = reader.readLine();
		while (currentLine != null) {
			builder.append(currentLine + "\n");
			currentLine = reader.readLine();
		}

		return builder.toString();
	}

}
