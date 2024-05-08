import { Link } from 'react-router-dom';
import Logo from '../../img/Logo.png';
import User from '../../img/User-Circle-Single--Streamline-Core.png';
import Mate from '../../img/Align-Two-Top-Square--Streamline-Core.png';
import './Navigation.css'
import MateModal from '../MateModal/MateModal';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Swal from 'sweetalert2';

const Navigation = () => {
    const [modalOpen, setModalOpen] = useState(false);
    const [isToken, setIsToken] = useState("");
    const navigate = useNavigate();

    const toggleModal = () => {
        const userId = window.localStorage.getItem('userId');
        axios({
            method: 'get',
            url: `/participation/${userId}`
        }).then(result => {
            if (result.status === 200) {
                setModalOpen(!modalOpen);
            }
        }).catch(err => {
            if (err.response && err.response.status === 500) {
                Swal.fire({
                    title: "매칭을 사용하려면<br/>로그인을 해주세요",
                    showCancelButton: true,
                    confirmButtonText: "네",
                    cancelButtonText: "아뇨, 구경만 할게요"
                }).then(result => {
                    if (result.isConfirmed) {
                        navigate('signui')
                    }
                })
            }
        })
    };

    useEffect(() => {
        setIsToken(window.localStorage.getItem('token'));
        console.log(isToken);
    }, [])

    const logout = () => {
        setIsToken(window.localStorage.removeItem('token'));
        window.localStorage.removeItem('userId');
        navigate('/');
        window.location.reload();
    }

    return (
        <div>
            <div className='navaigation_bar'>
                <div className='navaigation_sign_bar'>
                    {isToken === undefined || !isToken ? (
                        <>
                            <Link id='navaigation_sign_bar_frist' to={'/signui'}>회원가입</Link> |
                            <Link to={'/signui'}>로그인</Link> |
                        </>
                    ) : (
                        <>
                            <Link onClick={logout}>로그아웃</Link> |
                        </>
                    )}
                    <Link>고객센터</Link>
                </div>
                <div className='navigation_main_bar'>
                    <div className='navigation_main_bar_logo_competition'>
                        <Link to={'/'}><img src={Logo} /></Link>
                        <Link>전체 공모전</Link>
                        <Link>마감 임박</Link>
                        <Link>베스트</Link>
                        <Link>접수 예정</Link>
                    </div>
                    <div className='navigation_main_bar_user_mate'>
                        <Link to={'/mypage'}>
                            <img src={User} />
                            <p>마이페이지</p>
                        </Link>
                        <Link onClick={toggleModal} className='navigation_main_bar_mate'>
                            <img src={Mate} />
                            <p>추천 메이트</p>
                        </Link>
                        {modalOpen && <MateModal />}
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Navigation;