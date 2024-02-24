from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
import time
import os
from urllib.request import urlretrieve
import json

def extract_contest_info(browser) :
    title = browser.find_element(By.CSS_SELECTOR, "#container > div.content-area > div.content-wrap > div.content > div > div.tit-area > h6").text
    host = browser.find_element(By.CSS_SELECTOR, "#efrm > input[type=hidden]:nth-child(3)").get_attribute("value")[:-1]
    target_participants = browser.find_element(By.CSS_SELECTOR, "#container > div.content-area > div.content-wrap > div.content > div > div.cd-area > div.info > ul > li:nth-child(2)").text.replace("응모대상\n", "")
    reception_period = browser.find_element(By.CSS_SELECTOR, "#efrm > input[type=hidden]:nth-child(5)").get_attribute("value")[:-5]
    decision_period = None
    compatition_area = None
    award = browser.find_element(By.CSS_SELECTOR, "#efrm > input[type=hidden]:nth-child(6)").get_attribute("value") + ", 1등 상금 : " + browser.find_element(By.CSS_SELECTOR, "#efrm > input[type=hidden]:nth-child(7)").get_attribute("value").replace("\n", "")
    homepage_link = browser.find_element(By.CSS_SELECTOR, "#efrm > input[type=hidden]:nth-child(8)").get_attribute("value")
    how_to_apply = None
    participation_fee = None
    image_link = browser.find_element(By.CSS_SELECTOR, "#container > div.content-area > div.content-wrap > div.content > div > div.cd-area > div.img > div.thumb > img").get_attribute("src")

    detail_array = []
    detail_items = browser.find_elements(By.CSS_SELECTOR, "#viewContents > div")
    for detail_item in detail_items:
        detail_array.append(detail_item.text)
    detail_text = '\n'.join(detail_array)

    contest_json = {
        "contest_title": title,
        "contest_host": host,
        "contest_target_participants": target_participants,
        "contest_reception_period": reception_period,
        "contest_decision_period": decision_period,
        "contest_competition_area": compatition_area,
        "contest_award": award,
        "contest_homepage": homepage_link,
        "contest_how_to_apply": how_to_apply,
        "contest_fee": participation_fee,
        "contest_image": image_link,
        "contest_detail_text": detail_text
    }

    return contest_json

# 브라우저 꺼짐 방지
chrome_options = Options()
chrome_options.add_experimental_option("detach", True)

# 불필요한 에러 메시지 없애기
chrome_options.add_experimental_option("excludeSwitches", ["enable-logging"])

# 브라우저 생성, 웹 사이트 열기
browser = webdriver.Chrome(options=chrome_options)

contest_data = [] #공모전 정보 담을 리스트

## ==================== 웹/모바일/IT 접수중 공모전 시작 ==================== ##
browser.get('https://www.wevity.com/?c=find&s=1&gub=1&cidx=20&sp=&sw=&gbn=list&mode=ing')   #접수중 공모전 목록 페이지

browser.implicitly_wait(10)
browser.maximize_window()
time.sleep(2)

for item_index in range(2, len(browser.find_elements(By.CSS_SELECTOR, "#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li")) + 1, 1) :
    # 목록 상의 공모전 클릭
    browser.find_element(By.CSS_SELECTOR, f"#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li:nth-child({item_index}) > div.tit > a").click()
    time.sleep(2)

    contest_json = extract_contest_info(browser)
    contest_data.append(contest_json)

    # 이전 페이지 (목록 페이지)로 돌아가기
    browser.back()
    time.sleep(2)

# ====================================== 접수 중 공모전 끝, 마감 임박 공모전 시작 ====================================================== #
browser.get('https://www.wevity.com/?c=find&s=1&gub=1&cidx=20&sp=&sw=&gbn=list&mode=soon')   #마감임박 공모전 목록 페이지

browser.implicitly_wait(10)
browser.maximize_window()
time.sleep(2)

