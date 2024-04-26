import React, { useState } from "react";
import Modal from "react-modal";
import DaumPostcode from "react-daum-postcode";
import './Post.css';

const Post = () => {
    const [zipCode, setZipcode] = useState("");
    const [roadAddress, setRoadAddress] = useState("");
    const [detailAddress, setDetailAddress] = useState("");
    const [isOpen, setIsOpen] = useState(false);

    const completeHandler = (data) => {
        setZipcode(data.zonecode);
        setRoadAddress(data.roadAddress);
        setIsOpen(false);
    };

    const customStyles = {
        overlay: {
            backgroundColor: "rgba(0,0,0,0.5)",
        },
        content: {
            left: "0",
            margin: "auto",
            width: "500px",
            height: "600px",
            padding: "0",
            overflow: "hidden",
        },
    };

    const toggle = (event) => {
        event.preventDefault(); // 기본 동작 막기
        if (!isOpen) {
            setIsOpen(true);
        } else {
            setIsOpen(false);
        }
    };

    const changeHandler = (e) => {
        setDetailAddress(e.target.value);
    };

    return (
        <div>
            <input value={zipCode} className="zip__input" readOnly placeholder="우편번호" id="house" />
            <button onClick={(event) => toggle(event)} className="zip__box">우편번호 찾기</button>
            <br />
            <input value={roadAddress} readOnly placeholder="도로명 주소" className="road__input" id="house_second" />
            <br />
            <Modal isOpen={isOpen} ariaHideApp={false} style={customStyles}>
                <DaumPostcode onComplete={completeHandler} height="100%" />
                <button onClick={() => setIsOpen(false)} className="outbutton">닫기</button>
            </Modal>
            <input type="text" onChange={changeHandler} value={detailAddress} placeholder="상세주소" className="detail__input" id="house_third" />
        </div>
    );
};

export default Post;