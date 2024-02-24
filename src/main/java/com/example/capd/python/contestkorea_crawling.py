from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
import time
import json

def extract_contest_info(browser):
    title = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > h1").text
    host = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(1) > td").text
    target_participants = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(3) > td").text
    reception_period = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(4) > td").text
    decision_period = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(5) > td").text
    compatition_area = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(6) > td").text
    awards_details = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(7) > td").text
    if browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(8) > th").text == '홈페이지' :
        homepage_link = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(8) > td > a").get_attribute('href')
        how_to_apply = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(9) > td").text
        if browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(10) > th").text == '접수하기' :
            participation_fee = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(11) > td").text
        else :
            participation_fee = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(10) > td").text
    else :
        homepage_link = None
        how_to_apply = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(8) > td").text
        if browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(9) > th").text == '접수하기' :
            participation_fee = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(10) > td").text
        else :
            participation_fee = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.txt_area > table > tbody > tr:nth-child(9) > td").text
    detail_text = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.tab_cont > div > div").text
    image = browser.find_element(By.CSS_SELECTOR, "#wrap > div.container.list_wrap > div.left_cont > div.view_cont_area > div.view_top_area.clfx > div.clfx > div.img_area > div > img")
    image_link = image.get_attribute('src')

    contest_json = {
        "contest_title": title,
        "contest_host": host,
        "contest_target_participants": target_participants,
        "contest_reception_period": reception_period,
        "contest_decision_period": decision_period,
        "contest_compatition_area": compatition_area,
        "contest_award": awards_details,
        "contest_homepage": homepage_link,
        "contest_how_to_apply": how_to_apply,
        "contest_fee": participation_fee,
        "contest_image":image_link,
        "contest_detail_text": detail_text
    }
    return contest_json

# 브라우저 설정
chrome_options = Options()
chrome_options.add_experimental_option("detach", True)
chrome_options.add_experimental_option("excludeSwitches", ["enable-logging"])
browser = webdriver.Chrome(options=chrome_options)

# 웹 페이지 열기
browser.get('https://www.contestkorea.com/sub/list.php?int_gbn=1&Txt_bcode=030510001')  # 콘테스트코리아 IT/SW/Game부문 bcode 030510001
browser.implicitly_wait(10)
browser.maximize_window()
time.sleep(2)

# 대회 정보 수집
contest_data = []

# 접수중 공모전
browser.find_element(By.CSS_SELECTOR, "#frm > div > div.clfx.mb_20 > div.f-r > ul > li:nth-child(4) > button").click()
time.sleep(2)

items = browser.find_elements(By.CSS_SELECTOR, "#frm > div > div.list_style_2 > ul > li")
for item in range(1, len(items) + 1, 1):
    browser.find_element(By.CSS_SELECTOR, f"#frm > div > div.list_style_2 > ul > li:nth-child({item}) > div.title > a > span.txt").click()
    time.sleep(2)
    contest_json = extract_contest_info(browser)
    contest_data.append(contest_json)
    browser.back()
    time.sleep(2)

# 접수 예정 공모전
browser.get('https://www.contestkorea.com/sub/list.php?int_gbn=1&Txt_bcode=030510001')  # 콘테스트코리아 IT/SW/Game부문 bcode 030510001
browser.implicitly_wait(10)
browser.maximize_window()
time.sleep(2)
browser.find_element(By.CSS_SELECTOR, "#frm > div > div.clfx.mb_20 > div.f-r > ul > li:nth-child(3) > button").click()
time.sleep(2)

items = browser.find_elements(By.CSS_SELECTOR, "#frm > div > div.list_style_2 > ul > li")
for item in range(1, len(items) + 1, 1):
    browser.find_element(By.CSS_SELECTOR, f"#frm > div > div.list_style_2 > ul > li:nth-child({item}) > div.title > a > span.txt").click()
    time.sleep(2)
    contest_json = extract_contest_info(browser)
    contest_data.append(contest_json)
    browser.back()
    time.sleep(2)


# Contest 데이터를 JSON 파일로 저장
file_path = 'contestkorea_contest_data.json'

try:
    with open(file_path, 'w', encoding='utf-8') as f:
        json.dump(contest_data, f, ensure_ascii=False)
        print(f"JSON 파일이 생성되었습니다: {file_path}")
except Exception as e:
    print(f"JSON 파일 생성 중 오류 발생: {e}")

browser.quit()