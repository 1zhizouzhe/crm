layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);
    
    form.on('submit(saveBtn)',function (data){
        var fieldData = data.field;
        /*console.log(data.field);
        if(fieldData.username == 'undefined' || fieldData.username.trim() == ""){
            layer.msg("用户名称不能为空");
            return false;
        }
        if(fieldData.password == 'undefined' || fieldData.password.trim() == ""){
            layer.msg("用户密码不能为空");
            return false;
        }*/
        $.ajax({
            type:"post",
            url:ctx + "/user/updatePassword",
            data:{
                oldPassword:fieldData.old_password,
                newPassword:fieldData.new_password,
                confirmPassword:fieldData.again_password
            },
            dataType:"json",
            success:function(data){
                if(data.code == 200){
                    //alert("修改成功")
                    //layer.msg("修改成功了");
                    layer.msg("修改成功了，三秒后推出系统",function (){
                        $.removeCookie("userIdStr", {domain:"localhost",path:"/crm"});
                        $.removeCookie("userName", {domain:"localhost",path:"/crm"});
                        $.removeCookie("trueName", {domain:"localhost",path:"/crm"});
                        window.parent.location.href = ctx +"/index";
                    });
                }else{
                    layer.msg("!!!!!修改失败");
                }
            }
        });
        return false;
    });

});