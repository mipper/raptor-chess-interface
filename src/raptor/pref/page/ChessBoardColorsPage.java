/**
 * New BSD License
 * http://www.opensource.org/licenses/bsd-license.php
 * Copyright (c) 2009, RaptorProject (http://code.google.com/p/raptor-chess-interface/)
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of the RaptorProject nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package raptor.pref.page;

import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;

import raptor.Raptor;
import raptor.pref.PreferenceKeys;

public class ChessBoardColorsPage extends FieldEditorPreferencePage {
	public ChessBoardColorsPage() {
		// Use the "flat" layout
		super(GRID);
		setTitle("Colors");
		setPreferenceStore(Raptor.getInstance().getPreferences());
	}

	@Override
	protected void createFieldEditors() {
		ColorFieldEditor defaultMessages = new ColorFieldEditor(
				PreferenceKeys.BOARD_BACKGROUND_COLOR, "Background Color:",
				getFieldEditorParent());
		addField(defaultMessages);

		addField(new ColorFieldEditor(
				PreferenceKeys.BOARD_PIECE_JAIL_BACKGROUND_COLOR,
				"Piece Jail Background Color:", getFieldEditorParent()));

		ColorFieldEditor coordinatesColor = new ColorFieldEditor(
				PreferenceKeys.BOARD_COORDINATES_COLOR, "Coordinates Color:",
				getFieldEditorParent());
		addField(coordinatesColor);

		ColorFieldEditor lagLabelColor = new ColorFieldEditor(
				PreferenceKeys.BOARD_CONTROL_COLOR, "Label Color:",
				getFieldEditorParent());
		addField(lagLabelColor);

		ColorFieldEditor lagOver20LabelColor = new ColorFieldEditor(
				PreferenceKeys.BOARD_LAG_OVER_20_SEC_COLOR,
				"Lag Over 20 Seconds Color:", getFieldEditorParent());
		addField(lagOver20LabelColor);

		addField(new ColorFieldEditor(
				PreferenceKeys.BOARD_PIECE_JAIL_LABEL_COLOR,
				"Piece Jail/Drop Square Number Color:", getFieldEditorParent()));

		addField(new ColorFieldEditor(PreferenceKeys.BOARD_ACTIVE_CLOCK_COLOR,
				"Active Clock Color:", getFieldEditorParent()));

		addField(new ColorFieldEditor(
				PreferenceKeys.BOARD_INACTIVE_CLOCK_COLOR,
				"Inactive Clock Color:", getFieldEditorParent()));
	}
}