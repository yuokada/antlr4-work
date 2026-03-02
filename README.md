# antlr4-dncl

このリポジトリは **ANTLR4 + Java 17 + Maven** で、文法 (`.g4`) から Lexer/Parser を生成し、
Java コードから構文解析を行うサンプルです。

## 1. ディレクトリ構成

```text
src/main/antlr4/com/baeldung/antlr/   # ANTLR grammar (*.g4)
src/main/java/com/baeldung/antlr/     # パーサ利用コード (Main, Listener など)
src/test/java/com/baeldung/antlr/     # JUnit テスト
```

主要ファイル:
- `Hello.g4`: 最小の挨拶文法
- `Log.g4`, `Java8.g4`, `Sentinel.g4`: 応用文法
- `HelloMain.java`: 生成 Parser を使う実行例

## 2. ANTLR4 の設定（Maven）

`pom.xml` では以下を利用しています。

- `antlr4-maven-plugin`: `.g4` から Java コード生成
- `build-helper-maven-plugin`: `target/generated-sources/antlr4` をソースとして追加
- `antlr4-runtime`: 実行時ライブラリ

```xml
<plugin>
  <groupId>org.antlr</groupId>
  <artifactId>antlr4-maven-plugin</artifactId>
  <version>${antlr4.version}</version>
  <executions>
    <execution>
      <goals>
        <goal>antlr4</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

## 3. 基本コマンド

```bash
# 生成 + コンパイル
./mvnw clean compile

# 全テスト
./mvnw test

# 特定テストのみ
./mvnw -Dtest=SentinelParserUnitTest test
```

## 4. 文法作成の最小例

`src/main/antlr4/com/baeldung/antlr/Hello.g4`

```antlr
grammar Hello;
greeting : 'hello' ID ;
ID  : [a-zA-Z]+ ;
WS  : [ \t\r\n]+ -> skip ;
```

この文法から `HelloLexer`, `HelloParser` が生成されます。

## 5. 生成 Parser の使い方（Javaコード）

`src/main/java/com/baeldung/antlr/hello/HelloMain.java`

```java
CharStream input = CharStreams.fromStream(System.in);
HelloLexer lexer = new HelloLexer(input);
CommonTokenStream tokens = new CommonTokenStream(lexer);
HelloParser parser = new HelloParser(tokens);
ParseTree tree = parser.greeting();
System.out.println(tree.toStringTree(parser));
```

`exec-maven-plugin` を使うと直接実行できます:

```bash
./mvnw -Dexec.mainClass=com.baeldung.antlr.hello.HelloMain exec:java
```

## 6. テストの書き方

`src/test/java/com/baeldung/antlr/Hellog4Test.java`

```java
CharStream input = CharStreams.fromString("hello world");
HelloLexer lexer = new HelloLexer(input);
CommonTokenStream tokens = new CommonTokenStream(lexer);
HelloParser parser = new HelloParser(tokens);
ParseTree tree = parser.greeting();
assertEquals("(greeting hello world)", tree.toStringTree(parser));
```

ポイント:
- `CharStreams.fromString(...)` で入力を固定して再現性を確保
- `toStringTree(parser)` で構文木の期待値を検証
- 構文エラー検証時は `BaseErrorListener` を差し替えて件数を確認

## 7. Sentinel 文法の互換性

`Sentinel.g4` は段階的に互換性を拡張しています。現在の運用は次の2レーンです。

- 互換サブセット（構文エラー 0 を期待）
  - `aws/restrict-ami-owners.sentinel`
  - `cloud-agnostic/prohibited-providers.sentinel`
  - `aws/mocks/ec2-instance-mock-tfrun.sentinel`
- 未対応サブセット（診断を期待）
  - `common-functions/report/report.sentinel`

対応状況と今後の方針は [sentinel-testing-and-grammar-roadmap.md](/Users/yuokada/ghq/github.com/yuokada/antlr4-work/docs/sentinel-testing-and-grammar-roadmap.md) を参照してください。

## 8. 失敗時のデバッグ手順

Sentinel 関連でテストが落ちた場合は、次の順で切り分けると早いです。

1. Sentinel テストだけ実行して失敗を再現

```bash
./mvnw -Dtest=SentinelParserUnitTest test
```

2. 互換/未対応どちらの fixture が失敗したか確認  
`SentinelParserUnitTest` はパラメタライズされているため、失敗ケース名に fixture パスが出ます。

3. 構文エラー詳細を確認  
`SyntaxErrorCollector` のメッセージ（`line x:y ...`）を見て、`Sentinel.g4` の該当規則を確認します。

4. 必要に応じて最小入力で再現  
`CharStreams.fromString(...)` で最小ケースを作り、`toStringTree(parser)` で木構造を確認します。

```java
SentinelParser parser = new SentinelParser(
    new CommonTokenStream(new SentinelLexer(CharStreams.fromString("main = rule { true }")))
);
ParseTree tree = parser.policy();
System.out.println(tree.toStringTree(parser));
```

5. 修正後は全体回帰を実行

```bash
./mvnw test
```

## 9. 参考

- [Java with ANTLR | Baeldung](https://www.baeldung.com/java-antlr)
- [eugenp/tutorials ANTLR module](https://github.com/eugenp/tutorials/tree/master/text-processing-libraries-modules/antlr)
