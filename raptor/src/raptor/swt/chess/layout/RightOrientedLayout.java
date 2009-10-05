package raptor.swt.chess.layout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;

import raptor.swt.SWTUtils;
import raptor.swt.chess.ChessBoard;
import raptor.swt.chess.ChessBoardLayout;

/**
 * This layout adjusts the font sizes to match the viewing area. It displays all
 * labels.
 */
public class RightOrientedLayout extends ChessBoardLayout {
	private static final Log LOG = LogFactory.getLog(RightOrientedLayout.class);

	public static final int NORTH = 0;
	public static final int SOUTH = 1;
	public static final int EAST = 1;
	public static final int WEST = 0;

	public static final int FONT_RESIZE_PERCENTAGE = 90;

	public static final int TOP_LABEL_HEIGHT_PERCENTAGE_OF_SCREEN = 4;
	public static final int[] TOP_LABEL_HEIGHT_MARGIN_PERCENTAGES = { 1, 1, };
	public static final int[] TOP_LABEL_WIDTH_MARGIN_PERCENTAGES = { 1, 1 };

	public static final int BOTTOM_LABEL_HEIGHT_PERCENTAGE_OF_SCREEN = 4;
	public static final int[] BUTTOM_LABEL_HEIGHT_MARGIN_PERCENTAGES = { 1, 1 };
	public static final int[] BOTTOM_LABEL_WIDTH_MARGIN_PERCENTAGES = { 1, 1 };

	public static final int[] BOARD_WIDTH_MARGIN_PERCENTAGES = { 1, 1 };

	public static final int CLOCK_HEIGHT_PERCENTAGE_OF_BOARD_SQUARE = 100;
	public static final int NAME_LABEL_HEIGHT_PERCENTAGE_OF_BOARD_SQUARE = 70;
	public static final int LAG_LABEL_HEIGHT_PERCENTAGE_OF_BOARD_SQUARE = 30;
	public static final int TO_MOVE_INDICATOR_PERCENTAGE_OF_BOARD_SQUARE = 85;

	protected Point boardTopLeft;
	protected Rectangle gameDescriptionLabelRect;
	protected Rectangle currentPremovesLabelRect;
	protected Rectangle openingDescriptionLabelRect;
	protected Rectangle statusLabelRect;
	protected Rectangle topNameLabelRect;
	protected Rectangle bottomNameLabelRect;
	protected Rectangle topClockRect;
	protected Rectangle bottomClockRect;
	protected Rectangle topLagRect;
	protected Rectangle bottomLagRect;
	protected Rectangle topToMoveIndicatorRect;
	protected Rectangle bottomToMoveIndicatorRect;
	protected Point topPieceJailRow1Point;
	protected Point topPieceJailRow2Point;
	protected Point bottomPieceJailRow1Point;
	protected Point bottomPieceJailRow2Point;
	protected int topLabelHeight;
	protected int bottomLabelHeight;
	protected int squareSize;
	protected int boardHeight;
	protected int pieceJailSquareSize;
	protected boolean hasHeightProblem = false;
	protected boolean hasSevereHeightProblem = false;
	protected ControlListener controlListener;

	public RightOrientedLayout(ChessBoard board) {
		super(board);

		board.addControlListener(controlListener = new ControlListener() {

			public void controlMoved(ControlEvent e) {
			}

			public void controlResized(ControlEvent e) {
				setLayoutData();
				adjustLabelFontsAndImages();
			}
		});
	}

