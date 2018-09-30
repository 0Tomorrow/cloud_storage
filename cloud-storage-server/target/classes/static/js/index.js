var path = "";
// var account = "15927860204";
var account = sessionStorage.getItem("account");
var domain = "http://localhost:8020";
function addData() {
    layui.use('table', function(){
        var table = layui.table;
        //第一个实例
        table.render({
            elem: '#file'
            ,height: 315
            ,url: domain + '/file/show' //数据接口
            ,where: {
                'account' : account,
                'path' : path
            }
            ,parseData : function (res) {
                console.log(res);
                return {
                    "code" : res.code,
                    "data" : res.data
                }
            }
            ,page: true //开启分页
            ,limit: 10
            ,cols: [[ //表头
                {type: 'checkbox'},
                {field: 'fileName', title: '文件名', width:500, event:"fileClick", templet: function(d){
                        return "<i class=\"layui-icon\">&#xe621;</i>   " + d.fileName;
                    }},
                {field: 'updateTime', title: '时间', width:200, event:"click", templet: function(d){
                        return timestampToTime(parseInt(d.updateTime));
                    }},
                {fixed: 'right', width:150, align:'center', toolbar: '#barDemo'}
            ]]
        });
        table.render({
            elem: '#folder'
            ,height: 315
            ,url: domain + '/index/show' //数据接口
            ,where: {
                'updateBy' : account,
                'path' : path
            }
            ,page: true //开启分页
            ,limit: 10
            ,cols: [[ //表头
                {type: 'checkbox'},
                {field: 'indexName', title: '文件夹名', width:500, event:"indexClick"},
                {fixed: 'right', width:150, align:'center', toolbar: '#barDemo'}
            ]]
        });
        table.on('tool(folder)', function(obj){
            var data = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            if (layEvent === "indexClick") {
                if (path === "") {
                    path = data.indexName;
                } else {
                    path = path + "/" + obj.data.indexName;
                }
                addData();
                pageUp();
            } else if(layEvent === 'detail'){ //查看
                //do something
            } else if(layEvent === 'del'){ //删除
                layer.confirm('真的删除行么', function(index){
                    obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                    layer.close(index);
                    $.get(domain + "/index/delete", {"indexName" : data.indexName, "updateBy" : account, "path" : path})
                });
            } else if(layEvent === 'edit'){ //编辑
                //do something
            }
        });
        table.on('tool(file)', function(obj){ //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
            var fileData = obj.data; //获得当前行数据
            var layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
            var tr = obj.tr; //获得当前行 tr 的DOM对象
            if (layEvent === "fileClick") {
                var reg = /.*\.pdf/gi;
                if (reg.test(fileData.fileName)) {
                    $.post(domain + "/file/preview", {"fileName" : fileData.fileName, "account" : account, "path" : path}, function (data) {
                        var list = data.data;
                        layer.open({
                            type: 1,
                            area: '434',
                            title : fileData.fileName,
                            content : '<img src="http://' + list[0].path + '" width="434" height="614">'
                        })
                    });
                }
            } else if(layEvent === 'detail'){ //查看
                //do something
            } else if(layEvent === 'del'){ //删除
                layer.confirm('真的删除行么', function(index){
                    obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
                    layer.close(index);
                    $.get(domain + "/file/delete", {"fileName" : fileData.fileName, "account" : account, "path" : path})
                });
            } else if(layEvent === 'edit'){ //编辑
                //do something
            }
        });
    });
}
addData();
// layui.use('upload', function(){
//     var upload = layui.upload;
//     //执行实例
//     upload.render({
//         accept: 'file',
//         method: 'post',
//         elem: '#upload', //绑定元素
//         url: 'http://localhost:8002/uploadFile', //上传接口
//         multiple: true,
//         data: {"fileSize" : fileSize, "sliceCount" : count,
//             "sliceSize" : sliceSize, "fileName" : fileName, "updateBy" : account, "path" : path},
//         before: function(obj){
//             obj.pushFile();
//             obj.preview(function(index, file, result){
//                 var sliceSize = 1024 * 1024;
//                 var fileSize = file.size;
//                 var count = parseInt(fileSize / sliceSize + 1);
//                 var fileName = file.name;
//             });
//         }
//         ,done: function(res){
//             addData();
//         }
//         ,error: function(){
//             //请求异常回调
//         }
//     });
// });

document.querySelector('input').onchange = function() {
    var sliceSize = 1024 * 1024;
    var file = this.files[0];
    console.log(file);
    var fileSize = file.size;
    var count = parseInt(fileSize / sliceSize + 1);
    var fileName = file.name;

    $.ajax({
        type : "post",
        url : domain + "/file/handShake",
        data : JSON.stringify({"fileSize" : fileSize, "sliceCount" : count,
            "sliceSize" : sliceSize, "fileName" : fileName, "updateBy" : account, "path" : path}),
        contentType : "application/json",
        success : function (data) {
            addData();
            var mergeCode = data.data;
            for (var i = 0; i < count; i++) {
                var start = i * sliceSize;
                var end = (i + 1) * sliceSize;
                if (i === count - 1) {
                    end = fileSize;
                }
                var sliceFile = file.slice(start, end);
                console.log(sliceFile);
                var form_data = new FormData();
                form_data.append("index", i);
                form_data.append("mergeCode", mergeCode);
                form_data.append("file", sliceFile);
                $.ajax({
                    type: "post",
                    url: domain + "/file/sliceUpload",
                    data: form_data,
                    // contentType : "multipart/form-data",
                    processData : false,
                    contentType : false,
                    error : function (data) {
                        layer.open({
                            type: 1,
                            content: data.msg
                        });
                    },
                    dataType: "json"
                })
            }
        },
        error : function (data) {
            layer.open({
                type: 1,
                content: data.msg
            });
        },
        dataType : "json"
    });
};

function pageUp() {
    if (path === "") {
        $("#pageUp").attr("class","layui-btn layui-btn-disabled").attr("onclick","");
    } else {
        $("#pageUp").attr("class","layui-btn layui-btn-normal").attr("onclick","back()");
    }
}
pageUp();

function back() {
    var index = path.lastIndexOf("/");
    path = path.substring(0, index);
    addData();
    pageUp();
}

function createFolderForm() {
    layui.use('layer', function(){
        var layer = layui.layer;
        layer.open({
            type: 1,
            area: '500px'
            ,btn: ['提交']
            ,closeBtn: 2
            ,yes: function(index, layero){
                var folderName = $("#folderName").val();
                if (folderName === "" || folderName === null) {
                    return;
                }
                $.post(domain + "/index/create",
                    {"updateBy" : account, "path" : path, "indexName" : folderName},
                    function (data) {
                        if (data.code === -1) {
                            layer.msg(data.msg);
                        }
                        addData();
                    },
                    "json"
                );
                layer.close(index);
            }
            ,content: '<input type="text" id = "folderName" name="folderName" required lay-verify="required" placeholder="请输入文件夹名称" autocomplete="off" class="layui-input">'
        });
    });
}

function timestampToTime(timestamp) {
    var date = new Date(timestamp);//时间戳为10位需*1000，时间戳为13位的话不需乘1000
    var Y = date.getFullYear() + '-';
    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-';
    var D = date.getDate() + ' ';
    var h = date.getHours() + ':';
    var m = date.getMinutes() + ':';
    var s = date.getSeconds();
    return Y+M+D+h+m+s;
}