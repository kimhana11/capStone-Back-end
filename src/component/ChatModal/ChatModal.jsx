import Modal from 'react-modal';
import './ChatModal.css'
import { useState } from 'react';
import { useEffect } from 'react';
import axios from 'axios';
import Swal from 'sweetalert2';

const ChatModal = () => {

    useEffect(() => {
        const userId = window.localStorage.getItem('userId');
        axios({
            method: 'get',
            url: `/rooms/${userId}`
        }).then(result => {
            console.log(result.data);
        }).catch(err => {
            if (err.response && err.response.status === 500) {
            }
        })
    }, [])

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
            height: "450px",
            zIndex: 150,
            position: "fixed",
            top: "55%",
            left: "81%",
            transform: "translate(-50%, -50%)",
            borderRadius: "1rem",
            boxShadow: "2px 2px 2px rgba(0, 0, 0, 0.25)",
            backgroundColor: "white",
            overflow: "auto",
            padding: 0
        },
    };

    return (
        <>
            <Modal isOpen={true} style={chatStyles}>
                <div className='chat_modal_box'>
                    <div className='chat_modal'>
                        <div>
                            <p className='chat_modal_title'>제목</p>
                            <p className='chat_modal_content'>오늘 저희 할 내용은 이거 맞나요?</p>
                        </div>
                        <p className='chat_modal_time'>10 : 00</p>
                    </div>
                </div>
            </Modal>
        </>
    )
}

export default ChatModal;
