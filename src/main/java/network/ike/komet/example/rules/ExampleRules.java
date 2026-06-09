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

import dev.ikm.komet.framework.performance.impl.ObservationRecord;
import dev.ikm.komet.rules.annotated.RulesBase;
import dev.ikm.tinkar.entity.EntityVersion;
import org.evrete.dsl.annotation.MethodPredicate;
import org.evrete.dsl.annotation.Rule;
import org.evrete.dsl.annotation.RuleSet;
import org.evrete.dsl.annotation.Where;

/**
 * Example rule pack: a single rule that offers a context-menu action whenever a concept is
 * focused. It reuses the {@link RulesBase} {@code @RuleElement} predicates
 * ({@link RulesBase#isComponentFocused(ObservationRecord) isComponentFocused} and
 * {@link RulesBase#isConceptVersion(ObservationRecord) isConceptVersion}) as the rule's
 * left-hand side — no literal/string conditions, so nothing is compiled at runtime.
 *
 * <p>Authoring rules:
 * <ul>
 *   <li>Extend {@link RulesBase} and annotate the class with {@code @RuleSet}.</li>
 *   <li>Each rule is a {@code public void} method annotated with {@code @Rule} and
 *       {@code @Where(methods = …)}; the parameter is the matched fact.</li>
 *   <li>Compose conditions from {@code RulesBase}'s {@code @RuleElement} predicate methods
 *       referenced by name in {@code @MethodPredicate}. Add your own predicate methods here
 *       (annotated {@code @RuleElement}) for pack-specific conditions.</li>
 *   <li>In the body, build a {@code GeneratedAction} and call
 *       {@code addGeneratedActions(…)} (or {@code addConsequenceMenu(…)} for a raw menu).</li>
 *   <li>Compile with {@code -parameters} and open this package unqualified (see module-info).</li>
 * </ul>
 */
@RuleSet("Example rule pack")
public class ExampleRules extends RulesBase {

    /**
     * Offers the example action whenever a concept version is the focused component.
     *
     * @see RulesBase#isComponentFocused(ObservationRecord)
     * @see RulesBase#isConceptVersion(ObservationRecord)
     * @param $observation the focused-component observation fact
     */
    @Rule("Concept focused — offer example action")
    @Where(methods = {
            @MethodPredicate(method = "isComponentFocused", args = {"$observation"}),
            @MethodPredicate(method = "isConceptVersion", args = {"$observation"})
    })
    public void conceptFocusedExample(ObservationRecord $observation) {
        if ($observation.subject() instanceof EntityVersion entityVersion) {
            addGeneratedActions(new ExampleSuggestedAction(entityVersion, calculator(), editCoordinate()));
        }
    }
}
