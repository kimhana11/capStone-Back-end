import Modal from 'react-modal';
import './MateModal.css'
import image from '../../img/Ellipse 16.png'
import { useState } from 'react';
import { useEffect } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';

const MateModal = () => {
    const [selectedUsers, setSelectedUsers] = useState([]);
    const [userData, setUserData] = useState([]);
    const [contestData, setContestData] = useState([]);
    const [selectedContestId, setSelectedContestId] = useState(null);

    useEffect(() => {
        const userId = window.localStorage.getItem('userId');
        axios({
            method: 'get',
            url: `/participation/${userId}`
        }).then(result => {
            setContestData(result.data)
        }).catch(err => {
            if (err.response && err.response.status === 500) {
                setContestData([]);
            }
        })
    }, [])

    useEffect(() => {
        if (selectedContestId !== null) {
            const userId = window.localStorage.getItem('userId');
            axios({
                method: 'get',
                url: `/profile-stack/${selectedContestId}/${userId}`
            }).then(result => {
                setUserData(result.data)
            }).catch(err => {
                if (err.response && err.response.status === 500) {
                    setUserData([]);
                }
            })
        }
    }, [selectedContestId])

    const customStyles = {
        overlay: {
            backgroundColor: "rgba(0, 0, 0, 0)",
            zIndex: 1000,
            position: "static",
            top: 0,
            left: 0,
        },
        content: {
            width: "450px",
            height: "550px",
            zIndex: 150,
            position: "absolute",
            top: "47%",
            left: "73%",
            transform: "translate(-50%, -50%)",
            borderRadius: "1rem",
            boxShadow: "2px 2px 2px rgba(0, 0, 0, 0.25)",
            backgroundColor: "white",
            overflow: "auto",
            padding: 0
        },
    };

    const scrollToCenter = (contestId) => {
        setSelectedContestId(contestId);
        const parent = document.querySelector('.modal_mate_competition');
        const selectedParagraph = document.getElementById(contestId);
        const scrollLeft = (selectedParagraph.offsetLeft - parent.offsetWidth / 1.5 + selectedParagraph.offsetWidth / 2);
        parent.scrollTo({
            left: scrollLeft,
            behavior: 'smooth'
        });
        const paragraphs = parent.querySelectorAll('p');
        paragraphs.forEach(paragraph => {
            if (paragraph === selectedParagraph) {
                paragraph.style.backgroundColor = '#00B2FF'; // 선택된 요소
                paragraph.style.color = 'white'; // 선택된 요소
            } else {
                paragraph.style.backgroundColor = '#F3F3F3'; // 선택되지 않은 요소
                paragraph.style.color = '#D2D2D2'; // 선택되지 않은 요소
            }
        });
    };

    const handleUserClick = (userId) => {
        setSelectedUsers(prevSelectedUsers => {
            if (prevSelectedUsers.includes(userId)) {
                return prevSelectedUsers.filter(id => id !== userId); // 이미 선택된 경우 선택 해제
            } else {
                return [...prevSelectedUsers, userId]; // 새로 선택한 경우 선택 추가
            }
        });
    };

    function createChat() {
        Swal.fire({
            title: "채팅방 생성 시 필요한 정보",
            html: `
                <p id="name_p">채팅방 이름</p>
                <input id="name" class="swal2-input">
        
                <p id="password_p">비밀번호</p>
                <input type = password id="password" maxLength=10 class="swal2-input">
            `,
            showCancelButton: true,
            confirmButtonText: "채팅방 생성",
            cancelButtonText: "취소"
        }).then((result) => {

        })
    }

    return (
        <div>
            <Modal isOpen={true} style={customStyles}>
                <div className='modal_mate_top'>
                    <p>추천 메이트</p>
                    <p>AI 매칭 메이트</p>
                </div>
                <div className='modal_mate_main'>
                    {!(contestData.length === 0) && (
                        <>
                            <div className='modal_mate_competition'>
                                {contestData.map(contest => (
                                    <p key={contest.contestId} id={contest.contestId} onClick={() => scrollToCenter(contest.contestId)}>{contest.title}</p>
                                ))}
                            </div>
                            {!(userData.length === 0) && (
                                <>
                                    <div className='modal_mate'>
                                        {userData.map(user => (
                                            <div key={user.id} className={`modal_mate_user ${selectedUsers.includes(user.id) ? 'active' : 'noactive'}`} onClick={() => handleUserClick(user.id)}>
                                                <img src={image} />
                                                <div>
                                                    <p className='modal_mate_user_name'>{user.userId}</p>
                                                    <p className='modal_mate_user_content'>{user.intro}</p>
                                                    <div className='modal_mate_user_stack'>
                                                        {user.stackList.map((stack, index) => (
                                                            <p key={index}>{stack}</p>
                                                        ))}
                                                    </div>
                                                </div>
                                            </div>
                                        ))}
                                    </div>
                                    <div className='modal_mate_group_button' onClick={createChat}>
                                        <img src={image} />
                                        <img src={image} />
                                        <p>그룹 방 만들기</p>
                                    </div>
                                </>
                            )}
                            {userData.length === 0 && (
                                <>
                                    <p className='modal_mate_content'>매칭 대기열에 등록된 유저가 없습니다</p>
                                </>
                            )}
                        </>
                    )}
                    {contestData.length === 0 && (
                        <>
                            <p className='modal_mate_competition_p'>매칭 대기열에 등록된 공모전이 없습니다.</p>
                        </>
                    )}
                </div>
            </Modal>
        </div>
    )
}

export default MateModal;
