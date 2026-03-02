# Sentinel Issue Backlog (Drafts)

`yuokada/antlr4-work` は現在 GitHub Issues が無効化されているため、起票内容をドラフトとして管理する。
Issues 有効化後にそのまま転記して利用する。

## 1) Support report.sentinel core syntax

### Title
`Sentinel: support syntax required by common-functions/report/report.sentinel`

### Body
- Goal: move `common-functions/report/report.sentinel` from unsupported subset to compatible subset.
- Scope:
  - Improve function literal/call parsing around nested function declarations.
  - Strengthen operator coverage for `not contains` and related patterns.
  - Validate block nesting/for-loop forms used in report fixture.
- Acceptance:
  - `SentinelParserUnitTest` compatible subset includes `common-functions/report/report.sentinel`.
  - Unsupported subset no longer includes this fixture.
  - `./mvnw test` passes.

## 2) Promote fixtures by grammar milestone

### Title
`Sentinel: promote fixture assertions from diagnostics to parse-success`

### Body
- Goal: make fixture progression explicit and incremental.
- Scope:
  - Every grammar milestone must move at least one fixture from unsupported to compatible.
  - Keep both subsets explicit in `SentinelParserUnitTest`.
  - Add short changelog entries to roadmap when subsets change.
- Acceptance:
  - Test names clearly show compatible/unsupported sets.
  - CI fails if a compatible fixture regresses.

## 3) Expand loop and collection grammar compatibility

### Title
`Sentinel: expand loop/collection syntax compatibility for real policies`

### Body
- Goal: align `for ... as ...` and collection expression parsing with real policy usage.
- Scope:
  - Cover `for collection as key, value { ... }` variations.
  - Cover list/map trailing commas and nested access patterns.
  - Add focused unit tests for ambiguous or fragile forms.
- Acceptance:
  - New loop/collection unit tests added and passing.
  - No regression in existing compatible fixtures.
