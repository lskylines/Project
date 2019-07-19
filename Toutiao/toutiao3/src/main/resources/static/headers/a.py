import requests
from bs4 import BeautifulSoup
from PIL import Image
import os 
from io import BytesIO
import time
import random

headers ={"User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.75 Safari/537.36",
          "Cookie": "NOWCODERUID=B7A2FA1A5712D34922445B18BACB0ED2; NOWCODERCLINETID=93D73A7743BC7253CEBDE56D28E6A28B; gr_user_id=194e6866-8596-4254-ba0f-131f1b5a334d; grwng_uid=180bc9b6-01df-41d0-8b91-386246d63a26; Hm_lvt_a808a1326b6c06c437de769d1b85b870=1561709947,1561725981,1561740540,1561741499; _ga=GA1.2.1033600998.1561741542; _gid=GA1.2.831364698.1561741542",
          "Accept":"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3"}
for i in range(26, 100):
    time.sleep(2)
    url = "http://images.nowcoder.com/head/"+ str(random.randint(1, 1000)) +"t.png"
    response = requests.get(url, headers=headers)
    with open(str(i)+'.png', 'wb') as f:
            f.write(response.content)  
                                
