# Sentinel Testing And Grammar Roadmap

## Goal
- Keep `Sentinel.g4` buildable and testable while incrementally aligning with real Sentinel policy syntax.
- Use real policy files as regression inputs so parser behavior changes are visible in CI.

## Current Status
- Basic statements/expressions parse successfully.
- Real policy fixtures from `hashicorp/terraform-sentinel-policies` are executed in unit tests.
- Real fixtures currently validate "parser returns diagnostics" for unsupported constructs.

## Unsupported Constructs (High Priority)
- `import "module" as alias`
- `main = rule { ... }` and inline `rule` expressions
- Collection iteration forms used in real policies (`for x as k, v { ... }`)
- Additional operators/keywords patterns seen in upstream policies
- Comment forms and escape patterns beyond current grammar coverage

## Incremental Implementation Plan
1. Add parser/lexer rules for `import ... as ...` and cover with focused unit tests.
2. Add `rule` expression support (`rule { condition }`) and assignment to identifiers.
3. Expand loop and collection grammar to match real fixture usage.
4. Move selected fixture assertions from "diagnostics expected" to "no syntax error expected" as support lands.
5. Keep grammar ambiguity checks by running full Maven test on each step.

## Fixture Selection Criteria
- Prefer short files first (easy failure localization).
- Include representative syntax from production-style policies (`import`, `rule`, loops, maps/lists, function defs).
- Include fragile constructs (string escapes, nested blocks, chained expressions).
- Keep fixtures stable and versioned in `src/test/resources/sentinel/terraform-sentinel-policies/`.
- When adding fixtures, document why each file was chosen and what syntax it protects.

## Test Layering
- Unit tests for isolated grammar behavior (operator precedence, unary operators, error paths).
- Fixture-driven tests for real-world syntax coverage.
- Promote fixture tests from "diagnostics expected" to "parse success expected" per grammar milestone.
