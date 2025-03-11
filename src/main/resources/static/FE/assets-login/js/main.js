const BASE_URL = "http://127.0.0.1:8088/api/v1";
/*=============== SHOW HIDE PASSWORD LOGIN ===============*/
const passwordAccess = (loginPass, loginEye) =>{
   const input = document.getElementById(loginPass),
         iconEye = document.getElementById(loginEye)

   iconEye.addEventListener('click', () =>{
      // Change password to text
      input.type === 'password' ? input.type = 'text'
						              : input.type = 'password'

      // Icon change
      iconEye.classList.toggle('ri-eye-fill')
      iconEye.classList.toggle('ri-eye-off-fill')
   })
}
passwordAccess('password','loginPassword')

/*=============== SHOW HIDE PASSWORD CREATE ACCOUNT ===============*/
const passwordRegister = (loginPass, loginEye) =>{
   const input = document.getElementById(loginPass),
         iconEye = document.getElementById(loginEye)

   iconEye.addEventListener('click', () =>{
      // Change password to text
      input.type === 'password' ? input.type = 'text'
						              : input.type = 'password'

      // Icon change
      iconEye.classList.toggle('ri-eye-fill')
      iconEye.classList.toggle('ri-eye-off-fill')
   })
}
passwordRegister('passwordCreate','loginPasswordCreate')

/*=============== SHOW HIDE LOGIN & CREATE ACCOUNT ===============*/
const loginAcessRegister = document.getElementById('loginAccessRegister'),
      buttonRegister = document.getElementById('loginButtonRegister'),
      buttonAccess = document.getElementById('loginButtonAccess')

buttonRegister.addEventListener('click', () => {
   loginAcessRegister.classList.add('active')
})

buttonAccess.addEventListener('click', () => {
   loginAcessRegister.classList.remove('active')
})

// Handle Login
$('#loginForm').submit(function(event) {
   event.preventDefault();
   let email_login = $('#email').val();
   let password_login  = $('#password').val();

   $.ajax({
      url: `${BASE_URL}/auth/login`,
      type: "POST",
      data: JSON.stringify({ "email": email_login, "password": password_login }),
      contentType: "application/json",
      success: function (response) {
        // Xử lý phản hồi từ máy chủ
        console.log(response);
        var token = "Bearer " + response.data.token;
        localStorage.setItem('token', token);
        window.location.href = "./index.html";
      },
      error: function (xhr, status, error) {
        // Xử lý lỗi
        console.log(xhr.status);
      }
   });
});
