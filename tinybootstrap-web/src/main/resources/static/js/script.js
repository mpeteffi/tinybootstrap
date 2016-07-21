'use strict';

$(function(){

    $('html').on('click', '.send-btn', function(){
        $('.panel-danger').remove();
        $('#html-form').submit();
    });

    $('html').on('submit', '#html-form', function(event){
        toggleSendBtn();
        toggleLoader();
        submitForm($("#html-text").val(), $("#version-text").val());
        event.preventDefault();
    });
    
    $('html').on('click', '.again-btn', function(){
        location.reload();
    });
    
    $('html').on('click', '.copy-btn', function(){
        $('textarea').select();
    });
});

function toggleSendBtn(){
    $('.send-div').toggleClass('hide');
}

function toggleLoader(){
    $('.loader-div').toggleClass('hide');
}

function cleanContainer(){
    $('.panel-default').addClass('hide');
    $('.loader-div').addClass('hide');
    $('.send-div').addClass('hide');
}

function submitForm(conteudo, vers){
    $.ajax({url:'/', type:'POST', data:{html: conteudo, version: vers}})
        .always(function(res){
            if(res.indexOf('data-type="success"') > -1){
                cleanContainer();
                $('.container').append(res);
            } else {
                toggleLoader();
                toggleSendBtn();
                $('.send-div').before(res);
            }
        });
}
