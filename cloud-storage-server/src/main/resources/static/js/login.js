$(function() {
    var domain = "http://192.168.0.36:8020";


    $('#login #password').focus(function() {
        $('#owl-login').addClass('password');
    }).blur(function() {
        $('#owl-login').removeClass('password');
    });

    //简单登录操作
    $("#doLogin").click(function (e) {
        $.ajax({
            type : "POST",
            url : domain + "/user/login",
            data : {
                "account" : $("#userName").val(),
                "password" : $("#password").val()
            },
            dataType : "json",
            success : function(data) {
                if (data.result == "-1") {
                    alert("登陆失败");
                } else {
                    sessionStorage.setItem("account", $("#userName").val());
                    document.location = "index.html";
                }
            }
        });
    });
});