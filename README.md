# Composable Todo App

このアプリは、MVVMアーキテクチャとReduxパターンを組み合わせたTodoアプリケーションです。

## 技術スタック

- **言語**: Kotlin
- **アーキテクチャ**: MVVM + Redux
- **データベース**: Room
- **依存性注入**: Hilt
- **非同期処理**: Coroutines + Flow
- **UI**: Jetpack Compose
- **テスト**: Kotest + Mockk + Turbine

## 機能

- Todoの一覧表示
- Todoの追加
- Todoの削除
- リアルタイム更新（Flow）

## アーキテクチャの説明

### MVVM + Redux

このアプリでは、MVVMアーキテクチャとReduxパターンを組み合わせています：

- **Model**: RoomデータベースとRepository
- **View**: Jetpack Compose UI
- **ViewModel**: 状態管理とUIロジック
- **Redux**: 単一の状態管理とアクション駆動の更新

### データフロー

1. UIからアクションが発行される
2. ViewModelがアクションをStoreにディスパッチ
3. StoreがMiddlewareにアクションを渡す
4. MiddlewareがRepositoryを使用してデータを更新
5. RepositoryがRoomデータベースを更新
6. データベースの変更がFlowを通じて通知される
7. Storeが新しい状態を発行
8. ViewModelが状態を購読し、UIを更新

## セットアップ

1. リポジトリをクローン
```bash
git clone https://github.com/yourusername/mvvmandredux.git
```

2. プロジェクトを開く
```bash
cd mvvmandredux
```

3. 依存関係を同期
```bash
./gradlew build
```

## テスト

ユニットテストを実行:
```bash
./gradlew test
```

## ライセンス

MIT License 
