import { Link } from 'react-router-dom';
import Logo from '../../img/Logo.png';
import User from '../../img/User-Circle-Single--Streamline-Core.png';
import Mate from '../../img/Align-Two-Top-Square--Streamline-Core.png';
import './Navigation.css'

const Navigation = () => {
    return (
        <div>
            <div>
                <div className='navaigation_sign_bar'>
                    <Link id='navaigation_sign_bar_frist'>회원가입</Link>|
                    <Link>로그인</Link>|
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
                        <Link>
                            <img src={User}/>
                            <p>마이페이지</p>
                        </Link>
                        <Link>
                            <img src={Mate}/>
                            <p>추천 메이트</p>
                        </Link>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Navigation;