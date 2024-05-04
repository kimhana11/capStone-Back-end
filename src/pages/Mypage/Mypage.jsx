import React from "react";
import './Mypage.css';
import MypageNav from '../../component/MypageNav/MypageNav.jsx';
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

export default function MyPage() {
    const navigate = useNavigate();
    useEffect(() => {
        const istoken = window.localStorage.getItem('token');
        console.log(istoken)
        if (istoken === null) {
            navigate('/signui');
        }
    },[])

    return (
        <div>
            <MypageNav />
            <div></div>
            <div></div>
            <div></div>
            <div></div>
            <div></div>
        </div>
    );
}