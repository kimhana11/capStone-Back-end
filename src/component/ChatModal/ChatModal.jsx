import Modal from 'react-modal';
import './ChatModal.css';
import { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import Arrow_Button from '../../img/Arrow-Button-Left-3--Streamline-Ultimate.png'
import Swal from 'sweetalert2';

const ChatModal = (onRequestClose) => {
    const [rooms, setRooms] = useState([]);
    const [user, setUser] = useState('');
    const [contestId, setContestId] = useState("");
    const [leaderId, setLeaderId] = useState("");
    const [selectedRoomId, setSelectedRoomId] = useState(null);
    const [selectedRoomName, setSelectedRoomName] = useState(null);
    const [socket, setSocket] = useState(null);
    const [messages, setMessages] = useState([]);
    const [inputMessage, setInputMessage] = useState('');
    const messagesContainerRef = useRef(null);

    useEffect(() => {
        const userId = window.localStorage.getItem('userId');
        setUser(window.localStorage.getItem('userId'));
        axios.get(`/rooms/${userId}`).then(result => {
            setRooms(result.data);
        }).catch(err => {
            console.error('Error fetching rooms:', err);
        });
    }, []);

    useEffect(() => {
        if (selectedRoomId !== null) {
            const ws = new WebSocket('ws://localhost:8080/ws');

            ws.onopen = () => {
                console.log('WebSocket connected');
                ws.send(`/sub/chat/${selectedRoomId}`);
            };

            ws.onmessage = (event) => {
                const message = event.data;
                console.log('Received raw message:', message); // 수신된 메시지 로그 출력
                try {
                    if (isValidJson(message)) {
                        const parsedMessage = JSON.parse(message);
                        if (parsedMessage.type === 'chatHistory') {
                            setMessages(parsedMessage.content);
                        } else {
                            setMessages(prevMessages => [...prevMessages, parsedMessage]);
                        }
                    }
                } catch (error) {
                    console.error('Error parsing message:', error);
                    setMessages(prevMessages => [...prevMessages, { content: message }]);
                }
            };

            ws.onclose = () => {
                console.log('WebSocket disconnected');
            };

            setSocket(ws);
        }
    }, [selectedRoomId]);

    useEffect(() => {
        if (messagesContainerRef.current) {
            messagesContainerRef.current.scrollTop = messagesContainerRef.current.scrollHeight;
        }
    }, [messages]);

    const handleCloseModal = () => {
        if (socket) {
            socket.close();
        }
        onRequestClose();
    };

    const handleRoomClick = (roomId, name) => {
        setSelectedRoomId(roomId);
        setSelectedRoomName(name);

        axios({
            method: 'get',
            url: `/room/${roomId}`
        }).then(result => {
            setLeaderId(result.data.leaderId);
            setContestId(result.data.contestId);
        })
    };

    const handleSendMessage = () => {
        if (socket && inputMessage.trim() !== '') {
            const messageDto = {
                message: inputMessage,
                senderId: user,
                roomId: selectedRoomId,
            };
            setMessages(prevMessages => [...prevMessages, messageDto]);
            socket.send(JSON.stringify(messageDto));
            setInputMessage('');
        }
    };

    const roomlist = () => {
        if (socket) {
            socket.close();
        }
        setSelectedRoomId(null);
        setMessages([]);
    }

    const deleteRoom = () => {
        axios({
            method: 'delete',
            url: `/delete-room/${selectedRoomId}`
        }).then(
            Swal.fire({
                title: "채팅방이 삭제되었습니다."
            }).then(
                window.location.reload()
            )
        )
    }

    const updateRoom = () => {
        const updateRoom = {
            contestId: contestId,
            userId: leaderId,
            roomId: selectedRoomId,
            status: true
        };
        axios({
            method: 'post',
            url: '/room-update/status',
            data: updateRoom
        }).then(result => {
            if (result.status === 200) {
                Swal.fire({
                    title: "팀이 확정되었습니다."
                })
            }

        })
    }

    const userReview = (message) => {
        Swal.fire({
            title: "유저의 평점과 평가를 작성해주세요",
            html: `
                <input id="review_rate" type="range" min="1" max="5" step="0.1" oninput="document.getElementById('value1').innerHTML=this.value;">
                <div id="value1"></div>
                <input id="review_content" class="swal2-input" placeholder="ex) 같이 플젝해서 좋았어요~">
            `,
            showCancelButton: true,
            confirmButtonText: "리뷰 작성",
            cancelButtonText: "취소"
        }).then((result) => {
            const review_rate = document.querySelector('#review_rate').value;
            const review_content = document.querySelector('#review_content').value;
            const ReviewRequestDto = {
                content: review_content,
                rate: review_rate,
                reviewerId: user,
                reviewedUserId: message.senderId,
                roomId: selectedRoomId,
                contestId: contestId
            };
            if (result.isConfirmed) {
                axios({
                    method: 'post',
                    url: '/user-review',
                    data: ReviewRequestDto
                }).then((result) => {
                    if (result.status === 200) {
                        Swal.fire({
                            title: "리뷰가 작성되었습니다."
                        })
                    }
                }).catch(err => {
                    if (err.response && err.response.status === 500) {
                        Swal.fire({
                            title: "팀 확정을 한 후 <br/> 리뷰 작성이 가능합니다"
                        })
                    }
                })
            }
        })
    }

    const chatStyles = {
        overlay: {
            backgroundColor: "rgba(0, 0, 0, 0)",
            zIndex: 1000,
            position: "static",
            top: 0,
            left: 0,
        },
        content: {
            width: "400px",
            height: "500px",
            zIndex: 150,
            position: "fixed",
            top: "53%",
            left: "81%",
            transform: "translate(-50%, -50%)",
            borderRadius: "1rem",
            boxShadow: "2px 2px 2px rgba(0, 0, 0, 0.25)",
            backgroundColor: "white",
            overflow: "auto",
            padding: 0
        },
    };

    const isValidJson = (str) => {
        try {
            JSON.parse(str);
            return true;
        } catch (e) {
            return false;
        }
    };

    return (
        <>
            <Modal isOpen={true} onRequestClose={handleCloseModal} style={chatStyles}>
                <div className='chat_modal_box'>
                    {selectedRoomId === null ? (
                        <>
                            {rooms.map(room => (
                                <div key={room.roomId} className='chat_modal' onClick={() => handleRoomClick(room.roomId, room.name)}>
                                    <div>
                                        <p className='chat_modal_title'>{room.name}</p>
                                        <p className='chat_modal_content'>{room.lastMessage || '채팅 메시지가 없습니다'}</p>
                                    </div>
                                    <p className='chat_modal_time'>{room.lastMessageTimeStamp || ''}</p>
                                </div>
                            ))}
                        </>
                    ) : (
                        <>
                            <div className='chat_room_top'>
                                <div className='chat_room_top_first'>
                                    <img src={Arrow_Button} onClick={() => roomlist()} />
                                    <p className='chat_room_top_name'>{selectedRoomName}</p>
                                </div>
                                <div className='chat_room_top_second'>
                                    <p className='chat_room_top_state' onClick={() => deleteRoom()}>방 삭제</p>
                                    <p className='chat_room_top_state' onClick={() => updateRoom()}>팀 확정</p>
                                </div>
                            </div>
                            <div ref={messagesContainerRef} className='chat_room_messagebox'>
                                {messages.map((message, index) => (
                                    <div className={`${message.senderName === user ? 'chat_room_message_me_width' : 'chat_room_message_other_width'}`}>
                                        <div>
                                            <img className={`${message.senderName === user ? '' : 'chat_room_message_other_profile'}`} onClick={() => userReview(message)} />
                                            <p key={index}>{message.senderName === user ? '' : message.senderName}</p>
                                        </div>
                                        <p key={index} className={`${message.senderName === user ? 'chat_room_message_me' : 'chat_room_message'}`}>
                                            {message.senderName === user ? message.message : message.message}
                                        </p>
                                    </div>
                                ))}
                            </div>
                            <div className='chat_room_input_box'>
                                <input
                                    type="text"
                                    value={inputMessage}
                                    placeholder='채팅을 입력하세요'
                                    onChange={(e) => setInputMessage(e.target.value)}
                                />
                                <button onClick={handleSendMessage}>전송</button>
                            </div>
                        </>
                    )}
                </div>
            </Modal>
        </>
    );
}

export default ChatModal;
