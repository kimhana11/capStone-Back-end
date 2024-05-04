import React, { useEffect, useState } from "react";
import { Link, useNavigate } from 'react-router-dom';
import './MypageNav.css';
import axios from 'axios';
import Swal from 'sweetalert2';

export default function NavigationUser() {
    const navigate = useNavigate();
    const [userId, setUserId] = useState('');

    useEffect(() => {
        setUserId(window.localStorage.getItem('userId'));
    })

    const logoutbtn = e => {
        e.preventDefault();
        window.localStorage.removeItem('token');
        navigate('/');
    }
    
    const userDelete = e => {
        e.preventDefault();
        Swal.fire({
            title: "회원탈퇴를 진행하시겠습니까?",
            confirmButtonText: "예",
            cancelButtonText: "아니오",
            showCancelButton: true
        }).then((result) => {
            if (result.isConfirmed) {
                axios({
                    method: 'delete',
                    url: `/user/delete/${userId}`
                }).then((result) => {
                    if (result.status == 200) {
                        Swal.fire({
                            title: "회원탈퇴가 완료되었습니다"
                        }).then(()=> {
                            window.localStorage.removeItem('token');
                            navigate('/');
                        })
                    }
                })
            }
        })
    }

    return (
        <div>
            <div className="myPage_main">
                <p className="myPage_main_font_p" onClick={logoutbtn}>로그아웃</p>
                <Link className="myPage_main_font" to={'/userProfile'}>유저 프로필</Link>
                <Link className="myPage_main_font" to={'/userModify'}>회원정보 수정</Link>
                <Link className="myPage_main_font" onClick={userDelete}>회원 탈퇴</Link>
            </div>
        </div>
    );
}