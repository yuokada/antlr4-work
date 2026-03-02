# Sentinel Testing And Grammar Roadmap

## Goal
- Keep `Sentinel.g4` buildable and testable while incrementally aligning with real Sentinel policy syntax.
- Use real policy files as regression inputs so parser behavior changes are visible in CI.

## Current Status
- Basic statements/expressions parse successfully.
- Real policy fixtures from `hashicorp/terraform-sentinel-policies` are executed in unit tests.
- Fixture tests are split into two lanes:
  - **compatible subset**: no syntax errors expected.
  - **unsupported subset**: diagnostics expected until grammar support is added.

## Supported Syntax (Current)
- Statements:
  - variable assignment: `name = expr`, `name += expr`, `name -= expr`
  - `import "module"` and `import "module" as alias`
  - `if ... { ... } else { ... }`
  - `for collection as key { ... }` / `for collection as key, value { ... }`
  - `return`, `break`, `continue`
- Callable constructs:
  - function definition: `func name(arg1, arg2) { ... }`
  - function literal: `func(arg1, arg2) { ... }`
  - call: `fn(a, b)` and `obj.method(a, b)`
- Rule constructs:
  - named rule block: `rule name { ... }`
  - rule literal expression: `rule { ... }`
- Expressions:
  - arithmetic: `+`, `-`, `*`, `/`, `%`
  - comparison: `==`, `!=`, `<`, `<=`, `>`, `>=`
  - logical: `and`, `or`, `xor`, `not`
  - Sentinel-style operators: `in`, `contains`, `is`, `not in`, `not contains`
  - postfix access: `obj.field`, `list[index]`, `obj.method()`
  - literals: int/float/string/bool/null, list/map literals（末尾カンマ対応）
- Comments:
  - `# ...`, `// ...`, `/* ... */`

## Fixture Regression Matrix (Current)
- Compatible subset:
  - `aws/restrict-ami-owners.sentinel`
  - `cloud-agnostic/prohibited-providers.sentinel`
  - `aws/mocks/ec2-instance-mock-tfrun.sentinel`
- Unsupported subset:
  - `common-functions/report/report.sentinel`

## Unsupported Constructs (High Priority)
- `report.sentinel` で使われる一部の高度な入れ子表現・文字列操作パターン
- 実運用ポリシー全体をカバーするための追加演算子/式パターン
- `for` や関数リテラルのネストが深いケースでの互換性強化

## Incremental Implementation Plan
1. Add parser/lexer rules for `import ... as ...` and cover with focused unit tests.
2. Add `rule` expression support (`rule { condition }`) and assignment to identifiers.
3. Expand loop and collection grammar to match real fixture usage.
4. Move selected fixture assertions from "diagnostics expected" to "no syntax error expected" as support lands.
5. Keep grammar ambiguity checks by running full Maven test on each step.

Issue candidate drafts are tracked in `docs/sentinel-issue-backlog.md`
because GitHub Issues are currently disabled for this repository.

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
- Keep fixture path lists fixed in test code so grammar regressions are immediately visible in CI output.