	protected void adjustLabelFontsAndImages() {
		board.getGameDescriptionLabel().setFont(
				SWTUtils.getProportionalFont(board.getGameDescriptionLabel()
						.getFont(), 80, topLabelHeight));
		board.getCurrentPremovesLabel().setFont(
				SWTUtils.getProportionalFont(board.getCurrentPremovesLabel()
						.getFont(), 80, topLabelHeight));
		board.getStatusLabel().setFont(
				SWTUtils.getProportionalFont(board.getStatusLabel().getFont(),
						80, bottomLabelHeight));
		board.getOpeningDescriptionLabel().setFont(
				SWTUtils.getProportionalFont(board.getOpeningDescriptionLabel()
						.getFont(), 60, bottomLabelHeight));

		Font nameFont = SWTUtils.getProportionalFont(board
				.getWhiteNameRatingLabel().getFont(), hasHeightProblem ? 60
				: hasSevereHeightProblem ? 50 : 70, topNameLabelRect.height);
		board.getWhiteNameRatingLabel().setFont(nameFont);
		board.getBlackNameRatingLabel().setFont(nameFont);

		Font lagFont = SWTUtils.getProportionalFont(board.getWhiteLagLabel()
				.getFont(), 70, topLagRect.height);
		board.getWhiteLagLabel().setFont(lagFont);
		board.getBlackLagLabel().setFont(lagFont);

		Point nameSize = board.getWhiteNameRatingLabel().computeSize(
				SWT.DEFAULT, SWT.DEFAULT, true);
		Point lagSize = board.getWhiteLagLabel().computeSize(SWT.DEFAULT,
				SWT.DEFAULT, true);

		if (nameSize.y + lagSize.y <= squareSize) {
			System.err.println("****Adjusted name labels****");
			topNameLabelRect.height = nameSize.y;
			bottomNameLabelRect.height = nameSize.y;

			topLagRect.y = topNameLabelRect.y + topNameLabelRect.height;
			topLagRect.height = lagSize.y;
			bottomLagRect.y = bottomNameLabelRect.y
					+ bottomNameLabelRect.height;
			bottomLagRect.height = lagSize.y;
		}

		Font clockFont = SWTUtils.getProportionalFont(board
				.getWhiteClockLabel().getFont(), 80, topClockRect.height);
		board.getWhiteClockLabel().setFont(clockFont);
		board.getBlackClockLabel().setFont(clockFont);
	}

	@Override
	public void dispose() {
		super.dispose();
		LOG.debug("Disposed RightOrientedLayout");
		board.removeControlListener(controlListener);
	}

	@Override
	public String getName() {
		return "Right Oriented";
	}

	@Override
	public int getStyle(Field field) {
		switch (field) {
		case GAME_DESCRIPTION_LABEL:
			return SWT.LEFT;
		case CURRENT_PREMOVE_LABEL:
			return SWT.RIGHT;
		case STATUS_LABEL:
			return SWT.LEFT;
		case OPENING_DESCRIPTION_LABEL:
			return SWT.RIGHT;
		case NAME_RATING_LABEL:
			return SWT.LEFT;
		case CLOCK_LABEL:
			return SWT.LEFT;
		case LAG_LABEL:
			return SWT.LEFT;
		default:
			return SWT.NONE;
		}
	}

