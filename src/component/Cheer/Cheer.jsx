import { useState, useEffect } from 'react';
import './Cheer.css';

const Cheer = () => {
    useEffect(() => {
          const ws = new WebSocket('ws://localhost:8080/ws'); // 웹소켓 연결
    
          ws.onopen = () => {
            console.log('WebSocket connected'); // 웹소켓 연결됨
          };
    
          ws.onmessage = (event) => {
          };
      }, []);
    return (
        <>
            <div className='cheer_box'>
                <div className='cheer_div'>
                    <div className='cheer_user_box'>
                        <p className='cheer_user_name'>엄복동</p> | <p className='cheer_user_content'>남서울 화이팅!!</p>
                    </div>
                    <div className='cheer_user_box'>
                        <p className='cheer_user_name'>노홍철</p> | <p className='cheer_user_content'>행복하자 우리 모두</p>
                    </div>
                    <div className='cheer_user_box'>
                        <p className='cheer_user_name'>노홍철</p> | <p className='cheer_user_content'>행복하자 우리 모두ahaefnawsf</p>
                    </div>
                </div>
                <div className='cheer_input_box'>
                    <input placeholder='여러분의 응원을 담아 적어보세요.' className='cheer_input' />
                    <button className='cheer_button'>등록</button>
                </div>
            </div>
        </>
    )
}

export default Cheer;