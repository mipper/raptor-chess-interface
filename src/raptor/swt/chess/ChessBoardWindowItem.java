package raptor.swt.chess;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import raptor.Quadrant;
import raptor.Raptor;
import raptor.RaptorWindowItem;
import raptor.pref.PreferenceKeys;
import raptor.swt.ItemChangedListener;

public class ChessBoardWindowItem implements RaptorWindowItem {
	static final Log LOG = LogFactory.getLog(ChessBoardWindowItem.class);

	ChessBoard board;
	ChessBoardController controller;

	public ChessBoardWindowItem(ChessBoardController controller) {
		this.controller = controller;

		// When a controller change occurs it should always fire an
		// itemStateChange.
		// This listener will pick up the change and swap out the controllers.
		controller.addItemChangedListener(new ItemChangedListener() {
			public void itemStateChanged() {
				ChessBoardWindowItem.this.controller = board.getController();
			}
		});
	}

	public void addItemChangedListener(ItemChangedListener listener) {
		controller.addItemChangedListener(listener);
	}

	public boolean confirmClose() {
		return controller.confirmClose();
	}

	public boolean confirmQuadrantMove() {
		return true;
	}

	public void dispose() {
		board.dispose();
	}

	public Composite getControl() {
		return board;
	}

	public Image getImage() {
		return null;
	}

	public Quadrant getPreferredQuadrant() {
		return Raptor.getInstance().getPreferences().getCurrentLayoutQuadrant(
				PreferenceKeys.GAME_QUADRANT);
	}

	public String getTitle() {
		return controller.getTitle();
	}

	public Control getToolbar(Composite parent) {
		return controller.getToolbar(parent);
	}

	public void init(Composite parent) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Initing ChessBoardWindowItem");
		}
		long startTime = System.currentTimeMillis();
		board = new ChessBoard(parent);
		board.setController(controller);
		controller.setBoard(board);
		board.createControls();
		controller.init();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Inited window item in "
					+ (System.currentTimeMillis() - startTime));
		}
	}

	public void onActivate() {
		board.getDisplay().asyncExec(new Runnable() {
			public void run() {
				controller.onActivate();
			}
		});
	}

	public void onPassivate() {
		board.getDisplay().asyncExec(new Runnable() {
			public void run() {
				controller.onPassivate();
			}
		});
	}

	public void removeItemChangedListener(ItemChangedListener listener) {
		controller.removeItemChangedListener(listener);
	}
}
