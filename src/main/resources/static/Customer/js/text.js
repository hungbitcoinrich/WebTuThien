$(document).ready(function() {
    // Xử lý dropdown menu
    $('#userDropdown').on('click', function(event) {
        event.preventDefault();
        var $dropdownMenu = $(this).next('.dropdown-menu');
        $('.dropdown-menu').not($dropdownMenu).hide(); // Đóng các dropdown khác
        $dropdownMenu.toggle(); // Mở hoặc đóng dropdown hiện tại
    });

    // Đóng dropdown khi nhấp ra ngoài
    $(document).on('click', function(event) {
        if (!$(event.target).closest('.dropdown').length) {
            $('.dropdown-menu').hide();
        }
    });

    // Xử lý nút scroll top
    var mybutton = document.getElementById("myBtn-scroll");

    // Hiển thị hoặc ẩn nút khi scroll
    window.onscroll = function() {
        scrollFunction();
    };

    function scrollFunction() {
        if (document.body.scrollTop > 20 || document.documentElement.scrollTop > 20) {
            mybutton.style.display = "block";
        } else {
            mybutton.style.display = "none";
        }
    }

    // Cuộn lên đầu trang khi click nút
    function topFunction() {
        document.body.scrollTop = 0;
        document.documentElement.scrollTop = 0;
    }

    // Reload trang sau mỗi 60 giây
    setTimeout(function(){
        location.reload();
    }, 60000); // 60 * 1000 milliseconds
});