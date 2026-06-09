/*
 * Copyright © 2026 Knowledge Graphlet / IKE Network
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package network.ike.komet.example.rules;

import dev.ikm.komet.rules.actions.AbstractActionSuggested;
import dev.ikm.tinkar.coordinate.edit.EditCoordinate;
import dev.ikm.tinkar.coordinate.edit.EditCoordinateRecord;
import dev.ikm.tinkar.coordinate.view.calculator.ViewCalculator;
import dev.ikm.tinkar.entity.EntityVersion;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

/**
 * A rule-generated context-menu action emitted by {@link ExampleRules}. Rendered into the
 * context menu by the existing rule pipeline (a {@code ConsequenceAction} →
 * {@code ActionUtils.createMenuItem}); when chosen, it shows an informational alert.
 *
 * <p>This is the minimal shape of a rule action: extend {@link AbstractActionSuggested}
 * (a "suggested" action the user can pick from a menu), pass the menu label to {@code super},
 * and implement {@link #doAction(ActionEvent, EditCoordinateRecord)}. The active view is
 * available via {@link #viewCalculator()} — e.g. to resolve the component's description or
 * walk its version history.
 */
public final class ExampleSuggestedAction extends AbstractActionSuggested {

    private final int nid;

    /**
     * Creates the action for a focused component version.
     *
     * @param version        the focused entity version (its component nid is captured)
     * @param viewCalculator the active view (name, identifier, STAMP, history)
     * @param editCoordinate the edit coordinate (unused here; required by the base)
     */
    public ExampleSuggestedAction(EntityVersion version, ViewCalculator viewCalculator, EditCoordinate editCoordinate) {
        super("Example: log this concept", viewCalculator, editCoordinate);
        this.nid = version.nid();
    }

    /**
     * {@inheritDoc}
     *
     * <p>Demonstration only: shows an informational alert naming the component nid. A real
     * action would use {@link #viewCalculator()} to read the component and do useful work.
     *
     * @param actionEvent   the JavaFX action event
     * @param editCoordinate the resolved edit coordinate record
     */
    @Override
    public void doAction(ActionEvent actionEvent, EditCoordinateRecord editCoordinate) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "Example rule pack fired for component nid=" + nid + ".");
        alert.setHeaderText(null);
        alert.show();
    }
}
