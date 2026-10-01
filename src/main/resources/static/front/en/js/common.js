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
        closeText: "close",
        prevText: "previous",
        nextText: "next",
        currentText: "today",
        monthNames: [ "January","February","March","April","May","June","July","August","September","October","November","December" ],
        monthNamesShort: [ "Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec" ],
        dayNames: [ "Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday" ],
        dayNamesShort: [ "Sun","Mon","Tue","Wed","Thu","Fri","Sat" ],
        dayNamesMin: [ "Sun","Mon","Tue","Wed","Thu","Fri","Sat" ],
        weekHeader: "week",
        dateFormat: "yy-mm-dd",
        firstDay: 0,
        isRTL: false,
        showMonthAfterYear: true,
        yearSuffix: "year" };
    datepicker.setDefaults( datepicker.regional.ko );

    return datepicker.regional.ko;

} ) );

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
    if (target.value.trim()) {
        return true;
    }

    //const particle = (hasCoda(fieldName)) ? '을' : '를'; // 조사
    //const elementType = (target.type === 'text' || target.type === 'password' || target.type === 'search' || target.type === 'textarea') ? '입력' : '선택';
    //alert( `${fieldName + particle} ${elementType}해 주세요.` );
    alert( `please check ${fieldName}` );

    target.value = '';
    ( !focusTarget ? target : focusTarget).focus();
    return false;
}