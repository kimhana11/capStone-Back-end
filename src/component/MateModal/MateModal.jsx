import Modal from 'react-modal';
import './MateModal.css'
import image from '../../img/Ellipse 16.png'

const MateModal = () => {

    const customStyles = {
        overlay: {
            backgroundColor: "rgba(0, 0, 0, 0)",
            zIndex: 1000,
            position: "static",
            top: 0,
            left: 0,
        },
        content: {
            width: "450px",
            height: "550px",
            zIndex: 150,
            position: "absolute",
            top: "47%",
            left: "72%",
            transform: "translate(-50%, -50%)",
            borderRadius: "1rem",
            boxShadow: "2px 2px 2px rgba(0, 0, 0, 0.25)",
            backgroundColor: "white",
            overflow: "auto",
            padding: 0
        },
    };

    return (
        <div>
            <Modal isOpen={true} style={customStyles}>
                <div className='modal_mate_top'>
                    <p>추천 메이트</p>
                    <p>AI 매칭 메이트</p>
                </div>
                <div className='modal_mate_main'>
                    <div className='modal_mate_competition'>
                        <p>A 공모전</p>
                        <p>A 공모전</p>
                        <p>A 공모전</p>
                    </div>
                    <div className='modal_mate'>
                        <div className='modal_mate_user'>
                            <img src={image} />
                            <div>
                                <p className='modal_mate_user_name'>홍길동</p>
                                <p className='modal_mate_user_content'>안녕하세요 ~~</p>
                                <div className='modal_mate_user_stack'>
                                    <p>포토샵</p>
                                    <p>피그마</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className='modal_mate_group_button'>
                        <img src={image} />
                        <img src={image} />
                        <p>그룹 방 만들기</p>
                    </div>
                </div>
            </Modal>
        </div>
    )
}

export default MateModal;