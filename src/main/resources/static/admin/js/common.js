/*----------------------------
* datepicker-ko
-----------------------------*/
( function( factory ) {
    if ( typeof define === "function" && define.amd ) {

        // AMD. Register as an anonymous module.
        define( [ "../widgets/datepicker" ], factory );
    } else {

        // Browser globals
        factory( jQuery.datepicker );
    }
}( function( datepicker ) {

    datepicker.regional.ko = {
        closeText: "닫기",
        prevText: "이전달",
        nextText: "다음달",
        currentText: "오늘",
        monthNames: [ "1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월" ],
        monthNamesShort: [ "1월","2월","3월","4월","5월","6월","7월","8월","9월","10월","11월","12월" ],
        dayNames: [ "일요일","월요일","화요일","수요일","목요일","금요일","토요일" ],
        dayNamesShort: [ "일","월","화","수","목","금","토" ],
        dayNamesMin: [ "일","월","화","수","목","금","토" ],
        weekHeader: "주",
        dateFormat: "yy-mm-dd",
        firstDay: 0,
        isRTL: false,
        showMonthAfterYear: true,
        yearSuffix: "년" };
    datepicker.setDefaults( datepicker.regional.ko );

    return datepicker.regional.ko;

} ) );

/*----------------------------
*v 작업부분
-----------------------------*/
$(function(){
    /*----글자수 카운트----*/
    $('.text-limit input').keyup(function (e){
        var content = $(this).val();
        $(this).parent().find('.counter strong').html(content.length);
    });
    $('.text-limit textarea').keyup(function (e){
        var content = $(this).val();
        $(this).parent().find('.counter strong').html(content.length);
    });
    $('.text-limit input').keyup();
    $('.text-limit textarea').keyup();

    /*----달력----*/
    $(".date-set").datepicker({
        showOn: "button",
        buttonImage: "/admin/images/date.png",
        buttonImageOnly: false,
        buttonText: "Select date"
    });
    /*----파일업로드----*/
    var $fileTarget = $('.filebox .upload-hidden');

    $fileTarget.on('change', function(){
        if(window.FileReader){
            var filename = $(this)[0].files[0].name;
        } else {
            var filename = $(this).val().split('/').pop().split('\\').pop();
        }

        $(this).siblings('.upload-name').val(filename);
    });
    /*----*드래그 정렬----*/
    $( ".sortable" ).sortable({
        revert: true
    });
    /*----전체 체크----*/
    $('.chk-all').on('click', function () {
        $( '.chk' ).prop( 'checked', this.checked );
    });
    /*----popup - 마이페이지 열기----*/
    $( "header .menu p a" ).on('click', function () {
        $('#popMypage').fadeIn(100);
    });
    /*----popup - 팝업닫기 공통----*/
    $( ".dim-layer .close" ).on('click', function () {
        $('.dim-layer').fadeOut(100);
    });
    /*
    $( ".dim-layer .btn-group a" ).on('click', function () {
        $('.dim-layer').fadeOut(100);
    });
     */
});

/**
 * 문자열의 마지막(끝) 문자의 종성 포함 여부 확인
 * @param value - Target String
 * @returns 종성 포함 여부
 */
function hasCoda(value) {
    return ((value.charCodeAt(value.length - 1) - 0xAC00) % 28) > 0;
}


/**
 * 필드(Elemenet) 유효성 검사
 * @param target - 검사 대상 Element
 * @param fieldName - 필드명
 * @param focusTarget - 포커스 대상 Element
 * @returns 필드 입력(선택) 여부
 */
function isValid(target, fieldName, focusTarget) {
    console.log("## isValid > target = ", target);
    console.log("## isValid > fieldName = ", fieldName);
    
    if (target.value.trim()) {
        return true;
    }

    const particle = (hasCoda(fieldName)) ? '을' : '를'; // 조사
    const elementType = (target.type === 'text' || target.type === 'password' || target.type === 'search' || target.type === 'textarea') ? '입력' : '선택';
    alert( `${fieldName + particle} ${elementType}해 주세요.` );

    target.value = '';
    ( !focusTarget ? target : focusTarget).focus();
    return false;
}

$(document).ready(function () {
    if ((window.location.pathname !== "/AdminAuth/changePassword") && (window.location.pathname !== "/AdminLogin/login")) {
        $.ajax({
            url: "/AdminAuth/checkPasswordChanged",
            type: "POST",
            success: function (response) {
                if (response.status === "change_required") {
                    alert("비밀번호를 변경해야 합니다.");
                    window.location.href = "/AdminAuth/changePassword";

                }
            },
            error: function (xhr, status, error) {
                console.error("비밀번호 변경 확인 오류:", status, error);
            }
        });
    }

});


