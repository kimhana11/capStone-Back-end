import React from 'react';
import Slider from "react-slick";
import './slick.css';
import './slick-theme.css';
import img01 from '../../img/001.png';
import img02 from '../../img/002.png';
import img03 from '../../img/003.png';

const SimpleSlider = () => {
    const list = [
        {
            content: <img src={img01} />
        },
        {
            content: <img src={img02} />
        },
        {
            content: <img src={img03} />
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
            <Slider {...settings}>
                {list.map((slide, index) => (
                    <div key={index}>
                        <div className='slider_box'>
                            {slide.content}
                        </div>
                    </div>
                ))}
            </Slider>
        </div>
    );
};

export default SimpleSlider;