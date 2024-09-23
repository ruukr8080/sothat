var swiper = new Swiper('#swiper-container', {
    slidesPerView: 1,
    spaceBetween: 4,
    pagination: {
        el: '.swiper-pagination',
        clickable: true,
    },
    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
    },
    breakpoints: {
        640: {
            slidesPerView: 2,
            spaceBetween: 20,
        },
        768: {
            slidesPerView: 3,
            spaceBetween: 30,
        },
        1024: {
            slidesPerView: 5,
            spaceBetween: 40,
        },
    }
});

function redirectTo(url) {
    window.location.href = url;
}