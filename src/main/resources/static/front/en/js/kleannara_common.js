$(document).ready(function(){
    $("#gnb").hover(function(){
        $("#header").addClass("on")
    }, function(){
        $("#header").removeClass("on")
        $("#gnb > ul > li").removeClass("on")
    })

	$("#gnb > ul > li").hover(function(){
        $("#gnb > ul > li").removeClass("on")
        $(this).addClass("on")
    })

    $("#header .h_util .h_lang > button").click(function(){
        $("#header .h_util .h_lang").toggleClass("on")
    })

	$("#menuOpen").click(function(){
		$("#all_gnb").addClass("active")
	})
	$("#menuClose").click(function(){
		$("#all_gnb").removeClass("active")
	})

	$("#all_gnb .gnb_box > ul > li > a").click(function(){
		if ($(window).width() < 1025) {

			$(this).toggleClass("on");
			$(this).next("div").stop().slideToggle();
			return false;
		}
	})

	$(window).scroll(function(){
		if ($(window).scrollTop() == 0){
			$("#header").removeClass("scroll_on")
		} else {
			$("#header").addClass("scroll_on")
		}
	})
	
	setTimeout(function(){
		$("#mVisu").addClass("active")
	}, 1000)
	
	window.onload = function(){
		setTimeout(function(){
			scrollTo(0,0);
		}, 100)
	}
	$("#mVisu").on('scroll mousewheel', function(e) {
		if(e.originalEvent.wheelDelta < -20){
			if (!$(".mVisu_slide_box").hasClass("on")){
				$(".mVisu_slide_box").addClass("on");
				$("html").removeClass("no_scroll");

				$("#mVisu .mVisu_slide_box").css({top:"-"+ $(".mVisu_slide_wrap").offset().top+"px"})

				setTimeout(function(){
					$(".mVisu_slide_box").addClass("stop");
				}, 1000)
			}
			
		} else if(e.originalEvent.wheelDelta > 20){
			if ($(".mVisu_slide_box").hasClass("on") && $(window).scrollTop() == 0){
				$(".mVisu_slide_box").removeClass("stop").removeClass("on");
				$("html").addClass("no_scroll");
				
				$("#mVisu .mVisu_slide_box").css({top:"0"})
			}
		}
	});
	
	var touchStartY;
	var touchEndY;
	$("#mVisu").on("touchstart", function(e){
		touchStartY = e.originalEvent.changedTouches[0].screenY;
	})
	$("#mVisu").on("touchend", function(e){
		touchEndY = e.originalEvent.changedTouches[0].screenY;

		if (touchStartY < touchEndY){
			if ($(".mVisu_slide_box").hasClass("on") && $(window).scrollTop() <= 0){
				$(".mVisu_slide_box").removeClass("stop").removeClass("on");
				$("html").addClass("no_scroll");
				
				$("#mVisu .mVisu_slide_box").css({top:"0"})
			}
		} else if (touchStartY >= touchEndY){
			if (!$(".mVisu_slide_box").hasClass("on")){
				$(".mVisu_slide_box").addClass("on");
				$("html").removeClass("no_scroll");

				$("#mVisu .mVisu_slide_box").css({top:"-"+ $(".mVisu_slide_wrap").offset().top+"px"})

				setTimeout(function(){
					$(".mVisu_slide_box").addClass("stop");
				}, 1000)
			}
		}
	})

	$("#wrap").on('scroll touchmove mousewheel', function(e){
		if ($("div").hasClass("mVisu_slide_box")){
			if (!$(".mVisu_slide_box").hasClass("stop") && !$("#all_gnb").hasClass("active")){
				e.preventDefault();
				e.stopPropagation();
				return false;
			}
		}

		if ($("div").hasClass("sCompany_case1")){
			if (!$(".sCompany_case1").hasClass("complete") && !$("#all_gnb").hasClass("active")){
				e.preventDefault();
				e.stopPropagation();
				return false;
			}
		}
	});

	$(window).resize(function(){
		if ($(".mVisu_slide_box").hasClass("on")){
			$("#mVisu .mVisu_slide_box").css({top:"-"+ $(".mVisu_slide_wrap").offset().top+"px"})
		}
	})


	// sub
	$(".sCompany_case1").on('scroll mousewheel', function(e) {
		if(e.originalEvent.wheelDelta < -20){
			if (!$(".sCompany_case1").hasClass("active")){
				$(".sCompany_case1").addClass("active");

				setTimeout(function(){
					$(".sCompany_case1").addClass("complete");
				}, 1500)
			}

			if ($(".sCompany_case1").hasClass("complete"))	{
				$("html, body").animate({scrollTop:$(".sCompany_case2").offset().top})
				$(".sCompany_case2").addClass("active");
				$(".sCompany_case2 > div:first").addClass("on")

				setTimeout(function(){
					$(".sCompany_case2").addClass("complete");
				}, 2000)
			}
			
			e.preventDefault();
			e.stopPropagation();
			return false;
			
		} else if(e.originalEvent.wheelDelta > 20){
			if ($(window).scrollTop() == 0){
				$(".sCompany_case1").removeClass("complete").removeClass("active");
			}
		}
	});
	$(".sCompany_case1").on("touchstart", function(e){
		touchStartY = e.originalEvent.changedTouches[0].screenY;
	})
	$(".sCompany_case1").on("touchend", function(e){
		touchEndY = e.originalEvent.changedTouches[0].screenY;

		if (touchStartY >= touchEndY){
			if (!$(".sCompany_case1").hasClass("active")){
				$(".sCompany_case1").addClass("active");

				setTimeout(function(){
					$(".sCompany_case1").addClass("complete");
				}, 1500)
			}

			if ($(".sCompany_case1").hasClass("complete"))	{
				$("html, body").animate({scrollTop:$(".sCompany_case2").offset().top})
				$(".sCompany_case2").addClass("active");
				$(".sCompany_case2 > div:first").addClass("on")

				setTimeout(function(){
					$(".sCompany_case2").addClass("complete");
				}, 2000)
			}
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		} else if (touchStartY < touchEndY){
			if ($(window).scrollTop() == 0){
				$(".sCompany_case1").removeClass("complete").removeClass("active");
			}
		}
	})

	$(".sCompany_case2").on('scroll mousewheel', function(e) {
		if(e.originalEvent.wheelDelta < -20){
			if ($(".sCompany_case2 > div:first").hasClass("on") && $(".sCompany_case2 > div:last").hasClass("complete"))	{
				//$("html, body").stop().animate({scrollTop:$(this).next("div").offset().top})
			} else {
				if ($(this).hasClass("complete")){
					$(".sCompany_case2 > div:first").addClass("hide");
					$(".sCompany_case2 > div:last").addClass("on");

					setTimeout(function(){
						$(".sCompany_case2 > div:last").addClass("complete");
					}, 2000)

				}
			
				e.preventDefault();
				e.stopPropagation();
				return false;
			}
			
		} else if(e.originalEvent.wheelDelta > 20){
			
			if ($(".sCompany_case2 > div:last").hasClass("on")){
				$("html, body").stop().animate({scrollTop: $(this).offset().top})
				$(".sCompany_case2 > div:last").removeClass("complete").removeClass("on")
				setTimeout(function(){
					$(".sCompany_case2 > div:first").removeClass("hide")
				}, 1000)
			} else {
				if (!$(".sCompany_case2 > div:first").hasClass("hide")){
					$("html, body").stop().animate({scrollTop: $(this).prev("div").offset().top})
					$(".sCompany_case2").removeClass("active").removeClass("complete");
					$(".sCompany_case2 > div:first").removeClass("on")
				}
			}
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
	});
	$(".sCompany_case2").on("touchstart", function(e){
		touchStartY = e.originalEvent.changedTouches[0].screenY;
		e.preventDefault()
	})
	$(".sCompany_case2").on("touchend", function(e){
		touchEndY = e.originalEvent.changedTouches[0].screenY;

		if (touchStartY >= touchEndY){
			if ($(".sCompany_case2 > div:first").hasClass("on") && $(".sCompany_case2 > div:last").hasClass("complete"))	{
				$("html, body").stop().animate({scrollTop:$(this).next("div").offset().top})
			} else {
				if ($(this).hasClass("complete")){
					$(".sCompany_case2 > div:first").addClass("hide");
					$(".sCompany_case2 > div:last").addClass("on");

					setTimeout(function(){
						$(".sCompany_case2 > div:last").addClass("complete");
					}, 2000)

				}
			}
			
			e.preventDefault();
			e.stopPropagation();
			return false;

		} else if (touchStartY < touchEndY){
			
			if ($(".sCompany_case2 > div:last").hasClass("on")){
				$("html, body").stop().animate({scrollTop: $(this).offset().top})
				$(".sCompany_case2 > div:last").removeClass("complete").removeClass("on")
				setTimeout(function(){
					$(".sCompany_case2 > div:first").removeClass("hide")
				}, 1000)
			} else {
				if (!$(".sCompany_case2 > div:first").hasClass("hide")){
					$("html, body").stop().animate({scrollTop: $(this).prev("div").offset().top})
					$(".sCompany_case2").removeClass("active").removeClass("complete");
					$(".sCompany_case2 > div:first").removeClass("on")
				}
			}
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
	})

	var sub_tab_chk = false;
	$(".sCompany .sub_tab li a").click(function(e){
		e.stopPropagation();
		sub_tab_chk = true;
		$(".sCompany .sub_tab li").removeClass("on")
		$(this).parent("li").addClass("on");
		$("html, body").animate({scrollTop: $(""+ $(this).attr("href") +"").offset().top - 50}, 1000)

		if ($(this).parent("li").index() == 0){
			$(".sCompany_case1").removeClass("active").removeClass("complete")
			$(".sCompany_case2").removeClass("active").removeClass("complete")
			$(".sCompany_case2 > div:eq(0)").removeClass("on").removeClass("hide")
			$(".sCompany_case2 > div:eq(1)").removeClass("on").removeClass("complete")
		} else {
			$(".sCompany_case1").addClass("active").addClass("complete")
			$(".sCompany_case2").addClass("active").addClass("complete")
			$(".sCompany_case2 > div:eq(0)").addClass("on").addClass("hide")
			$(".sCompany_case2 > div:eq(1)").addClass("on").addClass("complete")
		}
		
		setTimeout(function(){
			sub_tab_chk = false;
		}, 1500)
		return false;
	})
	$(window).scroll(function(){
		if ($("div").hasClass("sCompany") && sub_tab_chk == false){
			
			if ($("div").hasClass("sFactory_case1"))	{
				if ($(window).scrollTop() < $("#company2").offset().top){
					$(".sCompany .sub_tab li").removeClass("on")
					$(".sCompany .sub_tab li:eq(0)").addClass("on")
				} else if ($(window).scrollTop() >= $("#company2").offset().top){
					$(".sCompany .sub_tab li").removeClass("on")
					$(".sCompany .sub_tab li:eq(1)").addClass("on")
				}
			} else {
				if ($(window).scrollTop() < $("#company2").offset().top){
					$(".sCompany .sub_tab li").removeClass("on")
					$(".sCompany .sub_tab li:eq(0)").addClass("on")
				} else if ($(window).scrollTop() >= $("#company2").offset().top && $(window).scrollTop() < $("#company3").offset().top){
					$(".sCompany .sub_tab li").removeClass("on")
					$(".sCompany .sub_tab li:eq(1)").addClass("on")
				} else if ($(window).scrollTop() >= $("#company3").offset().top){
					$(".sCompany .sub_tab li").removeClass("on")
					$(".sCompany .sub_tab li:eq(2)").addClass("on")
				}
			}

			if ($(window).width() < 1025){
				if ($(window).scrollTop() >= $("#header").height()){
					$(".sCompany .sub_tab ").addClass("scroll_on");
				} else {
					$(".sCompany .sub_tab ").removeClass("scroll_on");
				}
			}
		}
	})

	$(".sPersonnelmanagement .sub_tab li a").click(function(e){
		e.stopPropagation();
		$(".sPersonnelmanagement .sub_tab li").removeClass("on")
		$(this).parent("li").addClass("on");
		$("html, body").animate({scrollTop: $(""+ $(this).attr("href") +"").offset().top - 80})
		return false;
	})
	$(window).scroll(function(){
		if ($("div").hasClass("sPersonnelmanagement")){
			if ($(window).scrollTop() < $("#personnelmanagement2").offset().top - 85){
				$(".sPersonnelmanagement .sub_tab li").removeClass("on")
				$(".sPersonnelmanagement .sub_tab li:eq(0)").addClass("on")
			} else if ($(window).scrollTop() >= $("#personnelmanagement2").offset().top - 85 && $(window).scrollTop() < $("#personnelmanagement3").offset().top - 85){
				$(".sPersonnelmanagement .sub_tab li").removeClass("on")
				$(".sPersonnelmanagement .sub_tab li:eq(1)").addClass("on")
			} else if ($(window).scrollTop() >= $("#personnelmanagement3").offset().top - 85){
				$(".sPersonnelmanagement .sub_tab li").removeClass("on")
				$(".sPersonnelmanagement .sub_tab li:eq(2)").addClass("on")
			}

			if ($(window).width() < 1025){
				if ($(window).scrollTop() >= $("#header").height()){
					$(".sPersonnelmanagement .sub_tab ").addClass("scroll_on");
				} else {
					$(".sPersonnelmanagement .sub_tab ").removeClass("scroll_on");
				}
			}
		}
	})



	$(".sEsg_case1").on('scroll mousewheel', function(e) {
		console.log(e.originalEvent.wheelDelta)
		if(e.originalEvent.wheelDelta < 0){
			if ($(this).hasClass("complete")){
				$("html:not(:animated), body:not(:animated)").stop().animate({scrollTop: $(this).height()})
			} else {
				$(".sEsg_case1 .img_box").addClass("active")
				$("html:not(:animated), body:not(:animated)").css({scrollTop: 0})

				setTimeout(function(){
					$(".sEsg_case1").addClass("complete")
				}, 1500)
			}
			
			e.preventDefault();
			e.stopPropagation();
			return false;
			
		} else if(e.originalEvent.wheelDelta > 0){
			if ($(window).scrollTop() == 0){
				$("html:not(:animated), body:not(:animated)").animate({scrollTop: 0})
				$(".sEsg_case1").removeClass("complete")
				$(".sEsg_case1 .img_box").removeClass("active")

				setTimeout(function(){
					$(".sEsg_case1").removeClass("complete")
				}, 1500)
			}
		}
	});
	$(".sEsg_case1").on("touchstart", function(e){
		touchStartY = e.originalEvent.changedTouches[0].screenY;
		e.preventDefault()
	})
	$(".sEsg_case1").on("touchend", function(e){
		touchEndY = e.originalEvent.changedTouches[0].screenY;

		if (e.target.closest(".sGovernance_slide")) {
		} else {

			if (touchStartY >= touchEndY){
				if ($(this).hasClass("complete")){
					$("html, body").animate({scrollTop: $(this).height()})
				} else {
					$(".sEsg_case1 .img_box").addClass("active")

					setTimeout(function(){
						$(".sEsg_case1").addClass("complete")
					}, 1500)
				
					e.preventDefault();
					e.stopPropagation();
					return false;
				}

			} else if (touchStartY < touchEndY){			
				if ($(window).scrollTop() == 0){
					$("html, body").animate({scrollTop: 0})
					$(".sEsg_case1").removeClass("complete")
					$(".sEsg_case1 .img_box").removeClass("active")

					setTimeout(function(){
						$(".sEsg_case1").removeClass("complete")
					}, 1500)
				}
			}
		}
	})


	$(".sRightpeople_case1").on('scroll mousewheel', function(e) {
		if(e.originalEvent.wheelDelta < -20){
			if (!$(".sRightpeople_case1").hasClass("complete")){

				if (!$(".sRightpeople_case1").hasClass("active")){
					$(".sRightpeople_case1").addClass("active");

					setTimeout(function(){
						$(".sRightpeople_case1").addClass("complete");
					}, 1500)
				}
				
				e.preventDefault();
				e.stopPropagation();
				return false;
			}
			
		} else if(e.originalEvent.wheelDelta > 20){
			if ($(window).scrollTop() == 0){
				$(".sRightpeople_case1").removeClass("complete").removeClass("active");
			}
		}
	});
	$(".sRightpeople_case1").on("touchstart", function(e){
		touchStartY = e.originalEvent.changedTouches[0].screenY;
		if (!$(".sRightpeople_case1").hasClass("complete")){
			e.preventDefault()
		}
	})
	$(".sRightpeople_case1").on("touchend", function(e){
		touchEndY = e.originalEvent.changedTouches[0].screenY;

		if (touchStartY >= touchEndY){
			if (!$(".sRightpeople_case1").hasClass("complete")){
				if (!$(".sRightpeople_case1").hasClass("active")){
					$(".sRightpeople_case1").addClass("active");

					setTimeout(function(){
						$(".sRightpeople_case1").addClass("complete");
					}, 2500)
				}
				
				e.preventDefault();
				e.stopPropagation();
				return false;
			}
		} else if (touchStartY < touchEndY){
			if ($(".sRightpeople_case1").hasClass("complete")){
				$(".sRightpeople_case1").removeClass("complete").removeClass("active");
			}

			
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
	})

	$(".sRecruitment_case1").on('scroll mousewheel', function(e) {
		if(e.originalEvent.wheelDelta < -20){
			if (!$(".sRecruitment_case1").hasClass("active")){
				$(".sRecruitment_case1").addClass("active");

				setTimeout(function(){
					$(".sRecruitment_case1").addClass("complete");
				}, 1500)
			}

			if ($(".sRecruitment_case1").hasClass("complete"))	{
				$("html, body").stop().animate({scrollTop:$(this).height()})
			}
			
			e.preventDefault();
			e.stopPropagation();
			return false;
			
		} else if(e.originalEvent.wheelDelta > 20){
			if ($(window).scrollTop() == 0){
				$(".sRecruitment_case1").removeClass("complete").removeClass("active");
			}
		}
	});
	$(".sRecruitment_case1").on("touchstart", function(e){
		touchStartY = e.originalEvent.changedTouches[0].screenY;
		e.preventDefault()
	})
	$(".sRecruitment_case1").on("touchend", function(e){
		touchEndY = e.originalEvent.changedTouches[0].screenY;

		if (touchStartY >= touchEndY){
			if (!$(".sRecruitment_case1").hasClass("active")){
				$(".sRecruitment_case1").addClass("active");

				setTimeout(function(){
					$(".sRecruitment_case1").addClass("complete");
				}, 1500)
			}

			if ($(".sRecruitment_case1").hasClass("complete"))	{
				$("html, body").animate({scrollTop:$(this).height()})
			}
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		} else if (touchStartY < touchEndY){
			if ($(".sRecruitment_case1").hasClass("complete")){
				$(".sRecruitment_case1").removeClass("complete").removeClass("active");
			}

			
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
	})

	
	$(".sIntroduction_case1").on('scroll mousewheel', function(e) {
		if(e.originalEvent.wheelDelta < -20){
			if (!$(".sIntroduction_case1").hasClass("complete")){

				if (!$(".sIntroduction_case1").hasClass("active")){
					$(".sIntroduction_case1").addClass("active");

					setTimeout(function(){
						$(".sIntroduction_case1").addClass("complete");
					}, 1500)
				}
			} else {
				$("html, body").stop().animate({scrollTop: $(".sIntroduction_case2").offset().top})
				$(".sIntroduction_case2").addClass("on")
			}
				
			e.preventDefault();
			e.stopPropagation();
			return false;
			
		} else if(e.originalEvent.wheelDelta > 20){
			if ($(window).scrollTop() == 0){
				$(".sIntroduction_case1").removeClass("complete").removeClass("active");
			}
		}
	});
	$(".sIntroduction_case1").on("touchstart", function(e){
		touchStartY = e.originalEvent.changedTouches[0].screenY;
		e.preventDefault()
	})
	$(".sIntroduction_case1").on("touchend", function(e){
		touchEndY = e.originalEvent.changedTouches[0].screenY;

		if (touchStartY >= touchEndY){
			if (!$(".sIntroduction_case1").hasClass("complete")){

				if (!$(".sIntroduction_case1").hasClass("active")){
					$(".sIntroduction_case1").addClass("active");

					setTimeout(function(){
						$(".sIntroduction_case1").addClass("complete");
					}, 1500)
				}
			} else {
				$("html, body").stop().animate({scrollTop: $(".sIntroduction_case2").offset().top})
				$(".sIntroduction_case2").addClass("on")
			}
				
			e.preventDefault();
			e.stopPropagation();
			return false;
		} else if (touchStartY < touchEndY){
			if ($(".sIntroduction_case1").hasClass("complete")){
				$(".sIntroduction_case1").removeClass("complete").removeClass("active");
			}

			
			
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
	})

	$(".sIntroduction_case2").on('scroll mousewheel', function(e) {
		if(e.originalEvent.wheelDelta < -20){
			//if (!$(".sIntroduction_case2").hasClass("complete")){
				if ($(window).width() > 767){
					$("html, body").stop().animate({scrollTop: $(".sIntroduction_case3").offset().top})
					$(".sIntroduction_case2").addClass("complete")
					$(".sIntroduction_case3").addClass("on")
				} else if ($(window).width() <= 767){
					if ($(".sIntroduction_case2 .box > div.active").index() == 2){
						$("html, body").stop().animate({scrollTop: $(".sIntroduction_case3").offset().top})
						$(".sIntroduction_case2").addClass("complete")
						$(".sIntroduction_case3").addClass("on")
					} else {
						$(".sIntroduction_case2 .box > div:eq(0):not(:animated)").animate({marginTop: "-"+ (($(".sIntroduction_case2 .box > div.active").index() + 1) * 100) +"vh"}, 1000, function(){
							$(".sIntroduction_case2 .box > div.active").removeClass("active").next("div").addClass("active")
						})
					}
				}
				
			//}
			
		} else if(e.originalEvent.wheelDelta > 20){
			if ($(window).width() > 767){
				$("html, body").stop().animate({scrollTop: $(".sIntroduction_case1").offset().top})
				$(".sIntroduction_case2").removeClass("complete")
				$(".sIntroduction_case2").removeClass("on")
			} else if ($(window).width() <= 767){
				if ($(".sIntroduction_case2 .box > div.active").index() == 0){
					$("html, body").stop().animate({scrollTop: $(".sIntroduction_case1").offset().top})
				} else {
					$(".sIntroduction_case2 .box > div:eq(0):not(:animated)").animate({marginTop: "-"+ (($(".sIntroduction_case2 .box > div.active").index() - 1) * 100) +"vh"}, 1000, function(){
						$(".sIntroduction_case2 .box > div.active").removeClass("active").prev("div").addClass("active")
					})
				}
				$(".sIntroduction_case2").removeClass("complete")
			}
		}
				
		e.preventDefault();
		e.stopPropagation();
		return false;
	});
	$(".sIntroduction_case2").on("touchstart", function(e){
		touchStartY = e.originalEvent.changedTouches[0].screenY;
		e.preventDefault()
	})
	$(".sIntroduction_case2").on("touchend", function(e){
		touchEndY = e.originalEvent.changedTouches[0].screenY;

		if (touchStartY >= touchEndY){
			if (!$(".sIntroduction_case2").hasClass("complete")){
				if ($(window).width() > 767){
					$("html, body").stop().animate({scrollTop: $(".sIntroduction_case3").offset().top})
					$(".sIntroduction_case2").addClass("complete")
					$(".sIntroduction_case3").addClass("on")
				} else if ($(window).width() <= 767){
					if ($(".sIntroduction_case2 .box > div.active").index() == 2){
						$("html, body").stop().animate({scrollTop: $(".sIntroduction_case3").offset().top})
						$(".sIntroduction_case2").addClass("complete")
						$(".sIntroduction_case3").addClass("on")
					} else {
						$(".sIntroduction_case2 .box > div:eq(0):not(:animated)").animate({marginTop: "-"+ (($(".sIntroduction_case2 .box > div.active").index() + 1) * 100) +"vh"}, 1000, function(){
							$(".sIntroduction_case2 .box > div.active").removeClass("active").next("div").addClass("active")
						})
					}
				}
				
			}
		} else if (touchStartY < touchEndY){
			if ($(window).width() > 767){
				$("html, body").stop().animate({scrollTop: $(".sIntroduction_case1").offset().top})
				$(".sIntroduction_case2").removeClass("complete")
			} else if ($(window).width() <= 767){
				if ($(".sIntroduction_case2 .box > div.active").index() == 0){
					$("html, body").stop().animate({scrollTop: $(".sIntroduction_case1").offset().top})
				} else {
					$(".sIntroduction_case2 .box > div:eq(0):not(:animated)").animate({marginTop: "-"+ (($(".sIntroduction_case2 .box > div.active").index() - 1) * 100) +"vh"}, 1000, function(){
						$(".sIntroduction_case2 .box > div.active").removeClass("active").prev("div").addClass("active")
					})
				}
				$(".sIntroduction_case2").removeClass("complete")
			}
		}
				
		e.preventDefault();
		e.stopPropagation();
		return false;
	})


	$(".sIntroduction_case3").on('scroll mousewheel', function(e) {
		if(e.originalEvent.wheelDelta < -20){
		} else if(e.originalEvent.wheelDelta > 20){
			$("html, body").stop().animate({scrollTop: $(".sIntroduction_case2").offset().top})
			$(".sIntroduction_case3").removeClass("on")
				
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
	});
	$(".sIntroduction_case3").on("touchstart", function(e){
		touchStartY = e.originalEvent.changedTouches[0].screenY;
	})
	$(".sIntroduction_case3").on("touchend", function(e){
		touchEndY = e.originalEvent.changedTouches[0].screenY;

		if (touchStartY >= touchEndY){
		} else if (touchStartY < touchEndY){
			$("html, body").stop().animate({scrollTop: $(".sIntroduction_case2").offset().top})
			$(".sIntroduction_case3").removeClass("on")
				
			e.preventDefault();
			e.stopPropagation();
			return false;
		}
	})

	/*$("#wrap").on('scroll touchmove mousewheel', function(e){
		if ($("div").hasClass("sCompany_case1")){
			if (!$(".sCompany_case1").hasClass("active")){
				e.preventDefault();
				e.stopPropagation();
				return false;
			}
		}
	});*/


	

	setTimeout(function(){
		$("#Quick").addClass("load");
	}, 2000)
    $("#Quick .brand_btn").click(function(){
		if ($("#Quick").hasClass("on")){
			$("#Quick").removeClass("on")
			setTimeout(function(){
				$("#Quick").addClass("load");
			}, 500)
		} else {
			$("#Quick").removeClass("load");
			setTimeout(function(){
				$("#Quick").addClass("on");
			}, 500)
		}
        //$("#Quick").toggleClass("on")
    })

	$("#Quick .top_btn").click(function(){
		$("html, body").animate({scrollTop:0})

		if ($("div").hasClass("mVisu_slide_box")){
			$(".mVisu_slide_box").removeClass("stop").removeClass("on");
			$("html").addClass("no_scroll");
			
			$("#mVisu .mVisu_slide_box").css({top:"0"})
		} else if ($("div").hasClass("sCompany_case1")){
			$(".sCompany_case1").removeClass("active").removeClass("complete")
			$(".sCompany_case2").removeClass("active").removeClass("complete")
			$(".sCompany_case2 > div:eq(0)").removeClass("on").removeClass("hide")
			$(".sCompany_case2 > div:eq(1)").removeClass("on").removeClass("complete")
		}
	})


	$(document).on("click", ".select_box > button", function(){
		var select_box = $(this).parent(".select_box");
		var select_list = '';
		
		if (select_box.hasClass("on")){
			select_box.removeClass("on");
			select_box.children(".slist").remove();
		} else {
			select_box.addClass("on");
			for (var i = 0; i < select_box.children("select").children("option").length ;i++ ){
				if (i == 0){
					select_list += '<ul class="slist">';
				}

				if (select_box.children("select").children("option").eq(i).val() == $(this).text()){
					select_list += '<li class="active">';
				} else {
					select_list += '<li>';
				}
				select_list += '<button type="button" value="'+ select_box.children("select").children("option").eq(i).val() +'">'+ select_box.children("select").children("option").eq(i).val() +'</button>';
				select_list += '</li>';
			}
			select_list += '</ul>';

			select_box.append(select_list);
		}
	})
	$(document).on("click", ".select_box .slist button", function(){
		var select_box = $(this).closest(".select_box");

		select_box.children("button").text($(this).text())
		select_box.removeClass("on")
		select_box.children(".slist").remove()
	})
	$('html').click(function(e){
		if($(e.target).parents('.select_box').length < 1){
			$(".select_box").removeClass("on")
			$(".select_box .slist").remove()
		}
	});


	$(".sBoard .board_faq_box .question button").click(function(){
		$(this).parent("div").parent("div").toggleClass("active");
		$(this).parent("div").next("div").stop().slideToggle();
	})
	
	$(".sReportcenter_case5 .top_box button").click(function(){
		$(this).parent("div").parent("div").toggleClass("active");
		$(this).parent("div").next("div").stop().slideToggle();
	})

	$(document).on("click", ".sSharedgrowth_case2 .list_box button.view_btn", function(){
		$(this).parent("div").toggleClass("active")		
		if ($(this).parent("div").hasClass("active"))	{
			$(this).text("View content")
		} else {
			$(this).html("View full <br class='pc_br'>content")
		}
	})
	$(".sSharedgrowth_case2 .list_box button.close_btn").click(function(){
		$(this).parent("div").removeClass("active")
		$(".sSharedgrowth_case2 .list_box button.view_btn").html("View full <br class='pc_br'>content")
	})


	$(".sJobintroduction_case1 .top_box ul li button").click(function(){
		$(this).closest("ul").children("li").removeClass("on")
		$(this).parent("li").addClass("on")

		console.log($(this).parent("li").index())

		$(this).closest(".top_box").next("div").children("div").removeClass("on")
		$(this).closest(".top_box").next("div").children("div:eq("+ $(this).parent("li").index() +")").addClass("on")
	})


	$(".sBusiness_index > div .tit").click(function(){
		$(".sBusiness_index > div").removeClass("active")
		$(this).parent("div").addClass("active")

		$("html, body").css({scrollTop:0})
	})


	$(".sBusiness_product_slide .swiper-slide").click(function(){
		var busi_par = $(this).closest(".sBusiness_product")
		busi_par.find(".swiper-slide").removeClass("active")
		$(this).addClass("active")
		
		busi_par.find(".box").children("div").removeClass("active")
		busi_par.find(".box").children("div").eq($(this).index()).addClass("active")
	})


	if ($("div").hasClass("sBusiness_visu")){
		if ($(".sBusiness_visu").hasClass("case2")){
			$(window).scroll(function(){
				var visuTop1 = $(window).scrollTop() / 20;
				var visuTop2 = $(window).scrollTop() / 18;
				$(".sBusiness_visu > div").css({top: "-"+ visuTop1 +"%"})
				$(".sBusiness_visu .s-inner").css({top: "-"+ visuTop2 +"%"})
			})
		} else {
			$(window).scroll(function(){
				var visuTop = $(window).scrollTop() / 20;
				$(".sBusiness_visu > div").css({top: "-"+ visuTop +"%"})
			})
		}
	}


	$(".traffic_box button").click(function(){
		$(this).parent("div").toggleClass("active")
	})


	$(".sPrivacy_tabBtn li a").click(function(){
		$("html, body").stop().animate({scrollTop: ($(".sPrivacy_case1 dl dt:eq("+ $(this).parent("li").index() +")").offset().top - 140 )})
		return false;
	})

	$(document).on("click", ".select_change_wrap .select_box .slist button", function(){
		$(".select_change_wrap .select_change_box > div").removeClass("active");
		$(".select_change_wrap .select_change_box > div:eq("+ $(this).parent("li").index() +")").addClass("active");
	})

	$(".sBoard .board_inquiry_box .txt_chk_box .chk_box .layer_pop .close_btn").click(function(){
		$(this).closest(".chk_box").children("input").prop('checked', false);
	})

})


