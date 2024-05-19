import { Link, useNavigate } from 'react-router-dom';
import { useState, useEffect } from 'react';
import './Main.css';
import Logo from '../../img/Logo.png';
import CIO from '../../img/Group 79.png';
import CIC from '../../img/Group 80.png';
import axios from 'axios';
import ChatModal from '../../component/ChatModal/ChatModal.jsx';
import SimpleSlider from '../../component/SimpleSlider/SimpleSlider';
import Swal from 'sweetalert2';

const Main = () => {
    const navigate = useNavigate();
    const [modalOpen, setModalOpen] = useState(false);
    const [competitionData, setCompetitionData] = useState([]);
    const [bestCompetitionData, setBestCompetitionData] = useState([]);
    const currentPage = 1;
    const competitionsPerPage = 4;
    const bestcompetitionsPerPage = 5;
    const currentDate = new Date(); // 현재 날짜 가져오기
    const bestIndexOfLastCompetition = currentPage * bestcompetitionsPerPage;
    const bestIndexOfFirstCompetition = bestIndexOfLastCompetition - bestcompetitionsPerPage;
    const indexOfLastCompetition = currentPage * competitionsPerPage;
    const indexOfFirstCompetition = indexOfLastCompetition - competitionsPerPage;
    const currentCompetitions = competitionData.slice(indexOfFirstCompetition, indexOfLastCompetition);
    const bestCurrentCompetitions = bestCompetitionData.slice(bestIndexOfFirstCompetition, bestIndexOfLastCompetition);

    useEffect(() => {
        axios({
            method: 'get',
            url: '/contestData'
        }).then(result => {
            console.log(result);
            const sortedData = result.data.sort((a, b) => b.views - a.views);
            setCompetitionData(result.data);
            setBestCompetitionData(sortedData);
        })
    }, [])

    const categorizeDate = (receptionPeriod) => {
        const [start, end] = receptionPeriod.split(' ~ ').map(dateStr => {
            const [year, month, day] = dateStr.split(/[-.]/);
            return new Date(year, month - 1, day);
        });
        const today = new Date();
        const oneWeekBeforeEnd = new Date(end);
        oneWeekBeforeEnd.setDate(oneWeekBeforeEnd.getDate() - 7); // 마감 1주일 전 날짜 계산

        if (today > end) {
            // 현재 날짜가 마감 날짜를 지난 경우
            return '마감';
        } else if (today >= oneWeekBeforeEnd) {
            // 현재 날짜가 마감 날짜 기준 1주일 전 이후인 경우
            return '마감 임박';
        } else if (today >= start && today <= end) {
            // 현재 날짜가 접수 기간에 속하는 경우
            return '접수중';
        } else if (today < start) {
            // 현재 날짜가 접수 기간 이전인 경우
            return '접수 예정';
        }
    };

    const toggleModal = () => {
        const userId = window.localStorage.getItem('userId');
        axios({
            method: 'get',
            url: `/rooms/${userId}`
        }).then(result => {
            if (result.status === 200) {
                setModalOpen(!modalOpen);
            }
        }).catch(err => {
            if (err.response && err.response.status === 500) {
                Swal.fire({
                    title: "채팅을 사용하려면<br/>로그인을 해주세요",
                    showCancelButton: true,
                    confirmButtonText: "네",
                    cancelButtonText: "아뇨, 구경만 할게요"
                }).then(result => {
                    if (result.isConfirmed) {
                        navigate('signui')
                    }
                })
            }
        })
    };

    return (
        <div>
            <SimpleSlider />
            <div className='main_who'>
                <div>
                    <Link><img src='' /></Link>
                    <p>누구나</p>
                </div>
                <div>
                    <Link><img src='' /></Link>
                    <p>초등학생</p>
                </div>
                <div>
                    <Link><img src='' /></Link>
                    <p>중/고등학생</p>
                </div>
                <div>
                    <Link><img src='' /></Link>
                    <p>대학생</p>
                </div>
                <div>
                    <Link><img src='' /></Link>
                    <p>대학원생</p>
                </div>
                <div>
                    <Link><img src='' /></Link>
                    <p>직장인</p>
                </div>
                <div>
                    <Link><img src='' /></Link>
                    <p>외국인</p>
                </div>
                <div>
                    <Link><img src='' /></Link>
                    <p>기타</p>
                </div>
            </div>
            <div className='main_component'>
                <p>마감 임박 공모전</p>
                <div>
                    {currentCompetitions.map(competition => (
                        categorizeDate(competition.receptionPeriod) === '마감 임박' ? (
                            <Link to={'/competitionDetail'} state={{ id: competition.id }}>
                                <div>
                                    <img className='main_component_img' src={competition.image} />
                                    <p className='main_component_host'>{competition.host}</p>
                                    <p className='main_component_title'>{competition.title}</p>
                                </div>
                            </Link>
                        ) : (
                            <div className='main_empty'>
                                <img src={Logo} className='main_empty_img' />
                                <p>공모전 정보가 없습니다</p>
                            </div>
                        )
                    ))}
                </div>
            </div>
            <div className='main_component'>
                <p>베스트 공모전</p>
                <div className='main_component_div_box'>
                    <div>
                        {bestCurrentCompetitions.slice(0, 1).map(competition => {
                            const end = new Date(competition.receptionPeriod.split(' ~ ')[1]);
                            const daysRemaining = categorizeDate(competition.receptionPeriod) === '마감' ? 0 : Math.ceil((end - currentDate) / (1000 * 60 * 60 * 24));
                            return (
                                <div className='main_component_best_box_best' key={competition.id}>
                                    <Link to={'/competitionDetail'} state={{ id: competition.id }}>
                                        <img className='main_component_best_box_img' src={competition.image} />
                                    </Link>
                                    <div>
                                        <div className='main_component_best_box_best_p_box'>
                                            <p>D - {daysRemaining}</p>
                                            <p>인기 급상승</p>
                                        </div>
                                        <p className='main_component_best_box_best_host'>{competition.host}</p>
                                        <Link to={'/competitionDetail'} state={{ id: competition.id }} className='main_component_best_box_best_link'>
                                            <p className='main_component_best_box_best_title'>{competition.title}</p>
                                        </Link>
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                </div>
                <div>
                    {bestCurrentCompetitions.slice(1).map(competition => (
                        <Link to={'/competitionDetail'} state={{ id: competition.id }}>
                            <div>
                                <div>
                                    <img className='main_component_img' src={competition.image} />
                                    <p className='main_component_host'>{competition.host}</p>
                                    <p className='main_component_title'>{competition.title}</p>
                                </div>
                            </div>
                        </Link>
                    ))}
                </div>
            </div>
            <div className='main_component'>
                <p>접수 예정 공모전</p>
                <div>
                    {currentCompetitions.map(competition => (
                        categorizeDate(competition.receptionPeriod) === '접수 예정' ? (
                            <Link to={'/competitionDetail'} state={{ id: competition.id }}>
                                <div>
                                    <img className='main_component_img' src={competition.image} />
                                    <p className='main_component_host'>{competition.host}</p>
                                    <p className='main_component_title'>{competition.title}</p>
                                </div>
                            </Link>
                        ) : (
                            <div className='main_empty'>
                                <img src={Logo} className='main_empty_img' />
                                <p>공모전 정보가 없습니다</p>
                            </div>
                        )
                    ))}
                </div>
            </div>
            <div className='navigation_main_chat'>
                <img src={modalOpen ? CIC : CIO}  className='navigation_main_chat_icon' onClick={toggleModal} />
                {modalOpen && <ChatModal />}
            </div>
        </div>
    )
}

export default Main;