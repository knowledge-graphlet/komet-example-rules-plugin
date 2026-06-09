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

import dev.ikm.komet.framework.rulebase.RuleProvider;

import java.util.List;

/**
 * Contributes this pack's rule classes to the rule engine. Discovered via
 * {@code PluggableService.load(RuleProvider.class)} once this jar is present in the Komet
 * image's {@code plugins/} directory, and registered with
 * {@code provides dev.ikm.komet.framework.rulebase.RuleProvider with ...} in this module's
 * {@code module-info}.
 *
 * <p>Because the rule and action live in the plugin, they can be updated by replacing the
 * jar — no komet release required.
 */
public final class ExampleRuleProvider implements RuleProvider {

    /**
     * Public no-arg constructor required for {@link java.util.ServiceLoader}.
     */
    public ExampleRuleProvider() {
        // no-op
    }

    /**
     * {@inheritDoc}
     *
     * @return the annotated rule classes this pack contributes
     */
    @Override
    public List<Class<?>> ruleClasses() {
        return List.of(ExampleRules.class);
    }
}