	@Override
	protected void layout(Composite composite, boolean flushCache) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("in layout(" + flushCache + ") " + composite.getSize().x
					+ " " + composite.getSize().y);
		}

		if (flushCache) {
			setLayoutData();
			adjustLabelFontsAndImages();
		}

		long startTime = System.currentTimeMillis();

		board.getGameDescriptionLabel().setBounds(gameDescriptionLabelRect);
		board.getCurrentPremovesLabel().setBounds(currentPremovesLabelRect);
		board.getStatusLabel().setBounds(statusLabelRect);
		board.getOpeningDescriptionLabel().setBounds(
				openingDescriptionLabelRect);

		layoutChessBoard(boardTopLeft, squareSize);

		board.getWhiteNameRatingLabel().setBounds(
				board.isWhiteOnTop() ? topNameLabelRect : bottomNameLabelRect);
		board.getWhiteLagLabel().setBounds(
				board.isWhiteOnTop() ? topLagRect : bottomLagRect);
		board.getWhiteClockLabel().setBounds(
				board.isWhiteOnTop() ? topClockRect : bottomClockRect);

		board.getBlackNameRatingLabel().setBounds(
				board.isWhiteOnTop() ? bottomNameLabelRect : topNameLabelRect);
		board.getBlackLagLabel().setBounds(
				board.isWhiteOnTop() ? bottomLagRect : topLagRect);
		board.getBlackClockLabel().setBounds(
				board.isWhiteOnTop() ? bottomClockRect : topClockRect);

		if (board.isWhitePieceJailOnTop()) {
			board.getPieceJailSquares()[WP].setBounds(topPieceJailRow1Point.x,
					topPieceJailRow1Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
			board.getPieceJailSquares()[WN].setBounds(topPieceJailRow1Point.x
					+ pieceJailSquareSize, topPieceJailRow1Point.y,
					pieceJailSquareSize, pieceJailSquareSize);
			board.getPieceJailSquares()[WB].setBounds(topPieceJailRow1Point.x
					+ 2 * pieceJailSquareSize, topPieceJailRow1Point.y,
					pieceJailSquareSize, pieceJailSquareSize);
			board.getPieceJailSquares()[WQ].setBounds(topPieceJailRow2Point.x,
					topPieceJailRow2Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
			board.getPieceJailSquares()[WR].setBounds(topPieceJailRow2Point.x
					+ pieceJailSquareSize, topPieceJailRow2Point.y,
					pieceJailSquareSize, pieceJailSquareSize);
			board.getPieceJailSquares()[WK].setBounds(topPieceJailRow2Point.x
					+ 2 * pieceJailSquareSize, topPieceJailRow2Point.y,
					pieceJailSquareSize, pieceJailSquareSize);

			board.getPieceJailSquares()[BP].setBounds(
					bottomPieceJailRow1Point.x, bottomPieceJailRow1Point.y,
					pieceJailSquareSize, pieceJailSquareSize);
			board.getPieceJailSquares()[BN].setBounds(
					bottomPieceJailRow1Point.x + pieceJailSquareSize,
					bottomPieceJailRow1Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
			board.getPieceJailSquares()[BB].setBounds(
					bottomPieceJailRow1Point.x + 2 * pieceJailSquareSize,
					bottomPieceJailRow1Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
			board.getPieceJailSquares()[BQ].setBounds(
					bottomPieceJailRow2Point.x, bottomPieceJailRow2Point.y,
					pieceJailSquareSize, pieceJailSquareSize);
			board.getPieceJailSquares()[BR].setBounds(
					bottomPieceJailRow2Point.x + pieceJailSquareSize,
					bottomPieceJailRow2Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
			board.getPieceJailSquares()[BK].setBounds(
					bottomPieceJailRow2Point.x + 2 * pieceJailSquareSize,
					bottomPieceJailRow2Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
		} else {
			board.getPieceJailSquares()[BP].setBounds(topPieceJailRow1Point.x,
					topPieceJailRow1Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
			board.getPieceJailSquares()[BN].setBounds(topPieceJailRow1Point.x
					+ pieceJailSquareSize, topPieceJailRow1Point.y,
					pieceJailSquareSize, pieceJailSquareSize);
			board.getPieceJailSquares()[BB].setBounds(topPieceJailRow1Point.x
					+ 2 * pieceJailSquareSize, topPieceJailRow1Point.y,
					pieceJailSquareSize, pieceJailSquareSize);
			board.getPieceJailSquares()[BQ].setBounds(topPieceJailRow2Point.x,
					topPieceJailRow2Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
			board.getPieceJailSquares()[BR].setBounds(topPieceJailRow2Point.x
					+ pieceJailSquareSize, topPieceJailRow2Point.y,
					pieceJailSquareSize, pieceJailSquareSize);
			board.getPieceJailSquares()[BK].setBounds(topPieceJailRow2Point.x
					+ 2 * pieceJailSquareSize, topPieceJailRow2Point.y,
					pieceJailSquareSize, pieceJailSquareSize);

			board.getPieceJailSquares()[WP].setBounds(
					bottomPieceJailRow1Point.x, bottomPieceJailRow1Point.y,
					pieceJailSquareSize, pieceJailSquareSize);
			board.getPieceJailSquares()[WN].setBounds(
					bottomPieceJailRow1Point.x + pieceJailSquareSize,
					bottomPieceJailRow1Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
			board.getPieceJailSquares()[WB].setBounds(
					bottomPieceJailRow1Point.x + 2 * pieceJailSquareSize,
					bottomPieceJailRow1Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
			board.getPieceJailSquares()[WQ].setBounds(
					bottomPieceJailRow2Point.x, bottomPieceJailRow2Point.y,
					pieceJailSquareSize, pieceJailSquareSize);
			board.getPieceJailSquares()[WR].setBounds(
					bottomPieceJailRow2Point.x + pieceJailSquareSize,
					bottomPieceJailRow2Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
			board.getPieceJailSquares()[WK].setBounds(
					bottomPieceJailRow2Point.x + 2 * pieceJailSquareSize,
					bottomPieceJailRow2Point.y, pieceJailSquareSize,
					pieceJailSquareSize);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Layout completed in "
					+ (System.currentTimeMillis() - startTime));
		}
	}

	protected void setLayoutData() {
		int width = board.getSize().x;
		int height = board.getSize().y;

		hasHeightProblem = false;
		hasSevereHeightProblem = false;

		if (width < height) {
			height = width;
			hasHeightProblem = true;
		}

		int topLabelNorthMargin = height
				* TOP_LABEL_HEIGHT_MARGIN_PERCENTAGES[NORTH] / 100;
		topLabelHeight = height * TOP_LABEL_HEIGHT_PERCENTAGE_OF_SCREEN / 100;
		int topLabelSouthMargin = height
				* TOP_LABEL_HEIGHT_MARGIN_PERCENTAGES[SOUTH] / 100;

		int bottomLabelNorthMargin = height
				* BUTTOM_LABEL_HEIGHT_MARGIN_PERCENTAGES[NORTH] / 100;
		bottomLabelHeight = height * BOTTOM_LABEL_HEIGHT_PERCENTAGE_OF_SCREEN
				/ 100;
		int bottomLabelSouthMargin = height
				* BUTTOM_LABEL_HEIGHT_MARGIN_PERCENTAGES[SOUTH] / 100;

		int boardWidthPixelsWest = width * BOARD_WIDTH_MARGIN_PERCENTAGES[WEST]
				/ 100;
		int boardWidthPixelsEast = width * BOARD_WIDTH_MARGIN_PERCENTAGES[EAST]
				/ 100;

		squareSize = (height - bottomLabelSouthMargin - bottomLabelHeight
				- bottomLabelNorthMargin - topLabelSouthMargin - topLabelHeight - topLabelNorthMargin) / 8;

		while (width < (squareSize * 11 + boardWidthPixelsWest + boardWidthPixelsEast)) {
			squareSize -= 2;
			hasSevereHeightProblem = true;
		}

		pieceJailSquareSize = squareSize;

		boardHeight = squareSize * 8;

		int topLabelPixelsWest = width
				* TOP_LABEL_WIDTH_MARGIN_PERCENTAGES[WEST] / 100;
		int topLabelPixesEast = width
				* TOP_LABEL_WIDTH_MARGIN_PERCENTAGES[EAST] / 100;
		int bottonLabelPixelsWest = width
				* BOTTOM_LABEL_WIDTH_MARGIN_PERCENTAGES[WEST] / 100;
		int bottomLabelPixelsEast = width
				* BOTTOM_LABEL_WIDTH_MARGIN_PERCENTAGES[EAST] / 100;

		int gameDescriptionWidth = width / 2 - topLabelPixelsWest;
		int currentPremovesWidth = width / 2 - topLabelPixesEast;
		int gameStatusWidth = width / 2 - bottonLabelPixelsWest;
		int openingDescriptionWidth = width / 2 - bottomLabelPixelsEast;

		int topHeight = topLabelNorthMargin + topLabelHeight
				+ topLabelSouthMargin;

		gameDescriptionLabelRect = new Rectangle(topLabelPixelsWest,
				topLabelNorthMargin, gameDescriptionWidth, topLabelHeight);
		currentPremovesLabelRect = new Rectangle(topLabelPixelsWest
				+ gameDescriptionLabelRect.width, topLabelNorthMargin,
				currentPremovesWidth, topLabelHeight);

		statusLabelRect = new Rectangle(bottonLabelPixelsWest, topHeight + 8
				* squareSize + bottomLabelNorthMargin, gameStatusWidth,
				bottomLabelHeight);
		openingDescriptionLabelRect = new Rectangle(bottonLabelPixelsWest
				+ statusLabelRect.width, topHeight + 8 * squareSize
				+ bottomLabelNorthMargin, openingDescriptionWidth,
				bottomLabelHeight);

		boardTopLeft = new Point(boardWidthPixelsWest, topHeight);

		int toMoveIndicatorX = boardWidthPixelsWest + boardHeight
				+ boardWidthPixelsEast;
		int toMoveIndicatorSide = TO_MOVE_INDICATOR_PERCENTAGE_OF_BOARD_SQUARE / 100;

		int nameLabelStartX = boardWidthPixelsWest + boardHeight
				+ boardWidthPixelsEast + toMoveIndicatorSide;

		Point nameLabelSize = new Point(width - nameLabelStartX, squareSize
				* NAME_LABEL_HEIGHT_PERCENTAGE_OF_BOARD_SQUARE / 100);

		int clockStartX = boardWidthPixelsWest + boardHeight
				+ boardWidthPixelsEast;

		Point clockLabelSize = new Point(width - clockStartX, squareSize
				* CLOCK_HEIGHT_PERCENTAGE_OF_BOARD_SQUARE / 100);

		Point lagLabelSize = new Point(width - nameLabelStartX, squareSize
				* LAG_LABEL_HEIGHT_PERCENTAGE_OF_BOARD_SQUARE / 100);

		int nameStartY = topHeight;
		int bottomHeightStart = nameStartY + 4 * squareSize;

		topToMoveIndicatorRect = new Rectangle(toMoveIndicatorX, nameStartY,
				toMoveIndicatorSide, toMoveIndicatorSide);
		topNameLabelRect = new Rectangle(nameLabelStartX, nameStartY,
				nameLabelSize.x, nameLabelSize.y);
		topLagRect = new Rectangle(nameLabelStartX, nameStartY
				+ topNameLabelRect.height, lagLabelSize.x, lagLabelSize.y);
		topClockRect = new Rectangle(clockStartX, nameStartY + squareSize,
				clockLabelSize.x, clockLabelSize.y);

		bottomToMoveIndicatorRect = new Rectangle(toMoveIndicatorX,
				bottomHeightStart, toMoveIndicatorSide, toMoveIndicatorSide);
		bottomNameLabelRect = new Rectangle(nameLabelStartX, bottomHeightStart,
				nameLabelSize.x, nameLabelSize.y);
		bottomLagRect = new Rectangle(nameLabelStartX, bottomHeightStart
				+ bottomNameLabelRect.height, lagLabelSize.x, lagLabelSize.y);
		bottomClockRect = new Rectangle(clockStartX, bottomHeightStart
				+ squareSize, clockLabelSize.x, clockLabelSize.y);

		topPieceJailRow1Point = new Point(clockStartX, topHeight + 2
				* squareSize);
		topPieceJailRow2Point = new Point(clockStartX, topHeight + 3
				* squareSize);
		bottomPieceJailRow1Point = new Point(clockStartX, topHeight + 6
				* squareSize);
		bottomPieceJailRow2Point = new Point(clockStartX, topHeight + 7
				* squareSize);
	}
}