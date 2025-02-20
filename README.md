# SettingsLocker
![image](src/main/resources/logo.png)
## 環境
- ```forge 1.20.1```
- サーバー・クライアントの両方に導入必須
## 機能
キー・オプション設定をサーバー側で指定できるmod

- 設定ファイル```config/settingslocker.yml```
- 起動すると```config```内に```settingslocker.yml```が生成される
- サーバーに入ったときに自動的に設定が反映される
- 退出時に元の設定に戻る
- **pathは各種設定ボタンを右クリックするとクリップボードにコピーされる**
- キーバインドとオプションは追加可能
- ただし、modで追加されたオプションはボタンの有効化と無効化のみ設定可能

## 導入手順
- modsフォルダに追加
- ```config/settingslocker.yml```を書き換え

## 設定ファイル (settingslocker.yml) のフォーマット
### 基本構造
```yaml
path:
  unlock: <true/false>
  key: "<キー設定>"
  value: <値>
  active: <true/false> (一部のデバック機能のみ)
```

| 項目      | 型                      | 説明                                |
|---------|------------------------|-----------------------------------|
| path    | String                 | 設定する項目のパス                         |
| unlock	 | Boolean                | false の場合、その設定を変更不可にする            |
| key	    | String	                | キー割り当てを指定する (unlock: false のとき適用) |
| value	  | String/Number/Boolean	 | 設定する値 (value が設定されている場合)          |
| active	 | Boolean                | false にすると、その設定を無効化する             |

## コマンド
- コマンドでの変更はあくまで一時的なものです。

- プレイヤー毎の指定はプレイヤーの退出により、configの値に置き換えられます。

- コマンドで変更されたconfigの値は```/locksettings config save``` を実行しないとファイルに保存されず、再起動によりリセットされます。

### プレイヤーの設定を変更
```
/locksettings <targets> <category> <path> <value>
```
- targets: プレイヤーのリスト[player, @a, @e, @pなど]

- category: 設定のカテゴリ (unlock, key, value, active)

- path: 設定のパス

- value: 設定する値	特定のプレイヤーの設定を変更する

### コンフィグ設定の変更 (全プレイヤーの設定を変更する)
新しくログインしたプレイヤーに反映されるデフォルト設定
```
/locksettings config set <category> <path> <value>
```

- category: 設定のカテゴリ (unlock, key, value, active)

- path: 設定のパス

- value: 設定する値	サーバー内の全プレイヤーの設定を変更

### コンフィグファイルを読み込む (全プレイヤーの設定を変更する)
```
/locksettings config reload
```
設定ファイル (settingslocker.yml) をリロードし、全プレイヤーに同期する。

### 現在のコンフィグ設定を取得する
```
/locksettings config get <path>
```
path: 指定したパスの現在の設定を表示する。

### 現在のコンフィグを```config/settingslocker.yml```に保存する
```
/locksettings config save
```

## 使用例
### (1) プレイヤーに一人称視点を強制させたい場合
キー割り当てを割り当てなしにし、視点を一人称にする
``` yaml
### 視点の切り替え
key.togglePerspective:
  unlock: false
  key: "Unknown"
  cameraType: "FIRST_PERSON"
```

### (2) 明るさを自動的に1000%にしたい場合
``` yaml
# 明るさ
options.gamma:
  unlock: false
  value: 10 # 明るさは100倍される
```

### (3) 当たり判定表示 F3 + B を使えないようにしたい場合
```yaml
## 当たり判定を表示(F3+B)
key.debugF3+B:
  unlock: false
  value: false # false:非表示 (falseがデフォルト(当たり判定を表示しない))
```
### (4) デバック画面を非表示にしたい場合
```yaml
##　デバッグ画面(F3)
key.debugF3:
  unlock: false
  value: false # true: renderDebug (falseがデフォルト(デバッグ画面を表示しない))
```

### (5) チャット入力を不可能にしたい場合
```yaml
# falseにするとチャット欄に入力できなくなる
chat.editBox:
  unlock: false
```
### (6) Optifineのズームを無効化したい場合
```yaml
### Optifine ZOOM設定
of.key.zoom:
  unlock: false
  key: "Unknown"
```

### 謝辞
一部の実装は、[ClouserSettingsLocker](https://github.com/Mortimer-Kerman/ClouserSettingsLocker)を参考にしています。