for item_index in range(2, len(browser.find_elements(By.CSS_SELECTOR, "#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li")) + 1, 1) :
    # 목록 상의 공모전 클릭
    browser.find_element(By.CSS_SELECTOR, f"#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li:nth-child({item_index}) > div.tit > a").click()
    time.sleep(2)

    contest_json = extract_contest_info(browser)
    contest_data.append(contest_json)

    # 이전 페이지 (목록 페이지)로 돌아가기
    browser.back()
    time.sleep(2)


# ====================================== 마감임박 공모전 끝, 접수 예정 공모전 시작 ====================================================== #
browser.get('https://www.wevity.com/?c=find&s=1&gub=1&cidx=20&sp=&sw=&gbn=list&mode=future')   #접수예정 공모전 목록 페이지

browser.implicitly_wait(10)
browser.maximize_window()
time.sleep(2)

for item_index in range(2, len(browser.find_elements(By.CSS_SELECTOR, "#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li")) + 1, 1) :
    # 목록 상의 공모전 클릭
    browser.find_element(By.CSS_SELECTOR, f"#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li:nth-child({item_index}) > div.tit > a").click()
    time.sleep(2)

    contest_json = extract_contest_info(browser)
    contest_data.append(contest_json)

    # 이전 페이지 (목록 페이지)로 돌아가기
    browser.back()
    time.sleep(2)

# ====================================== 접수중 공모전(게임/소프트웨어) ====================================================== #
browser.get('https://www.wevity.com/?c=find&s=1&gub=1&cidx=21&sp=&sw=&gbn=list&mode=ing')   #접수중 공모전 목록 페이지

browser.implicitly_wait(10)
browser.maximize_window()
time.sleep(2)

for item_index in range(2, len(browser.find_elements(By.CSS_SELECTOR, "#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li")) + 1, 1) :
    # 목록 상의 공모전 클릭
    browser.find_element(By.CSS_SELECTOR, f"#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li:nth-child({item_index}) > div.tit > a").click()
    time.sleep(2)

    contest_json = extract_contest_info(browser)
    contest_data.append(contest_json)

    # 이전 페이지 (목록 페이지)로 돌아가기
    browser.back()
    time.sleep(2)

# ====================================== 접수중 공모전 끝, 접수 예정 공모전 시작 ====================================================== #
browser.get('https://www.wevity.com/?c=find&s=1&gub=1&cidx=21&sp=&sw=&gbn=list&mode=future')   #접수예정 공모전 목록 페이지

browser.implicitly_wait(10)
browser.maximize_window()
time.sleep(2)

for item_index in range(2, len(browser.find_elements(By.CSS_SELECTOR, "#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li")) + 1, 1) :
    # 목록 상의 공모전 클릭
    browser.find_element(By.CSS_SELECTOR, f"#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li:nth-child({item_index}) > div.tit > a").click()
    time.sleep(2)

    contest_json = extract_contest_info(browser)
    contest_data.append(contest_json)

    # 이전 페이지 (목록 페이지)로 돌아가기
    browser.back()
    time.sleep(2)

# ====================================== 접수예정 공모전 끝, 마감 임박 공모전 시작 ====================================================== #
browser.get('https://www.wevity.com/?c=find&s=1&gub=1&cidx=21&sp=&sw=&gbn=list&mode=soon')   #마감임박 공모전 목록 페이지

browser.implicitly_wait(10)
browser.maximize_window()
time.sleep(2)

for item_index in range(2, len(browser.find_elements(By.CSS_SELECTOR, "#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li")) + 1, 1) :
    # 목록 상의 공모전 클릭
    browser.find_element(By.CSS_SELECTOR, f"#container > div.content-area > div.content-wrap > div.content > div:nth-child(4) > div > ul > li:nth-child({item_index}) > div.tit > a").click()
    time.sleep(2)

    contest_json = extract_contest_info(browser)
    contest_data.append(contest_json)

    # 이전 페이지 (목록 페이지)로 돌아가기
    browser.back()
    time.sleep(2)

# JSON 저장
file_path = 'wevity_contest_data.json'

try:
    with open(file_path, 'w', encoding='utf-8') as f:
        json.dump(contest_data, f, ensure_ascii=False)
        print(f"JSON파일이 생성되었습니다: {file_path}")
except Exception as e:
    print(f"JSON파일 생성 중 오류 발생 : {e}")

browser.quit()