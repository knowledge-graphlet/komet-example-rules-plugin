# komet-example-rules-plugin

A minimal, copy-paste template for a **plugin-contributed Evrete rule pack**: add or update
Komet rules — and the context-menu actions they generate — by dropping a jar into the Komet
image's `plugins/` directory, **without a komet release and without any runtime compiler**.

See [IKE-Network/ike-issues#626](https://github.com/IKE-Network/ike-issues/issues/626) for
the design decision and the rule-pack compatibility contract. For a full-featured real-world
example, see `ZulipRuleProvider` in `komet-claude-plugin` (#620).

## How it works

1. `EvreteRulesService` constructs the rule engine with `DISABLE_LITERAL_DATA=true` and imports
   rules as precompiled `JAVA-CLASS` — so the in-JVM Java compiler is never invoked.
2. At startup it also calls `PluggableService.load(RuleProvider.class)` and imports the classes
   from every discovered `RuleProvider`, each in isolation (a bad pack loses its menu items; the
   engine keeps running).
3. `App.init()` builds a JPMS `ModuleLayer` over `plugins/`, parented by the boot layer — so a
   pack can `requires` anything already in the image (`dev.ikm.komet.rules`, `org.evrete.dsl.java`,
   tinkar). This jar's `provides … RuleProvider` is then visible to step 2.

No literal/string rules, no `javax.tools.JavaCompiler`, no `module-jars`.

## What's here

| File | Role |
|------|------|
| `ExampleRuleProvider` | The SPI entry point — returns the rule classes. |
| `ExampleRules` | One `@Rule` (extends `RulesBase`), fires on a focused concept, reusing `RulesBase` `@RuleElement` predicates as its `@Where(methods = …)`. |
| `ExampleSuggestedAction` | The rule-generated menu action (extends `AbstractActionSuggested`). |
| `module-info.java` | `provides RuleProvider`, `requires` the author surface, `opens` the rule package **unqualified**. |

## Three things that bite you (all handled here)

1. **Compile with `-parameters`.** `@MethodPredicate(args = {"$observation"})` binds facts to
   rule-method parameters *by name*; without parameter names in the bytecode the rule won't bind.
   (Set in `pom.xml`.)
2. **`opens` the rule package UNQUALIFIED.** Evrete reads the rule class via `MethodHandles`. A
   qualified `opens … to org.evrete.dsl.java` is **not** sufficient — it throws
   `IllegalAccessException` when the engine unreflects the rule method. Use a bare
   `opens <pkg>;` (or an `open module`).
3. **Build against a compatible `komet-bom`.** The pack binds to `RulesBase` / `AbstractAction*` /
   framework fact types at a specific version. Build against a `komet-bom` matching (same major)
   the komet image you deploy into — set `komet-bom.version` in `pom.xml`.

## Build

```bash
# from this directory, using the workspace's Maven 4 (or the wrapper)
mvn clean package
```

Produces `target/komet-example-rules-plugin-1-SNAPSHOT.jar`.

## Deploy

Drop the jar into the Komet runtime image's `plugins/` directory (sibling of `bin/`), then start
Komet:

```bash
cp target/komet-example-rules-plugin-1-SNAPSHOT.jar \
   /path/to/kometRuntimeImage/plugins/
```

Focus a concept in Komet and open its context menu — **"Example: log this concept"** appears,
contributed entirely by this plugin. Remove the jar (and restart) to remove the rule.

> Plugins can be disabled at launch with `-Dike.plugins.disabled=true`.

## Make it your own

- Add `@RuleElement`-annotated predicate methods to `ExampleRules` for pack-specific conditions,
  and reference them by name in `@MethodPredicate`.
- Return more rule classes from `ExampleRuleProvider.ruleClasses()`.
- Replace `ExampleSuggestedAction` with real work; use `viewCalculator()` to read the component,
  its STAMP, and version history.
<!-- BEGIN ike-managed: developer-setup -->

## Developer Setup

New to IKE development? The
[Developer Environment guide](https://ike.network/ike-tooling/ike-build-standards/developer-environment.html)
covers IDE configuration, JDK 25 setup, and the tooling conventions
every IKE workspace expects — start there before your first build.
<!-- END ike-managed: developer-setup -->
