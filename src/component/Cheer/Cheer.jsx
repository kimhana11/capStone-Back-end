import { useState, useEffect } from 'react';
import './Cheer.css';
import Swal from 'sweetalert2';

const Cheer = () => {
    const [username, setUsername] = useState(null);
    const [ws, setWs] = useState(null);
    const [cheers, setCheers] = useState([]);
    const [inputValue, setInputValue] = useState('');

    useEffect(() => {
        setUsername(window.localStorage.getItem('name'));
        const websocket = new WebSocket('ws://localhost:8080/ws'); // 웹소켓 연결
        setWs(websocket);

        websocket.onopen = () => {
            console.log('WebSocket connected'); // 웹소켓 연결됨
        };

        websocket.onmessage = (event) => {
            if (isValidJson(event.data)) {
                const message = JSON.parse(event.data);
                console.log(message)
                if (message.type === 'cheer') {
                    setCheers(message.cheeringMessages.map(cheer => `${cheer.senderName} : ${cheer.message}`));
                }
            }
        };

        websocket.onerror = (error) => {
            console.error('WebSocket error:', error); // 웹소켓 에러 핸들링
        };
    }, []);

    const handleInputChange = (event) => {
        setInputValue(event.target.value);
    };

    const handleButtonClick = () => {
        const token = window.localStorage.getItem('token');
            const cheerMessage = {
                senderName: username,
                message: inputValue.trim()
            };
            ws.send(JSON.stringify(cheerMessage));
            setInputValue('');
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
            <div className='cheer_box'>
                <div className='cheer_div'>
                    {cheers.map((cheer, index) => (
                        <div key={index} className='cheer_user_box'>
                            <p className='cheer_user_content'>{cheer}</p>
                        </div>
                    ))}
                </div>
                <div className='cheer_input_box'>
                    <input placeholder='여러분의 응원을 담아 적어보세요.' className='cheer_input' value={inputValue} onChange={handleInputChange} />
                    <button className='cheer_button' onClick={handleButtonClick}>등록</button>
                </div>
            </div>
        </>
    )
}

export default Cheer;