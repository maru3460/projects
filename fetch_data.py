import os
import json
import time
from pathlib import Path
import requests
from bs4 import BeautifulSoup


#urlをjsonから取り出す
with open('config.json') as f:
    urls = json.load(f)
gihyo_url = urls['gihyo_url']
gihyo_to_book = urls['gihyo_to_book']
gihyo_book_url = gihyo_url + gihyo_to_book
image_dir_path_from_python = urls['image_dir_path_from_python']
image_dir_path_from_java = urls['image_dir_path_from_java']
#mainのページのsoupを取得
time.sleep(1)
res = requests.get(gihyo_book_url)
res.encoding = 'utf-8'
gihyo_soup = BeautifulSoup(res.text, 'html.parser')
#書籍新刊案内のそれぞれのliを取得
boxes = gihyo_soup.find_all('li', class_='clearfix')
#ループしてタイトルと詳細ページへのリンク、画像へのリンクを取得
titles = []
links = []
img_urls = []
image_links = []
for i in range(len(boxes)):
    time.sleep(1)
    data_div = boxes[i].find('div', class_='data')
    a_tag = data_div.find('a')
    titles.append(a_tag.get_text())
    links.append(gihyo_url + a_tag.get('href'))
    p = Path(image_dir_path_from_java + f'image{i + 1}.jpg')
    image_links.append(str(p).replace("\\", "/"))
    cover_div = boxes[i].find('div', class_='cover')
    detail_url = gihyo_url + cover_div.find('a').get('href')
    detail_res = requests.get(detail_url)
    detail_res.encoding = 'utf-8'
    detail_soup = BeautifulSoup(detail_res.text, 'html.parser')
    img_a_tag = detail_soup.find(id='publishedDetail').find('div', class_='cover').find('a')
    img_url = img_a_tag.get('href')
    img_urls.append(img_url)
#data_file作成
data_file_path = urls['data_file_path']
write_str = []
for i in range(len(boxes)):
    write_str.append(f'{titles[i]},{links[i]},{image_links[i]}\n')
with open(data_file_path, 'w', encoding='utf-8') as f:
    f.writelines(write_str)
#画像を保存
if not os.path.exists(image_dir_path_from_python):
    os.mkdir(image_dir_path_from_python)
for i in range(len(boxes)):
    res_img = requests.get(img_urls[i])
    img = res_img.content
    with open(image_dir_path_from_python + 'image' + str(i + 1) + '.jpg', 'wb') as f:
        f.write(img)
