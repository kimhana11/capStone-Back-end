import React, { useEffect, useState } from 'react';
import './CompetitionDetail.css';
import axios from "axios";
import { useLocation, useNavigate } from 'react-router-dom';
import Share from '../../img/Share--Streamline-Nova.png'
import Swal from 'sweetalert2';

export default function CompetitionDetail() {
    const [contestId, setContestId] = useState('');
    const [contest, setContest] = useState('');
    const [userId, setUserId] = useState("");
    const [userProfile, setUserProfile] = useState([]);
    const [daysRemaining, setDaysRemaining] = useState(0);
    const location = useLocation();
    const navigate = useNavigate();

    useEffect(() => {
        const id = window.localStorage.getItem('contestId');
        const userId = window.localStorage.getItem('userId');
        setUserId(userId);
        console.log(id)

        setContestId(location.state.id);
        window.localStorage.setItem('contestId', location.state.id);

        axios({
            method: 'get',
            url: `/contestdetail/${id}`
        }).then(result => {
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
        axios({
            method: 'get',
            url: `/user-profile/${userId}`
        }).then(result => {
            console.log(result.data)
            setUserProfile(result.data)
        }).catch(err => {
            if (err.response && err.response.status === 500) {
                setUserProfile([]);
            }
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

    function Matching() {
        if (!(userId === null) && !(userProfile.length === 0)) {
            Swal.fire({
                title: "팀원 매칭 시 필요한 정보",
                html: `
                    <p id="stack_p">팀원에게 원하는 기술 스택</p>
                    <input id="stack" class="swal2-input" placeholder="ex) 자바, 스프링">
            
                    <p id="additional_p">기타(하고싶은 말)</p>
                    <input id="additional" maxLength=20 class="swal2-input" placeholder="어떤 아이디어가 있다 등등">
                `,
                showCancelButton: true,
                confirmButtonText: "팀원찾기",
                cancelButtonText: "취소"
            }).then((result) => {
                if (result.isConfirmed) {
                    const stackInput = document.querySelector('#stack').value;
                    const additional = document.querySelector('#additional').value;

                    const stackList = stackInput.split(',').map(item => item.trim());

                    const participationParam = {
                        userId: userId,
                        contestId: contestId,
                        stackList: stackList,
                        additional: additional
                    };

                    try {
                        axios({
                            method: 'post',
                            url: 'participation',
                            data: participationParam
                        }).then(result => {
                            if (result.status === 200) {
                                Swal.fire({
                                    title: "매칭열에 추가됐습니다"
                                })
                            }
                        }).catch(error => {
                            if (error.response && error.response.status === 400) {
                                Swal.fire({
                                    title: "이미 대기열에 있습니다<br/>대기열에서 취소하시겠습니까?",
                                    showCancelButton: true,
                                    confirmButtonText: "확인",
                                    cancelButtonText: "취소"
                                }).then((result) => {
                                    if (result.isConfirmed) {
                                        try {
                                            axios({
                                                method: 'delete',
                                                url: `/participation/${contestId}/${userId}`
                                            }).then(result => {
                                                if (result.status === 200) {
                                                    Swal.fire({
                                                        title: "매칭열에서 제외되셨습니다"
                                                    })
                                                }
                                            })
                                        } catch (err) {
                                            console.error(err);
                                        }
                                    }
                                });
                            }
                        })
                    } catch (err) {
                        console.error(err);
                    }
                }
            });
        } else if (!(userId === null) && (userProfile.length === 0)) {
            Swal.fire({
                title: "매칭을 사용하려면<br/>유저 프로필을 설정해주세요",
                showCancelButton: true,
                confirmButtonText: "네",
                cancelButtonText: "아뇨, 구경만 할게요"
            }).then(result => {
                if (result.isConfirmed) {
                    navigate('/mypage')
                }
            })
        } else {
            Swal.fire({
                title: "매칭을 사용하려면<br/>로그인을 해주세요",
                showCancelButton: true,
                confirmButtonText: "네",
                cancelButtonText: "아뇨, 구경만 할게요"
            }).then(result => {
                if (result.isConfirmed) {
                    navigate('/signui')
                }
            })
        }
    }

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
                        <img src={Share} />
                        공유하기
                    </p>
                    <p className='competition_detail_button_p' onClick={Matching}>참여하기</p>
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
        </div >
    )
}