$.ajax({
    url:"http://localhost:8080/tp2019/VerSession",
    type:"get",
    async:"false",
    dataType:"json",
    success:function(myJson){
        $("#titleUsuario").html(myJson.apellido+" "+myJson.nombre);
    },
    error:function(error){
        alert("Se produjo un error");
        console.log(error);
    }
});