import React, { useEffect, useState } from 'react';
import './CompetitionDetail.css';
import axios from "axios";
import { useLocation } from 'react-router-dom';
import Share from '../../img/Share--Streamline-Nova.png'

export default function CompetitionDetail() {
    const [contestId, setContestId] = useState('');
    const [contest, setContest] = useState('');
    const [daysRemaining, setDaysRemaining] = useState(0);
    const location = useLocation();

    useEffect(() => {
        const id = window.localStorage.getItem('contestId');

        setContestId(location.state.id);
        window.localStorage.setItem('contestId', location.state.id);

        axios({
            method: 'get',
            url: `/contestdetail/${id}`
        }).then(result => {
            console.log(result.data);
            const processedData = preprocessData(result.data);
            setContest(processedData);

            const end = new Date(result.data.receptionPeriod.split(' ~ ')[1]);
            const currentDate = new Date();
            const remaining = Math.ceil((end - currentDate) / (1000 * 60 * 60 * 24));
            if (remaining < 0) {
                setDaysRemaining(0);
            } else {
                setDaysRemaining(remaining);
            }
        })
        axios({
            method: 'post',
            url: '/viewPlus',
            data: { id: id }
        })
    }, [])

    const preprocessData = (data) => {
        // 데이터 전처리하여 <br>로 줄 바꿈 처리
        const processedData = { ...data };
        for (const key in processedData) {
            if (typeof processedData[key] === 'string') {
                processedData[key] = processedData[key].replace(/\n/g, '<br>');
            }
        }
        return processedData;
    };

    return (
        <div>
            <div className='competition_detail_background'></div>
            <img src={contest.image} className='competition_detail_img' />
            <div className='competition_detail_big_box'>
                <div className='competition_detail_big_box_content'>
                    {contest.targetParticipants && contest.targetParticipants.split(',').map((participant, index) => (
                        <p key={index} className='competition_detail_big_box_content_people'>{participant.trim()}</p>
                    ))}
                    <p className='competition_detail_big_box_content_deadline'>D - {daysRemaining}</p>
                </div>
                <p className='competition_detail_big_box_content_title'>{contest.title}</p>
                <p className='competition_detail_big_box_content_host'>{contest.host}</p>
                <div>
                    <p className='competition_detail_big_box_content_sub_title'>접수비용</p>
                    <p className='competition_detail_big_box_content_sub_fee'>{contest.fee}</p>
                </div>
                <div className='competition_detail_sub_box_content'>
                    <div>
                        <p className='competition_detail_big_box_content_sub_title'>모집기간</p>
                        <p>  {
                            contest.receptionPeriod &&
                            <span className='competition_detail_sub_p'>~24.{contest.receptionPeriod.split('.')[3]}.{contest.receptionPeriod.split('.')[4]}</span>
                        }</p>
                    </div>
                    <div className='competition_detail_sub_box_content_div'>
                        <p className='competition_detail_big_box_content_sub_title'>접수방법</p>
                        <p className='competition_detail_sub_p'>{contest.howToApply}</p>
                    </div>
                    <div className='competition_detail_sub_box_content_div'>
                        <p className='competition_detail_big_box_content_sub_title'>매칭신청</p>
                        <p className='competition_detail_sub_p'></p>
                    </div>
                </div>
                <div className='competition_detail_button'>
                    <p className='competition_detail_button_p_box'>
                        <img src={Share}/>
                        공유하기
                    </p>
                    <p className='competition_detail_button_p'>참여하기</p>
                </div>
            </div>
            <div>
                <div className='competition_detail_content_nav'>
                    <div>
                        <p id='competition_detail_content_nav_first'>상세정보</p>
                        <p>매칭중인 팀</p>
                        <p>문의사항</p>
                    </div>
                    <p className='competition_detail_content_text' dangerouslySetInnerHTML={{ __html: contest.detailText }}></p>
                </div>
            </div>
        </div>
    )
}