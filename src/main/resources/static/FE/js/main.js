let isLogin = !!localStorage.getItem("token");
const TOKEN = localStorage.getItem("token");
const BASE_URL = "http://127.0.0.1:8088/api/v1";
let currentUser = null;
let stompClient = null;
let connectedUsers = null;
function checkLogin() {
    if (isLogin != 1) {
        // let htmlSnippet = `
        //     <a class="nav-link text-black" href="./login.html" id="DA-HoVaTen">Đăng nhập</a>
        //         `;
        // $('#DA-loginCheck').html(htmlSnippet);
         window.location.href = "./login.html"
    } else{
        // getName();
        getCurentUser();
    }
};

function getCurentUser(){
    $.ajax({
        type: "GET",
        url: `${BASE_URL}/auth/getCurrentUser`,
        headers: {
            "Authorization": TOKEN
        },
        success: function (response) {
            currentUser = response.data;
            console.log(currentUser);   
            connect();
        },
        error: function (error, xhr) {
            console.log(error, xhr);
        }
    });
}

$(document).ready(function() {
    
    window.onbeforeunload = () => onLogout();

});

function connect() {
    findAndDisplayConnectedUsers();
        stompClient = new StompJs.Client({
            brokerURL: 'ws://127.0.0.1:8088/ws'
        });

        stompClient.onConnect = (frame) => {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/user/public', (user) => {
                userOnlineOffline(JSON.parse(user.body))
            });

            stompClient.publish({
                destination: '/app/user.addUser',
                body: JSON.stringify(currentUser)
            });

            stompClient.onWebSocketError = (error) => {
                console.error('Error with websocket', error);
            };
            stompClient.onStompError = (frame) => {
                console.error('Broker reported error: ' + frame.headers['message']);
                console.error('Additional details: ' + frame.body);
            };
        };
        stompClient.activate();
}
function userOnlineOffline(user){
    if (user.status == "ONLINE" && user.id != currentUser.id){
        $('#listUserOnline').append(`<div class="item-chat-user" id = "${user.id}">
            <div class="user-avatar ">
                <img src="./assets/avatar.png" class="img-user-avatar" alt="">
            </div>
            <div class="p_0 username-and-message" >
                <div class="user-name-and-new-time">
                    <div class="user-name">
                        ${user.fullName}
                    </div>
                    <div class="new-time">
                        Hôm qua
                    </div>
                </div>
                <div class="chat-message ">
                    <span style="margin-right: 4px;">Bạn:</span>
                    <p class="truncated m-0">Anh xin thông báo, vừa nhật được tin mật báo của Trưởng thôn </p>
                </div>
            </div>
        </div>`)
    }
    else if (user.status == "OFFLINE" && user.id != currentUser.id) {
        $('#listUserOnline').find(`#${user.id}`).remove();
    }
}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch(BASE_URL + `/user/online?id=${currentUser.id}`, {
        method: 'GET', // Hoặc phương thức phù hợp
        headers: {
          'Authorization': TOKEN,
          'Content-Type': 'application/json' // Nếu cần thiết
        },
      });
      connectedUsers = await connectedUsersResponse.json();
      renderUserOnline(connectedUsers);
      console.log(connectedUsers);
      
} 

function renderUserOnline(connectedUsers){
    if (connectedUsers.length > 0){
        connectedUsers.forEach(user => {
            $('#listUserOnline').append(`<div class="item-chat-user" id = "${user.id}">
                                    <div class="user-avatar ">
                                        <img src="./assets/avatar.png" class="img-user-avatar" alt="">
                                    </div>
                                    <div class="p_0 username-and-message" >
                                        <div class="user-name-and-new-time">
                                            <div class="user-name">
                                                ${user.fullName}
                                            </div>
                                            <div class="new-time">
                                                Hôm qua
                                            </div>
                                        </div>
                                        <div class="chat-message ">
                                            <span style="margin-right: 4px;">Bạn:</span>
                                            <p class="truncated m-0">Anh xin thông báo, vừa nhật được tin mật báo của Trưởng thôn </p>
                                        </div>
                                    </div>
                                </div>`)
        });
    }
}

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

// Handle click add friend
$('#btnAddFriend').on('click', function(event) {
    $('#btnCancelAddFriend').removeClass('hidden');
    $('#btnAddFriend').addClass('hidden');
        event.preventDefault();
    });
//Handle click cancel add friend
$('#btnCancelAddFriend').on('click', function(event) {
    $('#btnAddFriend').removeClass('hidden');
    $('#btnCancelAddFriend').addClass('hidden');
        event.preventDefault();
});

function onError() {
    console.log("errorrrrrr");
    
}


function onLogout() {
    stompClient.publish({
        destination: '/app/user.disconnect',
        body: JSON.stringify(currentUser)
    });
    // window.location.reload();
}

