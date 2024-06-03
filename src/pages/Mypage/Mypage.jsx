import React, { useState } from "react";
import './Mypage.css';
import Post from '../../component/Post/Post.jsx';
import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import AIImg from '../../img/Group 88.png';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faStar, faUser } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import Swal from "sweetalert2";

export default function MyPage() {
    const navigate = useNavigate();
    const [userId, setUserId] = useState('');
    const [userReview, setUserReview] = useState('');
    const [userRate, setUserRate] = useState('');
    const [view, setView] = useState('userModify');

    useEffect(() => {
        const istoken = window.localStorage.getItem('token');
        const userid = window.localStorage.getItem('userId');
        setUserId(window.localStorage.getItem('userId'));
        if (istoken === null) {
            navigate('/signui');
        }
        axios({
            method: 'get',
            url: `/user-rate/${userid}`
        }).then((result) => {
            if (result.status == 200) {
                setUserRate(result.data);
            }
        }).catch(err => {
            if (err.response && err.response.status === 404) {

            }
        })
        axios({
            method: 'get',
            url: `/user-profile/${userid}`
        }).then(result => {
            setProfileData(result.data);
            setStack(result.data.stackList);
            setProfile(result.data.careers);
        }).catch(err => {
            if (err.response && err.response.status === 500) {
                Swal.fire({
                    title: "프로필을 작성해주세요"
                })
                console.log(profileData)
            }
        })
        axios({
            method: 'get',
            url: `/user-review/${userid}`
        }).then(result => {
            console.log(result.data);
            setUserReview(result.data);
        }).catch(err => {
            if (err.response && err.response.status === 500) {
                setUserReview('');
            }
        })
    }, [])

    const userDelete = e => {
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
                        }).then(() => {
                            window.localStorage.removeItem('token');
                            navigate('/');
                        })
                    }
                })
            }
        })
    }

    const UserUpdateRequest = {}

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

    const checkPut = (e) => {
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

        if (isPw && isName && isPhone && email_data.value.trim() && email_Seconddata.value.trim() &&
            houseCheck.value.trim() && houseSecondCheck.value.trim() && houseThirdCheck.value.trim()) {
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

    const putUser = (e) => {
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

                axios.put('/user/update', UserUpdateRequest)
                    .then((result) => {
                        if (result.status === 200) {
                            Swal.fire({
                                title: "회원정보가 수정되었습니다"
                            });
                        }
                    });
            }
        });
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

    const onChangePhone = (e) => {
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

    const [isUpdate, setisUpdate] = useState(false);
    const [stack, setStack] = useState([]);
    const [profile, setProfile] = useState([]);
    const [profileData, setProfileData] = useState([]);
    const [stackForm, setStackForm] = useState({ value: "" });
    const [profileForm, setProfileForm] = useState({ title: "", stack: "", period: "", gitHub: "" });

    const onChangeInput = (e) => {
        const { name, value } = e.target;
        setStackForm({ ...stackForm, [name]: value });
    };

    const onChangeInput_Plus = (e) => {
        const { name, value } = e.target;
        setProfileForm({ ...profileForm, [name]: value });
    };

    const addStackList = (e) => {
        e.preventDefault();
        if (stackForm.value === "") {
            Swal.fire({
                title: "공백은 추가할 수 없습니다"
            })
        } else {
            setStack([...stack, stackForm.value]);
            setStackForm({ value: "" });
        }
    };

    const addStackList_Plus = (e) => {
        e.preventDefault();
        if (Object.values(profileForm).some(value => value === "")) {
            Swal.fire({
                title: "공백은 추가할 수 없습니다"
            })
        } else {
            setProfile([...profile, profileForm]);
            setProfileForm({
                title: "",
                stack: "",
                period: "",
                gitHub: ""
            });
        }
    };

    const removeStackList = (index) => {
        const removedStackList = [...stack.slice(0, index), ...stack.slice(index + 1)];
        setStack(removedStackList);
    };

    const removeProfileList = (index) => {
        const removedProfileList = [...profile.slice(0, index), ...profile.slice(index + 1)];
        setProfile(removedProfileList);
    };

    const updateUserProflie = () => {
        setisUpdate(true);
    }

    const saveUserProflie = (e) => {
        e.preventDefault();
        let user_intro = document.querySelector('#user_profile_intro').value;
        let user_profile_time = document.querySelector('#user_profile_time').value;
        let user_profile_desiredTime = document.querySelector('#user_profile_desiredTime').value;
        let user_profile_count = document.querySelector('#user_profile_count').value;
        if (user_intro === "") {
            Swal.fire({
                title: "자기 소개를 입력해주세요"
            })
        } else if (user_profile_time === "") {
            Swal.fire({
                title: "프로젝트시 투자 가능한 시간을 입력해주세요"
            })
        } else if (user_profile_desiredTime === "") {
            Swal.fire({
                title: "프로젝트시 팀원이 투자 했으면 좋겠는 시간을 입력해주세요"
            })
        } else if (user_profile_count === "") {
            Swal.fire({
                title: "팀원이 팀프로젝트 경험이 몇 회 정도 있으면 좋을 거 같은지를 입력해주세요"
            })
        } else if (stack.length === 0) {
            Swal.fire({
                title: '기술 스택란이 비었습니다 이대로 진행할까요?',
                text: '후에 프로필 수정이 가능합니다',
                icon: 'warning',
                showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
                confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
                confirmButtonText: '확인', // confirm 버튼 텍스트 지정
                cancelButtonText: '취소', // cancel 버튼 텍스트 지정
            }).then(result => {
                // 만약 Promise리턴을 받으면,
                if (result.isConfirmed) { // 만약 모달창에서 confirm 버튼을 눌렀다면
                    if (profile.length === 0) {
                        Swal.fire({
                            title: '공모 관련 정보가 비었습니다 이대로 진행할까요?',
                            text: '후에 프로필 수정이 가능합니다',
                            icon: 'warning',
                            showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
                            confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                            cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
                            confirmButtonText: '확인', // confirm 버튼 텍스트 지정
                            cancelButtonText: '취소', // cancel 버튼 텍스트 지정
                        }).then(result => {
                            // 만약 Promise리턴을 받으면,
                            if (result.isConfirmed) { // 만약 모달창에서 confirm 버튼을 눌렀다면
                                submitUserProfile(user_intro, user_profile_time, user_profile_desiredTime, user_profile_count);
                            }
                        })
                    } else {
                        submitUserProfile(user_intro, user_profile_time, user_profile_desiredTime, user_profile_count);
                    }
                }
            })
        } else if (profile.length === 0) {
            Swal.fire({
                title: '공모 관련 정보가 비었습니다 이대로 진행할까요?',
                text: '후에 프로필 수정이 가능합니다',
                icon: 'warning',
                showCancelButton: true, // cancel버튼 보이기. 기본은 원래 없음
                confirmButtonColor: '#3085d6', // confrim 버튼 색깔 지정
                cancelButtonColor: '#d33', // cancel 버튼 색깔 지정
                confirmButtonText: '확인', // confirm 버튼 텍스트 지정
                cancelButtonText: '취소', // cancel 버튼 텍스트 지정
            }).then(result => {
                // 만약 Promise리턴을 받으면,
                if (result.isConfirmed) { // 만약 모달창에서 confirm 버튼을 눌렀다면
                    submitUserProfile(user_intro, user_profile_time, user_profile_desiredTime, user_profile_count);
                }
            })
        } else {
            submitUserProfile(user_intro, user_profile_time, user_profile_desiredTime, user_profile_count);
        }
    }

    const submitUserProfile = (user_intro, user_profile_time, user_profile_desiredTime, user_profile_count) => {
        const userProfile = {
            userId: userId,
            intro: user_intro,
            stackList: stack.map(item => item),
            myTime: user_profile_time,
            desiredTime: user_profile_desiredTime,
            collaborationCount: user_profile_count,
            careers: profile.map(item => ({
                title: item.title,
                stack: item.stack,
                period: parseInt(item.period),
                gitHub: item.gitHub
            }))
        };
        try {
            axios({
                method: 'post',
                url: '/user-profile',
                data: userProfile
            }).then(() => {
                Swal.fire({
                    title: "프로필이 저장되었습니다"
                }).then(() => {
                    navigate('/mypage');
                });
            }).catch(err => {
                if (err.response && err.response.status === 500) {
                    axios({
                        method: 'post',
                        url: '/profile-update',
                        data: userProfile
                    }).then(() => {
                        Swal.fire({
                            title: "프로필이 수정되었습니다"
                        }).then(() => {
                            navigate('/mypage');
                        });
                    })
                }
            })
        } catch (err) {
            console.error(err);
        }
    }

    const deleteUserProflie = (e) => {
        e.preventDefault();
        document.querySelector('#user_profile_intro').value = "";
        document.querySelector('#user_profile_time').value = "";
        document.querySelector('#user_profile_desiredTime').value = "";
        document.querySelector('#user_profile_count').value = "";
        setStackForm({ value: "" });
        setProfileForm({
            title: "",
            stack: "",
            period: "",
            gitHub: ""
        });
        setStack([]);
        setProfile([]);
    }

    const renderUserModify = () => {
        return (
            <>
                <div className="myPage_user_top">
                    <div className="myPage_user_top_div">
                        <FontAwesomeIcon icon={faUser} color="#b7b7b7" size="10x" />
                    </div>
                    <div className="myPage_user_top_box">
                        <FontAwesomeIcon icon={faStar} color="#FFC700" size="5x" className="myPage_star" />
                        <p>{userRate}</p>
                    </div>
                </div>
                <div className="myPage_main_background">
                    <div className="myPage_main_nav">
                        <p className={`nav-item ${view === 'userModify' ? 'active' : ''}`} onClick={() => setView('userModify')}>회원정보</p>
                        <p className={`nav-item ${view === 'userProfile' ? 'active' : ''}`} onClick={() => setView('userProfile')}>프로필</p>
                        <p onClick={userDelete}>회원탈퇴</p>
                    </div>
                    <div className='UserModify_Main'>
                        <form className='UserModify_Form'>
                            <div className='UserModify_Form_dbox_icon'>
                                <div className='UserModify_Form_Main'>
                                    <div className="UserModify_Form_Main_div">아이디</div>
                                    <input type="text" className='UserModify_Form_id' value={userId} />
                                </div>
                                <div className='UserModify_Form_Main'>
                                    <div className="UserModify_Form_Main_div">비밀번호</div>
                                    <div className="UserModify_Form_dbox_box">
                                        <input type="password" className='UserModify_Form_pw' onChange={onChangePassword} />
                                        <br />
                                        {pw.length > 0 && (<span className={`message ${isPw ? 'success' : 'error'}`}>{pwMessage}</span>)}
                                    </div>
                                </div>
                                <div className='UserModify_Form_Main'>
                                    <div className="UserModify_Form_Main_div">이름</div>
                                    <div className="UserModify_Form_dbox_box">
                                        <input type="text" className='UserModify_Form_name' onChange={onChangeName} />
                                        <br />
                                        {name.length > 0 && <span className={`message ${isName ? 'success' : 'error'}`}>{nameMessage}</span>}
                                    </div>
                                </div>
                                <div className='UserModify_Form_Main'>
                                    <div className="UserModify_Form_Main_div">이메일</div>
                                    <div className="UserModify_Form_dbox_mailbox">
                                        <input type="text" id="UserModify_Form_email_id" />
                                        <p className="email_annotation">@</p>
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
                                </div>
                                <div className='UserModify_Form_Main'>
                                    <div className="UserModify_Form_Main_div">전화번호</div>
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
                                </div>
                                <div className='UserModify_Form_Main'>
                                    <div className="UserModify_Form_Main_div">주소</div>
                                    <Post className='UserModify_Form_home' />
                                </div>
                                <div className='UserModify_Form_Main'>
                                    <div className="UserModify_Form_Main_div">성별</div>
                                    <span className="UserModify_Form_dbox_gender_box">
                                        <input type="radio" id="genderMale" name="gender" value="M" checked={selectedGender === "M"} onChange={handleGenderChange} />남자
                                        <input type="radio" id="genderFemale" name="gender" value="G" checked={selectedGender === "G"} onChange={handleGenderChange} />여자
                                    </span>
                                </div>
                                <div className='UserModify_Form_Main_last'>
                                    <div className="UserModify_Form_Main_div">성향</div>
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
                            </div>
                        </form>
                        <div className="userModify_button_box">
                            <span className="userModify_button_box_next" id="signup_last" onClick={checkPut}>다음</span>
                            <span className="userModify_button_box_span" id="signup_next" onClick={putUser}>회원정보수정</span>
                        </div>
                    </div>
                </div>
                <div className="myPage_user_bottom_div">
                    <img src={AIImg} alt="AI" />
                    <p className="myPage_user_bottom_p">
                        매칭 상대에게 바라는 경험 횟수, 투자 가능 시간을 <br /> 작성해주시면 더 정확한 추천이 가능해요!
                    </p>
                </div>
                <div className="myPage_review_backcolor">
                    <p>내게 남겨진 리뷰 박스</p>
                    {userReview && userReview.length > 0 ? (
                        userReview.map((review, index) => (
                            <div key={index} className="myPage_review_div_box">
                                <div className="myPage_review_big_div">
                                    <div className="myPage_review_div">
                                        <FontAwesomeIcon icon={faUser} color="#b7b7b7" size="3x" />
                                    </div>
                                    <p>{review.reviewerId}님</p>
                                </div>
                                <p>{review.content}</p>
                            </div>
                        ))
                    ) : (
                        <p className="myPage_review_non">존재하는 리뷰가 없습니다</p>
                    )}
                </div>
            </>
        )
    }

    const renderUserProfile = () => {
        return (
            <>
                {profileData.length === 0 && (
                    <>
                        <div className="myPage_user_top">
                            <div className="myPage_user_top_div">
                                <FontAwesomeIcon icon={faUser} color="#b7b7b7" size="10x" />
                            </div>
                            <div className="myPage_user_top_box">
                                <FontAwesomeIcon icon={faStar} color="#FFC700" size="5x" className="myPage_star" />
                                <p>{userRate}</p>
                            </div>
                        </div>
                        <div className="myPage_main_background">
                            <div className="myPage_main_nav">
                                <p className={`nav-item ${view === 'userModify' ? 'active' : ''}`} onClick={() => setView('userModify')}>회원정보</p>
                                <p className={`nav-item ${view === 'userProfile' ? 'active' : ''}`} onClick={() => setView('userProfile')}>프로필</p>
                                <p onClick={userDelete}>회원탈퇴</p>
                            </div>
                            <form className="user_file_form_box">
                                <div className="user_mate_profile_big_box">
                                    <p className="user_intro_form_box_profile">프로필</p>
                                    <div>
                                        <div className="user_intro_div_box_update">
                                            <p className="user_intro_div_box_first">자기소개 :</p>
                                            <input id="user_profile_intro" className="user_introduce_update"
                                                maxLength={50} type="text" placeholder="자기 소개를 한 줄로 간단하게 해주세요!" />
                                        </div>
                                        <div className="user_stack_big_box">
                                            <div className="user_stack_box">
                                                <input
                                                    type="text"
                                                    name="value"
                                                    value={stackForm.value}
                                                    className="user_stack"
                                                    onChange={onChangeInput}
                                                    placeholder="기술 스택을 입력하세요"
                                                />
                                                <button onClick={addStackList}>
                                                    추가
                                                </button>
                                            </div>
                                            <div className="user_profile_list_box">
                                                <ul>
                                                    {stack.map((item, index) => (
                                                        <li key={index}>
                                                            {item}
                                                            <button type="button" onClick={() => removeStackList(index)}>x</button>
                                                        </li>
                                                    ))}
                                                </ul>
                                            </div>
                                        </div>
                                        <div className="user_intro_div_box_update">
                                            <p>프로젝트 투자 가능한 시간 :</p>
                                            <input id="user_profile_time" className="user_time_update"
                                                maxLength={5} type="text" placeholder="ex) 10" />
                                        </div>
                                    </div>
                                </div>
                                <div className="user_mate_profile_big_box">
                                    <p className="user_careers_form_box_profile"> 경력 </p>
                                    <div className="user_profile_big_box">
                                        <div className="user_profile_box_update">
                                            <input
                                                type="text"
                                                name="title"
                                                value={profileForm.title}
                                                className="user_profile_title"
                                                onChange={onChangeInput_Plus}
                                                placeholder="공모명을 입력하세요"
                                            />
                                            <input
                                                type="text"
                                                name="stack"
                                                value={profileForm.stack}
                                                className="user_profile_stack"
                                                onChange={onChangeInput_Plus}
                                                placeholder="기술 스택을 입력하세요"
                                            />
                                            <input
                                                type="text"
                                                name="period"
                                                value={profileForm.period}
                                                className="user_profile_period"
                                                onChange={onChangeInput_Plus}
                                                placeholder="공모전 기간(일수만)을 입력하세요"
                                            />
                                            <input
                                                type="text"
                                                name="gitHub"
                                                value={profileForm.gitHub}
                                                className="user_profile_gitHub"
                                                onChange={onChangeInput_Plus}
                                                placeholder="깃허브 주소를 입력하세요"
                                            />
                                            <button onClick={addStackList_Plus}>
                                                추가
                                            </button>
                                        </div>
                                        <div className="user_profile_list_box">
                                            <ul>
                                                {profile.map((item, index) => (
                                                    <li key={item.id}>
                                                        <div>
                                                            <div className="user_profile_list_box_update"><p>공모명</p> - {item.title}<br /></div>
                                                            <div className="user_profile_list_box_update"><p>기술 스택</p> - {item.stack}<br /></div>
                                                            <div className="user_profile_list_box_update"><p>공모전 기간(일수)</p> - {item.period}<br /></div>
                                                            <div className="user_profile_list_box_update"><p>깃허브 주소</p> - {item.gitHub}</div>
                                                        </div>
                                                        <button type="button" onClick={() => removeProfileList(index)}>x</button>
                                                    </li>
                                                ))}
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <div className="user_mate_profile_big_box_upate">
                                    <p>팀원에게 바라는 프로젝트 경험 횟수와 투자 시간을 입력해주세요. <i>(선택)</i></p>
                                    <div className="user_mate_profile_big_box">
                                        <p className="user_mate_profile_big_box_p">추천 메이트</p>
                                        <div>
                                            <div className="user_intro_div_box_update">
                                                <p>경험 횟수 : </p>
                                                <input id="user_profile_count" className="user_time_update"
                                                    maxLength={5} type="text" placeholder="ex) 5" />
                                            </div>
                                            <div className="user_intro_div_box_update">
                                                <p>투자 가능 시간 : </p>
                                                <input id="user_profile_desiredTime" className="user_time_update"
                                                    maxLength={5} type="text" placeholder="ex) 10" />
                                            </div>
                                            <div className="user_profile_button_box">
                                                <button type="button" onClick={saveUserProflie}>저장</button>
                                                <button type="button" onClick={deleteUserProflie}>리셋</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div className="myPage_user_bottom_div">
                            <img src={AIImg} alt="AI" />
                            <p className="myPage_user_bottom_p">
                                매칭 상대에게 바라는 경험 횟수, 투자 가능 시간을 <br /> 작성해주시면 더 정확한 추천이 가능해요!
                            </p>
                        </div>
                        <div className="myPage_review_backcolor">
                            <p>내게 남겨진 리뷰 박스</p>
                            {userReview && userReview.length > 0 ? (
                                userReview.map((review, index) => (
                                    <div key={index} className="myPage_review_div_box">
                                        <div className="myPage_review_big_div">
                                            <div className="myPage_review_div">
                                                <FontAwesomeIcon icon={faUser} color="#b7b7b7" size="3x" />
                                            </div>
                                            <p>{review.reviewerId}님</p>
                                        </div>
                                        <p>{review.content}</p>
                                    </div>
                                ))
                            ) : (
                                <p className="myPage_review_non">존재하는 리뷰가 없습니다</p>
                            )}
                        </div>
                    </>)}
                {!(profileData.length === 0) && isUpdate === false && (
                    <>
                        <div className="myPage_user_top">
                            <div className="myPage_user_top_div">
                                <FontAwesomeIcon icon={faUser} color="#b7b7b7" size="10x" />
                            </div>
                            <div className="myPage_user_top_box">
                                <FontAwesomeIcon icon={faStar} color="#FFC700" size="5x" className="myPage_star" />
                                <p>{userRate}</p>
                            </div>
                        </div>
                        <div className="myPage_main_background">
                            <div className="myPage_main_nav">
                                <p className={`nav-item ${view === 'userModify' ? 'active' : ''}`} onClick={() => setView('userModify')}>회원정보</p>
                                <p className={`nav-item ${view === 'userProfile' ? 'active' : ''}`} onClick={() => setView('userProfile')}>프로필</p>
                                <p onClick={userDelete}>회원탈퇴</p>
                            </div>
                            <form className="user_file_form_box">
                                <div className="user_intro_form_box">
                                    <p className="user_intro_form_box_profile">프로필</p>
                                    <div>
                                        <div className="user_intro_div_box">
                                            <p className="user_intro_div_box_first">자기소개 :</p>
                                            <input id="user_profile_intro" className="user_introduce"
                                                maxLength={50} type="text" value={profileData.intro} disabled />
                                        </div>
                                        <div className="user_intro_div_box">
                                            <p className="user_intro_div_box_p">기술 스택 :</p>
                                            <div className="user_profile_list_box">
                                                {profileData && profileData.stackList && profileData.stackList.map((item, index, array) => (
                                                    <p key={index}>
                                                        {item}{index < array.length - 1 && ','}
                                                    </p>
                                                ))}
                                            </div>
                                        </div>
                                        <div className="user_intro_div_box">
                                            <p>프로젝트 투자 가능한 시간 :</p>
                                            <input id="user_profile_time" className="user_mytime"
                                                maxLength={5} type="text" value={"주에 " + profileData.myTime + "시간"} disabled />
                                        </div>
                                    </div>
                                </div>
                                <div className="user_select_profile_big_box">
                                    <p className="user_careers_form_box_profile"> 경력 </p>
                                    <div className="user_profile_list_box">
                                        <ul>
                                            {profileData && profileData.careers && profileData.careers.map((item) => (
                                                <li key={item.id}>
                                                    <div className="user_careers_list_box">
                                                        <p className="user_careers_list_box_p">○</p>
                                                        <div className="user_careers_list_div_box">
                                                            <div className="user_careers_list_div"><p>공모명</p> - {item.title}<br /></div>
                                                            <div className="user_careers_list_div"><p>기술 스택</p> - {item.stack}<br /></div>
                                                            <div className="user_careers_list_div"><p>공모전 기간</p> - {item.period} 일<br /></div>
                                                            <div className="user_careers_list_div"><p>깃허브 주소</p> - {item.gitHub}</div>
                                                        </div>
                                                    </div>
                                                </li>
                                            ))}
                                        </ul>
                                    </div>
                                </div>
                                <div className="user_mate_profile_big_box">
                                    <p className="user_mate_profile_big_box_p">추천 메이트</p>
                                    <div>
                                        <div className="user_intro_div_box">
                                            <p className="user_intro_div_box_first">경험 횟수 : </p>
                                            <input id="user_profile_count" className="user_introduce"
                                                maxLength={5} type="text" value={profileData.collaborationCount + "회 이상"} disabled />
                                        </div>
                                        <div className="user_intro_div_box">
                                            <p>투자 가능 시간 : </p>
                                            <input id="user_profile_desiredTime" className="user_mytime"
                                                maxLength={5} type="text" value={"주에 " + profileData.desiredTime + "시간 이상"} disabled />
                                        </div>
                                        <div className="user_profileData_button_box">
                                            <button type="button" onClick={updateUserProflie}>프로필 수정</button>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div className="myPage_user_bottom_div">
                            <img src={AIImg} alt="AI" />
                            <p className="myPage_user_bottom_p">
                                매칭 상대에게 바라는 경험 횟수, 투자 가능 시간을 <br /> 작성해주시면 더 정확한 추천이 가능해요!
                            </p>
                        </div>
                        <div className="myPage_review_backcolor">
                            <p>내게 남겨진 리뷰 박스</p>
                            {userReview && userReview.length > 0 ? (
                                userReview.map((review, index) => (
                                    <div key={index} className="myPage_review_div_box">
                                        <div className="myPage_review_big_div">
                                            <div className="myPage_review_div">
                                                <FontAwesomeIcon icon={faUser} color="#b7b7b7" size="3x" />
                                            </div>
                                            <p>{review.reviewerId}님</p>
                                        </div>
                                        <p>{review.content}</p>
                                    </div>
                                ))
                            ) : (
                                <p className="myPage_review_non">존재하는 리뷰가 없습니다</p>
                            )}
                        </div>
                    </>)}
                {profileData && isUpdate === true && (
                    <>
                        <div className="myPage_user_top">
                            <div className="myPage_user_top_div">
                                <FontAwesomeIcon icon={faUser} color="#b7b7b7" size="10x" />
                            </div>
                            <div className="myPage_user_top_box">
                                <FontAwesomeIcon icon={faStar} color="#FFC700" size="5x" className="myPage_star" />
                                <p>{userRate}</p>
                            </div>
                        </div>
                        <div className="myPage_main_background">
                            <div className="myPage_main_nav">
                                <p className={`nav-item ${view === 'userModify' ? 'active' : ''}`} onClick={() => setView('userModify')}>회원정보</p>
                                <p className={`nav-item ${view === 'userProfile' ? 'active' : ''}`} onClick={() => setView('userProfile')}>프로필</p>
                                <p onClick={userDelete}>회원탈퇴</p>
                            </div>
                            <form className="user_file_form_box">
                                <div className="user_mate_profile_big_box">
                                    <p className="user_intro_form_box_profile">프로필</p>
                                    <div>
                                        <div className="user_intro_div_box_update">
                                            <p className="user_intro_div_box_first">자기소개 :</p>
                                            <input id="user_profile_intro" className="user_introduce_update"
                                                maxLength={50} type="text" defaultValue={profileData.intro} />
                                        </div>
                                        <div className="user_stack_big_box">
                                            <div className="user_stack_box">
                                                <input
                                                    type="text"
                                                    name="value"
                                                    value={stackForm.value}
                                                    className="user_stack"
                                                    onChange={onChangeInput}
                                                    placeholder="기술 스택을 입력하세요"
                                                />
                                                <button onClick={addStackList}>
                                                    추가
                                                </button>
                                            </div>
                                            <div className="user_profile_list_box">
                                                <ul>
                                                    {stack.map((item, index) => (
                                                        <li key={index}>
                                                            {item}
                                                            <button type="button" onClick={() => removeStackList(index)}>x</button>
                                                        </li>
                                                    ))}
                                                </ul>
                                            </div>
                                        </div>
                                        <div className="user_intro_div_box_update">
                                            <p>프로젝트 투자 가능한 시간 :</p>
                                            <input id="user_profile_time" className="user_time_update"
                                                maxLength={5} type="text" defaultValue={profileData.myTime} />
                                        </div>
                                    </div>
                                </div>
                                <div className="user_mate_profile_big_box">
                                    <p className="user_careers_form_box_profile"> 경력 </p>
                                    <div className="user_profile_big_box">
                                        <div className="user_profile_box_update">
                                            <input
                                                type="text"
                                                name="title"
                                                value={profileForm.title}
                                                className="user_profile_title"
                                                onChange={onChangeInput_Plus}
                                                placeholder="공모명을 입력하세요"
                                            />
                                            <input
                                                type="text"
                                                name="stack"
                                                value={profileForm.stack}
                                                className="user_profile_stack"
                                                onChange={onChangeInput_Plus}
                                                placeholder="기술 스택을 입력하세요"
                                            />
                                            <input
                                                type="text"
                                                name="period"
                                                value={profileForm.period}
                                                className="user_profile_period"
                                                onChange={onChangeInput_Plus}
                                                placeholder="공모전 기간(일수만)을 입력하세요"
                                            />
                                            <input
                                                type="text"
                                                name="gitHub"
                                                value={profileForm.gitHub}
                                                className="user_profile_gitHub"
                                                onChange={onChangeInput_Plus}
                                                placeholder="깃허브 주소를 입력하세요"
                                            />
                                            <button onClick={addStackList_Plus}>
                                                추가
                                            </button>
                                        </div>
                                        <div className="user_profile_list_box">
                                            <ul>
                                                {profile.map((item, index) => (
                                                    <li key={item.id}>
                                                        <div>
                                                            <div className="user_profile_list_box_update"><p>공모명</p> - {item.title}<br /></div>
                                                            <div className="user_profile_list_box_update"><p>기술 스택</p> - {item.stack}<br /></div>
                                                            <div className="user_profile_list_box_update"><p>공모전 기간(일수)</p> - {item.period}<br /></div>
                                                            <div className="user_profile_list_box_update"><p>깃허브 주소</p> - {item.gitHub}</div>
                                                        </div>
                                                        <button type="button" onClick={() => removeProfileList(index)}>x</button>
                                                    </li>
                                                ))}
                                            </ul>
                                        </div>
                                    </div>
                                </div>
                                <div className="user_mate_profile_big_box_upate">
                                    <p>팀원에게 바라는 프로젝트 경험 횟수와 투자 시간을 입력해주세요. <i>(선택)</i></p>
                                    <div className="user_mate_profile_big_box">
                                        <p className="user_mate_profile_big_box_p">추천 메이트</p>
                                        <div>
                                            <div className="user_intro_div_box_update">
                                                <p>경험 횟수 : </p>
                                                <input id="user_profile_count" className="user_time_update"
                                                    maxLength={5} type="text" defaultValue={profileData.collaborationCount} />
                                            </div>
                                            <div className="user_intro_div_box_update">
                                                <p>투자 가능 시간 : </p>
                                                <input id="user_profile_desiredTime" className="user_time_update"
                                                    maxLength={5} type="text" defaultValue={profileData.desiredTime} />
                                            </div>
                                            <div className="user_profile_button_box">
                                                <button type="button" onClick={saveUserProflie}>수정</button>
                                                <button type="button" onClick={deleteUserProflie}>리셋</button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div className="myPage_user_bottom_div">
                            <img src={AIImg} alt="AI" />
                            <p className="myPage_user_bottom_p">
                                매칭 상대에게 바라는 경험 횟수, 투자 가능 시간을 <br /> 작성해주시면 더 정확한 추천이 가능해요!
                            </p>
                        </div>
                        <div className="myPage_review_backcolor">
                            <p>내게 남겨진 리뷰 박스</p>
                            {userReview && userReview.length > 0 ? (
                                userReview.map((review, index) => (
                                    <div key={index} className="myPage_review_div_box">
                                        <div className="myPage_review_big_div">
                                            <div className="myPage_review_div">
                                                <FontAwesomeIcon icon={faUser} color="#b7b7b7" size="3x" />
                                            </div>
                                            <p>{review.reviewerId}님</p>
                                        </div>
                                        <p>{review.content}</p>
                                    </div>
                                ))
                            ) : (
                                <p className="myPage_review_non">존재하는 리뷰가 없습니다</p>
                            )}
                        </div>
                    </>)}
            </>
        )
    }

    return (
        <>
            {view === 'userModify' && renderUserModify()}
            {view === 'userProfile' && renderUserProfile()}
        </>
    );
}