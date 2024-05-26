import Modal from 'react-modal';
import './ChatModal.css';
import { useState, useEffect, useRef } from 'react';
import axios from 'axios';
import Arrow_Button from '../../img/Arrow-Button-Left-3--Streamline-Ultimate.png'

const ChatModal = (onRequestClose) => {
    const [rooms, setRooms] = useState([]);
    const [selectedRoomId, setSelectedRoomId] = useState(null);
    const [selectedRoomName, setSelectedRoomName] = useState(null);
    const [socket, setSocket] = useState(null);
    const [messages, setMessages] = useState([]);
    const [inputMessage, setInputMessage] = useState('');
    const [username, setUsername] = useState('');
    const messagesContainerRef = useRef(null);

    useEffect(() => {
        const userId = window.localStorage.getItem('userId');
        setUsername(window.localStorage.getItem('userId'));
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
    };

    const handleSendMessage = () => {
        if (socket && inputMessage.trim() !== '') {
            const messageDto = {
                message: inputMessage,
                senderId: username,
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
                                    <p className='chat_room_top_state'>방 삭제</p>
                                    <p className='chat_room_top_state'>팀 확정</p>
                                </div>
                            </div>
                            <div ref={messagesContainerRef} className='chat_room_messagebox'>
                                {messages.map((message, index) => (
                                    <div className={`${message.senderId === username ? 'chat_room_message_me_width' : ''}`}>
                                        <p key={index} className={`${message.senderId === username ? 'chat_room_message_me' : 'chat_room_message'}`}>
                                            {message.senderId === username ? message.message : message.senderId + " : " + message.message}
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
