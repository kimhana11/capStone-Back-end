import React from 'react';
import Slider from "react-slick";
import './slick.css';
import './slick-theme.css';

const SimpleSlider = () => {
    const list = [
        {
            content: 1
        },
        {
            content: 2
        },
        {
            content: 3
        }
    ]

    const settings = {
        infinite: true,
        autoplaySpeed: 3000,
        autoplay: true,
        slidesToShow: 3,
        centerMode: true,
        slidesToScroll: 1
    };

    return (
        <div className='slider_main_box'>
            <Slider {...settings}> {/* settings 객체를 전달할 때는 {...settings} 형태로 전달해야 합니다. */}
                {list.map((value, index) => (
                    <div>
                        <div
                            className='slider_box'
                            key={index}>
                        </div>
                    </div>
                ))}
            </Slider>
        </div>
    );
};

export default SimpleSlider;