import pymysql
import pandas as pd

connection = pymysql.connect(host='localhost',
                             user='root',
                             password='db12',
                             database='capd',
                             charset='utf8mb4',
                             cursorclass=pymysql.cursors.DictCursor)


# user_id = '1'  #유저 id -> 추천 리스트 요청한 user
contest_id = '1' #공모전 id

try:
    with connection.cursor() as cursor:
        # profile 테이블에서 데이터 가져오기
        profile_sql = "SELECT id, collaboration_count,my_time, desired_time,  rate FROM profile"
        cursor.execute(profile_sql)
        profile_rows = cursor.fetchall()


        # 각 프로필마다 참여 데이터를 가져와서 스택 리스트를 조합하여 출력
        for profile in profile_rows:
            user_id = profile['id']

            # 참여 데이터 가져오기
            participation_sql = f"""
                SELECT psl.stack_list
                FROM participation_stack_list psl
                JOIN participation p ON psl.participation_id = p.id
                JOIN contest c ON p.contest_id = c.id
                WHERE p.user_id = '{user_id}'
                AND c.id = '{contest_id}';
            """
            cursor.execute(participation_sql)
            participation_rows = cursor.fetchall()

            # 스택 리스트 조합
            stack_lists = [participation['stack_list'] for participation in participation_rows]
            combined_stack_list = ', '.join(stack_lists)


            # 출력
            #profile['stack_list'] = combined_stack_list #-> stack_list 위치 변경 x
            #print(profile)

            # 프로필 정보 저장
            profile_info = {
                'id': profile['id'],
                'stack_list': combined_stack_list,                #-> stack_list 위치 변경
                'collaboration_count': profile['collaboration_count'],
                #'my_time': profile['my_time'],
                'desired_time': profile['desired_time'],
                'rate': profile['rate']
            }
            print(profile_info)

            #내 스택, 투자가능시간, 경험횟수,
        my_stack_sql = """
                        SELECT p.stack_list,
                        FROM profile p;
                        """

        my_career_count_sql = """
                            SELECT COUNT(*) FROM c.career;
                            """


finally:
    # 연결 닫기
    connection.close()