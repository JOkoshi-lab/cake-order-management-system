# ケーキ注文管理システム

ケーキの商品情報と注文データを扱うWebアプリケーションです。JdbcTemplateを用いたデータベース操作、注文データの集計などを学べる構成です。

## 使用技術
Java / Spring Boot / Thymeleaf / Spring JDBC / H2 Database

## 起動方法（Windows）

プロジェクトフォルダをPowerShellで開き、次のコマンドを実行してください。

```powershell
.\mvnw.cmd spring-boot:run
```

アプリのアクセス先：http://localhost:8080/

データベースを使用する作品は、起動時にH2のインメモリデータベースを初期化します。再起動すると、登録内容は初期状態に戻ります。