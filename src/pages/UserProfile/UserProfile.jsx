import React, { useEffect, useState } from "react";
import { useNavigate } from 'react-router-dom';
import axios from "axios";
import './UserProfile.css';
import MypageNav from '../../component/MypageNav/MypageNav.jsx';
import Swal from 'sweetalert2';

export default function UserProfile() {
    const navigate = useNavigate();
    const [userId, setUserId] = useState('');
    const [isUpdate, setisUpdate] = useState(false);
    const [stack, setStack] = useState([]);
    const [profile, setProfile] = useState([]);
    const [profileData, setProfileData] = useState([]);
    const [stackForm, setStackForm] = useState({ value: "" });
    const [profileForm, setProfileForm] = useState({ contestName: "", stack: "", contestPeriod: "", gitHub: "" });

    useEffect(() => {
        setUserId(window.localStorage.getItem('userId'));
        const userid = window.localStorage.getItem('userId');
        axios({
            method: 'get',
            url: `/user-profile/${userid}`
        }).then(result => {
            console.log(result.data)
            setProfileData(result.data);
        }).catch(err => {
            if (err.response && err.response.status === 500) {
                Swal.fire({
                    title: "프로필을 작성해주세요"
                })
                console.log(profileData)
            }
        })
    }, [])

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
            setStack([...stack, stackForm]);
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
                contestName: "",
                stack: "",
                contestPeriod: "",
                gitHub: ""
            });
        }
    };

    const removeStackList = (index) => {
        const removedStackList = stack.filter((_, i) => i !== index);
        setStack(removedStackList);
    };

    const removeProfileList = (index) => {
        const removedProfileList = profile.filter((_, i) => i !== index);
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
            stackList: stack.map(item => item.value),
            myTime: user_profile_time,
            desiredTime: user_profile_desiredTime,
            collaborationCount: user_profile_count,
            careers: profile.map(item => ({
                title: item.contestName,
                stack: item.stack,
                period: parseInt(item.contestPeriod),
                gitHub: item.gitHub
            }))
        };
        try {
            axios({
                method: 'post',
                url: '/user-profile',
                data: userProfile
            }).then(result => {
                Swal.fire({
                    title: "프로필이 저장되었습니다"
                }).then(() => {
                    navigate('/mypage');
                });
            })
        } catch (err) {
            console.error(err);
        }
    }

    const deleteUserProflie = (e) => {
        e.preventDefault();
        document.querySelector('#user_profile_intro').value = "";
        setStackForm({ value: "" });
        setProfileForm({
            contestName: "",
            stack: "",
            contestPeriod: "",
            gitHub: ""
        });
        setStack([]);
        setProfile([]);
    }

    return (
        <div>
            <MypageNav />
            {profileData.length === 0 && (
                <form className="user_file_form_box">
                    <span>프로필에 입력된 정보들은 AI 추천 매칭 시 도움이 됩니다!</span>
                    <div className="user_intro_form_box">
                        <div>
                            <input id="user_profile_intro" className="user_introduce"
                                maxLength={50} type="text" placeholder="자기 소개를 한 줄로 간단하게 해주세요!" />
                        </div>
                        <div className="user_intro_form_box">
                            <span>프로젝트시 투자 가능한 시간을 입력해주세요 ( 일주일에 몇 시간 )</span><br />
                            <input id="user_profile_time" className="user_time"
                                maxLength={5} type="text" placeholder="ex) 10" />
                        </div>
                        <div className="user_intro_form_box">
                            <span>프로젝트시 팀원이 투자 했으면 좋겠는 시간을 입력해주세요 ( 일주일에 몇 시간 )</span><br />
                            <input id="user_profile_desiredTime" className="user_time"
                                maxLength={5} type="text" placeholder="ex) 10" />
                        </div>
                        <div className="user_intro_form_box">
                            <span>팀원이 팀프로젝트 경험이 몇 회 정도 있으면 좋을 거 같은지 입력해주세요</span><br />
                            <input id="user_profile_count" className="user_time"
                                maxLength={5} type="text" placeholder="ex) 5" />
                        </div>
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
                                        {item.value}
                                        <button type="button" onClick={() => removeStackList(index)}>x</button>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                    <div className="user_profile_big_box">
                        <div className="user_profile_box">
                            <input
                                type="text"
                                name="contestName"
                                value={profileForm.contestName}
                                className="user_profile_contestName"
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
                                name="contestPeriod"
                                value={profileForm.contestPeriod}
                                className="user_profile_contestPeriod"
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
                                            <div><p>공모명</p> - {item.contestName}<br /></div>
                                            <div><p>기술 스택</p> - {item.stack}<br /></div>
                                            <div><p>공모전 기간(일수)</p> - {item.contestPeriod}<br /></div>
                                            <div><p>깃허브 주소</p> - {item.gitHub}</div>
                                        </div>
                                        <button type="button" onClick={() => removeProfileList(index)}>x</button>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                    <div className="user_profile_button_box">
                        <button type="button" onClick={saveUserProflie}>저장</button>
                        <button type="button" onClick={deleteUserProflie}>리셋</button>
                    </div>
                </form>
            )}
            {!(profileData.length === 0) && isUpdate === false && (
                <form className="user_file_form_box">
                    <span>프로필에 입력된 정보들은 AI 추천 매칭 시 도움이 됩니다!</span>
                    <div className="user_intro_form_box">
                        <div>
                            <input id="user_profile_intro" className="user_introduce"
                                maxLength={50} type="text" value={profileData.intro} disabled />
                        </div>
                        <div className="user_intro_form_box">
                            <span>프로젝트시 투자 가능한 시간</span><br />
                            <input id="user_profile_time" className="user_time"
                                maxLength={5} type="text" value={profileData.myTime} disabled />
                        </div>
                        <div className="user_intro_form_box">
                            <span>프로젝트시 팀원이 투자 했으면 좋겠는 시간</span><br />
                            <input id="user_profile_desiredTime" className="user_time"
                                maxLength={5} type="text" value={profileData.desiredTime} disabled />
                        </div>
                        <div className="user_intro_form_box">
                            <span>팀원에게 원하는 팀 프로젝트 횟수</span><br />
                            <input id="user_profile_count" className="user_time"
                                maxLength={5} type="text" value={profileData.collaborationCount} disabled />
                        </div>
                    </div>
                    <div className="user_select_stack_big_box">
                        <div className="user_stack_box"> 사용가능한 기술 스택 </div>
                        <div className="user_profile_list_box">
                            <ul>
                                {profileData && profileData.stackList && profileData.stackList.map((item, index) => (
                                    <li key={index}>
                                        {item}
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                    <div className="user_select_profile_big_box">
                        <div className="user_profile_box"> 참여했던 공모전 정보 </div>
                        <div className="user_profile_list_box">
                            <ul>
                                {profileData && profileData.careers && profileData.careers.map((item) => (
                                    <li key={item.id}>
                                        <div>
                                            <div><p>공모명</p> - {item.title}<br /></div>
                                            <div><p>기술 스택</p> - {item.stack}<br /></div>
                                            <div><p>공모전 기간(일수)</p> - {item.period}<br /></div>
                                            <div><p>깃허브 주소</p> - {item.gitHub}</div>
                                        </div>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                    <div className="user_profileData_button_box">
                        <button type="button" onClick={updateUserProflie}>프로필 수정</button>
                    </div>
                </form>
            )}
            {profileData && isUpdate === true && (
                <form className="user_file_form_box">
                    <span>프로필에 입력된 정보들은 AI 추천 매칭 시 도움이 됩니다!</span>
                    <div className="user_intro_form_box">
                        <div>
                            <input id="user_profile_intro" className="user_introduce"
                                maxLength={50} type="text" defaultValue={profileData.intro} />
                        </div>
                        <div className="user_intro_form_box">
                            <span>프로젝트시 투자 가능한 시간을 입력해주세요 ( 일주일에 몇 시간 )</span><br />
                            <input id="user_profile_time" className="user_time"
                                maxLength={5} type="text" defaultValue={profileData.myTime} />
                        </div>
                        <div className="user_intro_form_box">
                            <span>프로젝트시 팀원이 투자 했으면 좋겠는 시간을 입력해주세요 ( 일주일에 몇 시간 )</span><br />
                            <input id="user_profile_desiredTime" className="user_time"
                                maxLength={5} type="text" defaultValue={profileData.desiredTime} />
                        </div>
                        <div className="user_intro_form_box">
                            <span>팀원이 팀프로젝트 경험이 몇 회 정도 있으면 좋을 거 같은지 입력해주세요</span><br />
                            <input id="user_profile_count" className="user_time"
                                maxLength={5} type="text" defaultValue={profileData.collaborationCount} />
                        </div>
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
                                        {item.value}
                                        <button type="button" onClick={() => removeStackList(index)}>x</button>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                    <div className="user_profile_big_box">
                        <div className="user_profile_box">
                            <input
                                type="text"
                                name="contestName"
                                value={profileForm.contestName}
                                className="user_profile_contestName"
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
                                name="contestPeriod"
                                value={profileForm.contestPeriod}
                                className="user_profile_contestPeriod"
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
                                            <div><p>공모명</p> - {item.contestName}<br /></div>
                                            <div><p>기술 스택</p> - {item.stack}<br /></div>
                                            <div><p>공모전 기간(일수)</p> - {item.contestPeriod}<br /></div>
                                            <div><p>깃허브 주소</p> - {item.gitHub}</div>
                                        </div>
                                        <button type="button" onClick={() => removeProfileList(index)}>x</button>
                                    </li>
                                ))}
                            </ul>
                        </div>
                    </div>
                    <div className="user_profile_button_box">
                        <button type="button" onClick={saveUserProflie}>저장</button>
                        <button type="button" onClick={deleteUserProflie}>리셋</button>
                    </div>
                </form>
            )}
        </div>
    );
}