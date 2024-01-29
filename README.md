# Java SpringBoot Programs  
springbootで作ったものです。  

## 1. Hit&Blowのアシストプログラム   
ユーザーのHit&Blowのアシスト(代行)をするプログラムです。  
プログラムが相手の数字を予測するので、その数字のHitとBlowを入力していってください。  

## 2. Pythonによるスクレイピング
[技術評論社](https://gihyo.jp/book "技術評論社 トップ")における書籍新刊案内からデータを持ってきています。  
有効にするためには、実行前にvenvを作って、bs4とrequestsをインストールしておいてください。  
```venvの作り方  
cd projects
python -m venv venv
./venv/Scripts/activate
pip install bs4
pip install requests
deactivate
```  

## 実行  
```
git clone ---  
cd projects  
chmod +x gradlew  

./gradlew build  
./gradlew bootRun  

(ブラウザ)localhost:8080/home/  
```