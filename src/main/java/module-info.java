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

/**
 * Minimal example of a plugin-contributed Evrete rule pack.
 *
 * <p>The {@link dev.ikm.komet.framework.rulebase.RuleProvider} registered below is
 * discovered by {@code EvreteRulesService} via {@code PluggableService.load(RuleProvider.class)}
 * when this jar sits in the Komet image's {@code plugins/} directory. Its rule classes are
 * merged into the rule knowledge as precompiled {@code JAVA-CLASS} rules — no runtime
 * compiler is involved — so the rules (and the menu actions they generate) can be updated
 * by replacing this jar, without a new komet release. See IKE-Network/ike-issues#626.
 */
module komet.example.rules {
    // Author surface: RulesBase (+ @RuleElement predicates) and AbstractAction*.
    // Brings the framework fact types, the evrete-dsl annotations, and tinkar
    // coordinate/entity transitively. Supplied by the boot layer at runtime.
    requires dev.ikm.komet.rules;
    // The Evrete DSL annotations (@RuleSet/@Rule/@Where/@MethodPredicate).
    requires org.evrete.dsl.java;
    // EntityVersion — the fact subject the example rule matches.
    requires dev.ikm.tinkar.entity;
    // The example action shows a JavaFX Alert.
    requires javafx.controls;

    // The single contribution point: EvreteRulesService picks this up via ServiceLoader.
    provides dev.ikm.komet.framework.rulebase.RuleProvider
            with network.ike.komet.example.rules.ExampleRuleProvider;

    // Evrete reads the rule class reflectively (MethodHandles). The rule package must be
    // opened UNQUALIFIED. A qualified `opens … to org.evrete.dsl.java` is NOT sufficient and
    // throws IllegalAccessException when the engine unreflects the rule method — verified in
    // komet-claude-plugin. (komet/rules itself sidesteps this by being an `open module`.)
    opens network.ike.komet.example.rules;
}
