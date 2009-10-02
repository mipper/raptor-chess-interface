package raptor.game;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SuicideGame extends Game {
	private static final Log LOG = LogFactory.getLog(SuicideGame.class);

	public SuicideGame() {
		setType(Type.SUICIDE);
	}

	@Override
	public PriorityMoveList getLegalMoves() {
		PriorityMoveList result = getPseudoLegalMoves();

		LOG.debug("Possible Moves = "
				+ java.util.Arrays.toString(result.asArray()));

		boolean containsCaptures = false;
		for (int i = 0; i < result.getHighPrioritySize(); i++) {
			Move move = result.getHighPriority(i);
			forceMove(move);

			if (!isLegalPosition()) {
				result.removeHighPriority(i);
				i--;
			} else {
				if (move.isCapture()) {
					containsCaptures = true;
				}
			}

			rollback();
		}

		LOG.debug("containsCapture = " + containsCaptures);

		for (int i = 0; i < result.getLowPrioritySize(); i++) {
			if (containsCaptures) {
				result.removeLowPriority(i);
				i--;
				continue;
			} // remove all, since there are no non-capture legal moves if can
			// capture

			Move move = result.getLowPriority(i);
			forceMove(move);

			if (!isLegalPosition()
					|| (move.isCastleKSide() || move.isCastleQSide())) { // castling
				// not
				// allowed in
				// suicide
				result.removeLowPriority(i);
				i--;
			}

			rollback();
		}

		LOG.debug("Possible Moves = "
				+ java.util.Arrays.toString(result.asArray()));

		return result;
	}

	@Override
	public boolean isLegalPosition() {
		return true;
	}
}
