$(document).ready(function() {
    $('#input-text-message').on('keydown', function(event) {
        // Kiểm tra nếu phím nhấn là Enter (keyCode 13)
        if (event.key === 'Enter') {
            const container = $('#display-message-area')
            if ($(this).val().trim() !== ''){
                $('#display-message-area').append(`<div class="message-segment my-message" >
                    <div class="message-key ">
                        ${$(this).val()}
                    </div>
                </div>`);
                $('#display-message-area').scrollTop($('#display-message-area')[0].scrollHeight);
                console.log('Bạn đã nhấn Enter! Nội dung: ' + $(this).val());
                $('#input-text-message').val('');
            }         
            
            event.preventDefault();
        }
    });


});
// Handle click add friend
$('#btnAddFriend').on('keydown', function(event) {
    $('#btnCancelAddFriend').removeClass('hidden');
    $('#btnAddFriend').addClass('hidden');
        event.preventDefault();
    });
//Handle click cancel add friend
$('#btnCancelAddFriend').on('keydown', function(event) {
    $('#btnAddFriend').removeClass('hidden');
    $('#btnCancelAddFriend').addClass('hidden');
        event.preventDefault();
    });