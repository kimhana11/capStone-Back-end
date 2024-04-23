import React, { useEffect, useState } from 'react';
import './CompetitionDetail.css';
import axios from "axios";
import { useLocation } from 'react-router-dom';

export default function CompetitionDetail() {
    const [contestId, setContestId] = useState('');
    const [contest, setContest] = useState('');
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
            setContest(result.data);
        })
        axios({
            method: 'post',
            url: '/viewPlus',
            data : { id: id }
        }).then((result) => {
            console.log(result)
        })
    }, [])


    return (
        <div className='competition_detail'>
            <div className='competition_detail_background'></div>
            <img src={contest.image} className='competition_detail_img'/>
            <div className='competition_detail_big_box'></div>
            <div>
                <div className='competition_detail_content_nav'>
                    <p>상세정보</p>
                    <p>매칭중인 팀</p>
                    <p>문의사항</p>
                </div>
                <p>{contest.detailText}</p>
            </div>
        </div>
    )
}