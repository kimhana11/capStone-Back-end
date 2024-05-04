import React, { useEffect, useState } from "react";
import MypageNav from '../../component/MypageNav/MypageNav.jsx';
import Post from '../../component/Post/Post.jsx';
import './UserModify.css';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser, faLock, faPerson, faEnvelope, faPhone, faHouse, faVenusMars, faUsers } from "@fortawesome/free-solid-svg-icons";
import axios from 'axios';
import Swal from 'sweetalert2';

export default function UserModify() {
    const UserUpdateRequest = {}

    const [userId, setUserId] = useState('');
    const [pw, setPw] = useState('');
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [address, setAddress] = useState('');
    const [gender, setGender] = useState('');
    const [tendency, setTendency] = useState('');

    const [isPw, setIsPw] = useState(false);
    const [isName, setIsName] = useState(false);
    const [isPhone, setIsPhone] = useState(false);

    const [pwMessage, setPwMessage] = useState('');
    const [nameMessage, setNameMessage] = useState('');
    const [phoneMessage, setPhoneMessage] = useState('');


    const [selectedGender, setSelectedGender] = useState('');
    const [selectedTendency, setSelectedTendency] = useState("ISTJ");

    const handleGenderChange = (e) => {
        setSelectedGender(e.target.value);
    };

    const handleTendencyChange = (e) => {
        setSelectedTendency(e.target.value);
    };

    useEffect(() => {
        setUserId(window.localStorage.getItem('userId'));
    })

    const checkPut = e => {
        e.preventDefault();

        let email_data = document.querySelector("#UserModify_Form_email_id");
        let email_Seconddata = document.querySelector("#domain-txt");
        let phoneFrist = document.querySelector("#box_list_phone");
        let phoneSecondCheck = document.querySelector("#phone_second");
        let phoneThirdCheck = document.querySelector("#phone_third");
        let houseCheck = document.querySelector("#house");
        let houseSecondCheck = document.querySelector("#house_second");
        let houseThirdCheck = document.querySelector("#house_third");

        setEmail(email_data.value.trim() + '@' + email_Seconddata.value.trim());
        setAddress(houseCheck.value.trim() + '-' + houseSecondCheck.value.trim() + ' ' + houseThirdCheck.value.trim());
        setPhone(phoneFrist.value.trim() + '-' + phoneSecondCheck.value.trim() + '-' + phoneThirdCheck.value.trim());
        setGender(selectedGender);
        setTendency(selectedTendency);

        if (isPw === true && isName === true && isPhone === true && !(email_data.value.trim() === "") && !(email_Seconddata.value.trim() === "")
            && !(houseCheck.value.trim() === "") && !(houseSecondCheck.value.trim() === "") && !(houseThirdCheck.value.trim() === "")) {
            const signupButton = document.querySelector(".userModify_button_box_next");
            const submitButton = document.querySelector(".userModify_button_box_span");
            signupButton.style.display = 'none';
            submitButton.style.display = 'block';

            document.querySelectorAll('input').forEach(input => {
                input.disabled = true;
            });
            document.querySelectorAll('select').forEach(select => {
                select.disabled = true;
            });
        }
    }

    const putUser = e => {
        e.preventDefault();

        Swal.fire({
            title: "회원정보를 수정하시겠습니까?",
            confirmButtonText: "예",
            cancelButtonText: "아니오",
            showCancelButton: true
        }).then((result) => {
            if (result.isConfirmed) {
                UserUpdateRequest['userId'] = userId;
                UserUpdateRequest['newPassword'] = pw;
                UserUpdateRequest['newUsername'] = name;
                UserUpdateRequest['newEmail'] = email;
                UserUpdateRequest['newPhone'] = phone;
                UserUpdateRequest['newAddress'] = address;
                UserUpdateRequest['newGender'] = gender;
                UserUpdateRequest['newTendency'] = tendency;

                axios({
                    method: 'put',
                    url: '/user/update',
                    data: UserUpdateRequest
                }).then((result) => {
                    if (result.status == 200) {
                        Swal.fire({
                            title: "회원정보가 수정되었습니다"
                        })
                    }
                })
            }
        })
    }


    const onChangePassword = (e) => {
        const passwordRegex = /^(?=.*[a-zA-Z])(?=.*[!@#$%^*+=-])(?=.*[0-9]).{8,25}$/;
        const passwordCurrent = e.target.value;
        setPw(passwordCurrent);

        if (!passwordRegex.test(passwordCurrent)) {
            setPwMessage('숫자+영문자+특수문자 조합으로 8자리 이상 입력해주세요!');
            setIsPw(false);
        } else {
            setPwMessage('안전한 비밀번호에요 : )');
            setIsPw(true);
        }
    };

    const onChangeName = (e) => {
        setName(e.target.value);
        if (e.target.value.length < 2) {
            setNameMessage('2글자 이상으로 입력해주세요.');
            setIsName(false);
        } else {
            setNameMessage('올바른 이름 형식입니다 :)');
            setIsName(true);
        }
    };

    const domainListEl = document.querySelector('#domain-list');
    const domainInputEl = document.querySelector('#domain-txt');

    if (domainListEl && domainInputEl) {
        domainListEl.addEventListener('change', (event) => {
            if (event.target.value !== "type") {
                domainInputEl.value = event.target.value;
                domainInputEl.disabled = true;
            } else {
                domainInputEl.value = "";
                domainInputEl.disabled = false;
            }
        });
    }

    const onChangePhone = e => {
        const phoneRegex = /^[0-9]{4}$/;
        const phoneCurrent = e.target.value;
        setPhone(phoneCurrent);

        if (!phoneRegex.test(phoneCurrent)) {
            setPhoneMessage('숫자 4자리로 입력해주세요!');
            setIsPhone(false);
        } else {
            setPhoneMessage('올바른 전화번호입니다.');
            setIsPhone(true);
        }
    };


    return (
        <div>
            <MypageNav />
            <div className='UserModify_Main'>
                <div>회원 정보 수정</div>
                <form className='UserModify_Form'>
                    <div className='UserModify_Form_dbox_icon'>
                        <div className='UserModify_Form_Main'>
                            <FontAwesomeIcon icon={faUser} />
                            <div>아이디</div>
                        </div>
                        <div className='UserModify_Form_Main'>
                            <FontAwesomeIcon icon={faLock} />
                            <div>비밀번호</div>
                        </div>
                        <div className='UserModify_Form_Main'>
                            <FontAwesomeIcon icon={faPerson} />
                            <div>이름</div>
                        </div>
                        <div className='UserModify_Form_Main'>
                            <FontAwesomeIcon icon={faEnvelope} />
                            <div>이메일</div>
                        </div>
                        <div className='UserModify_Form_Main'>
                            <FontAwesomeIcon icon={faPhone} />
                            <div>전화번호</div>
                        </div>
                        <div className='UserModify_Form_Main'>
                            <FontAwesomeIcon icon={faHouse} />
                            <div>거주지</div>
                        </div>
                        <div className='UserModify_Form_Main'>
                            <FontAwesomeIcon icon={faVenusMars} />
                            <div>성별</div>
                        </div>
                        <div className='UserModify_Form_Main'>
                            <FontAwesomeIcon icon={faUsers} />
                            <div>성향</div>
                        </div>
                    </div>
                    <div className='UserModify_Form_dbox'>
                        <input type="text" className='UserModify_Form_id' value={userId} />
                        <div className="UserModify_Form_dbox_box">
                            <input type="password" className='UserModify_Form_pw' onChange={onChangePassword} />
                            <br />
                            {pw.length > 0 && (<span className={`message ${isPw ? 'success' : 'error'}`}>{pwMessage}</span>)}
                        </div>
                        <div className="UserModify_Form_dbox_box">
                            <input type="text" className='UserModify_Form_name' onChange={onChangeName} />
                            <br />
                            {name.length > 0 && <span className={`message ${isName ? 'success' : 'error'}`}>{nameMessage}</span>}
                        </div>
                        <div className="UserModify_Form_dbox_mailbox">
                            <input type="text" id="UserModify_Form_email_id" />
                            <p className="emailAnd">@</p>
                            <div className="mail_box">
                                <input class="mail_box" id="domain-txt" type="text" placeholder="직접 입력" />
                                <select class="mail_box" id="domain-list">
                                    <option value="type">직접 입력</option>
                                    <option value="naver.com">naver.com</option>
                                    <option value="gmail.com">gmail.com</option>
                                    <option value="hanmail.net">hanmail.net</option>
                                    <option value="nate.com">nate.com</option>
                                    <option value="kakao.com">kakao.com</option>
                                </select>
                            </div>
                        </div>
                        <div className="UserModify_Form_dbox_box">
                            <div className='UserModify_Form_phone'>
                                <select class="box" id="box_list_phone">
                                    <option value="010" selected>010</option>
                                    <option value="011">011</option>
                                    <option value="016">016</option>
                                    <option value="017">017</option>
                                    <option value="018">018</option>
                                    <option value="019">019</option>
                                </select>
                                <p className="phone_between">-</p>
                                <input type="text" placeholder="4자리"
                                    maxLength={4} class="login__input" id="phone_second" onChange={onChangePhone} />
                                <p className="phone_between">-</p>
                                <input type="text" placeholder="4자리"
                                    maxLength={4} class="login__input" id="phone_third" onChange={onChangePhone} />
                            </div>
                            {phone.length > 0 && (<span className={`message ${isPhone ? 'success' : 'error'}`}>{phoneMessage}</span>)}
                        </div>
                        <Post className='UserModify_Form_home' />
                        <span className="UserModify_Form_dbox_gender_box">
                            <input type="radio" id="genderMale" name="gender" value="M" checked={selectedGender === "M"} onChange={handleGenderChange} />남자
                            <input type="radio" id="genderFemale" name="gender" value="G" checked={selectedGender === "G"} onChange={handleGenderChange} />여자
                        </span>
                        <span className='UserModify_Form_list' >
                            <select class="box" id="domain-list_tendency" value={selectedTendency} onChange={handleTendencyChange}>
                                <option value="ISTJ" selected>ISTJ (현실주의자)</option>
                                <option value="ISTP">ISTP (장인)</option>
                                <option value="INFJ">INFJ (옹호자)</option>
                                <option value="INTJ">INTJ (전략가)</option>
                                <option value="ISFJ">ISFJ (수호자)</option>
                                <option value="ISFP">ISFP (모험가)</option>
                                <option value="INFP">INFP (중재자)</option>
                                <option value="INTP">INTP (논리술사)</option>
                                <option value="ESTJ">ESTJ (경영자)</option>
                                <option value="ESFP">ESFP (연예인)</option>
                                <option value="ENFP">ENFP (활동가)</option>
                                <option value="ENTP">ENTP (변론가)</option>
                                <option value="ESFJ">ESFJ (집정관)</option>
                                <option value="ESTP">ESTP (사업가)</option>
                                <option value="ENFJ">ENFJ (선도자)</option>
                                <option value="ENTJ">ENTJ (통솔자)</option>
                            </select>
                        </span>
                    </div>
                </form>
                <div className="userModify_button_box">
                    <span className="userModify_button_box_next" id="signup_last" onClick={checkPut}>다음</span>
                    <span className="userModify_button_box_span" id="signup_next" onClick={putUser}>회원정보수정</span>
                </div>
            </div>
        </div>
    )
}