import subprocess

# 실행할 파이썬 파일 경로들
file_paths = [
    'C:\IntelliJ\capD\src\main\java\com\example\capd\python\contestkorea_crawling.py',
    'C:\IntelliJ\capD\src\main\java\com\example\capd\python\wevity_crawling.py'
]

# 각 파일을 순회하며 실행
for file_path in file_paths:
    try:
        subprocess.run(['python', file_path], check=True)

    except subprocess.CalledProcessError as e:
        print(f"오류 발생: {file_path}\n{e}")