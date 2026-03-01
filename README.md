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

## 7. 参考

- [Java with ANTLR | Baeldung](https://www.baeldung.com/java-antlr)
- [eugenp/tutorials ANTLR module](https://github.com/eugenp/tutorials/tree/master/text-processing-libraries-modules/antlr)
