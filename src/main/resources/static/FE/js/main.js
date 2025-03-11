let isLogin = !!localStorage.getItem("token");
const TOKEN = localStorage.getItem("token");
const BASE_URL = "http://127.0.0.1:8088/api/v1";
let currentUser = null;
let stompClient = null;
let usersInChatRoom = null;
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
    findAndDisplayUsers();
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
        $(`#${user.id} .dot-activate`).removeClass('hidden');
    }
    else if (user.status == "OFFLINE" && user.id != currentUser.id) {
        $(`#${user.id} .dot-activate`).addClass('hidden');
    }
}

async function findAndDisplayUsers() {
    const connectedUsersResponse = await fetch(BASE_URL + `/chat-room/get-user-in-chat-room?limit=2&page=1`, {
        method: 'GET', 
        headers: {
          'Authorization': TOKEN,
          'Content-Type': 'application/json' 
        },
      });
      usersInChatRoom = await connectedUsersResponse.json();
      renderUsersChatRoom(usersInChatRoom);
      console.log(usersInChatRoom);
      
} 

function handleSendDate(sendDateInput){
    let sendDate = new Date(sendDateInput);
    let currentDay = new Date();
    let differenceInTime = Math.ceil(((currentDay - sendDate) / (1000 * 3600 * 24)).toFixed(1));
    
    let textDay = "Hôm nay";
    switch (differenceInTime) {
        case 0: 
            textDay = sendDate.getHours() + ":" + String(sendDate.getMinutes()).padStart(2, '0'); 
            break;
        case 1:
            textDay = "Hôm qua";
            break;
        case 2:
            textDay = "2 ngày";
            break;
        case 3:
            textDay = "3 ngày";
            break;
        case 4:
            textDay = "4 ngày";
            break;
        case 5:
            textDay = "5 ngày";
            break;
        default: {
            if (sendDate.getFullYear() != currentDay.getFullYear()){
                textDay = sendDate.getDate() + "/" + (sendDate.getMonth() + 1) + "/" (sendDate.getFullYear() - 2000)
            }
            else{
                textDay = sendDate.getDate() + "/" + (sendDate.getMonth() + 1)
            }
            break;
        }
    }
    return textDay;
}

function renderUsersChatRoom(usersInChatRoom){
    if (usersInChatRoom.length > 0){
        usersInChatRoom.forEach(user => {
            let textDay = handleSendDate(user.updatedAt);
            let isMe = user.senderId == currentUser.id ? true : false;
            $('#listUserOnline').append(`<div class="item-chat-user" id = "${user.id}">
                                    <div class="user-avatar position-relative">
                                        <img src="./assets/avatar.png" class="img-user-avatar" alt="">
                                        <span class="position-absolute translate-middle border rounded-circle dot-activate ${user.status == "0" ? "" : "hidden"}"></span>
                                    </div>
                                    <div class="p_0 username-and-message" >
                                        <div class="user-name-and-new-time">
                                            <div class="user-name">
                                                ${user.fullName}
                                            </div>
                                            <div class="new-time">
                                                ${textDay}
                                            </div>
                                        </div>
                                        <div class="chat-message ">
                                            ${isMe ? '<span style="padding-right: 4px;">Bạn:</span>' : ""}
                                            <p class="truncated m-0">${user.content}</p>
                                        </div>
                                    </div>
                                </div>`);
            $(`#${user.id}`).on('click', () => {
                $('#titleName').text(user.fullName);

            })
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

