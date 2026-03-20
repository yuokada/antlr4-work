# GitHub Copilot Instructions

このリポジトリで作業する際は、以下の方針を優先してください。

## 基本方針

- ユーザー向けの説明、提案、レビューコメント、PR 要約は日本語で書く。
- 変更は最小限に留め、既存の ANTLR4 + Java + Maven 構成との整合性を保つ。
- ローカル環境の絶対パス、個人環境依存の設定、生成物への手動編集を持ち込まない。
- 文法定義、生成されたパーサ利用コード、テストの責務を混在させない。
- 文法変更では、対応する実行コードやテストも同じ変更で見直す。

## プロジェクト理解

- 文法ファイルは `src/main/antlr4/com/baeldung/antlr/` 配下にある。
- Java の実行コードやリスナーは `src/main/java/com/baeldung/antlr/` 配下にある。
- テストは `src/test/java/com/baeldung/antlr/` と `src/test/resources/` 配下にある。
- 依存関係やコード生成設定は `pom.xml` で管理している。

## 実装と検証

- 文法や Java コードの変更では `./mvnw test` を優先して確認する。
- 実施した確認と未実施の確認を明確に区別して伝える。
