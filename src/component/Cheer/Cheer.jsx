import { useState, useEffect } from 'react';
import './Cheer.css';

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
            const message = JSON.parse(event.data);
            console.log(message)
            if (message.type === 'cheer') {
                setCheers(prevCheers => [...prevCheers, message]);
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
        if (inputValue.trim() !== '' && ws) {
            const cheerMessage = {
                type: 'cheer',
                userName: username,
                content: inputValue.trim()
            };
            ws.send(JSON.stringify(cheerMessage));
            setInputValue('');
        }
    };

    return (
        <>
            <div className='cheer_box'>
                <div className='cheer_div'>
                    {cheers.map((cheer, index) => (
                        <div key={index} className='cheer_user_box'>
                            <p className='cheer_user_name'>{cheer.userName}</p> | <p className='cheer_user_content'>{cheer.content}</p>
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