import API from './api.js';

export default function Login(onSuccess){
  const root = document.createElement('div');
  root.innerHTML = `
    <h3>Login</h3>
    <input id="username" placeholder="username"/><br/>
    <input id="password" type="password" placeholder="password"/><br/>
    <button id="doLogin">Login</button>
    <div id="msg"></div>
  `;
  root.querySelector('#doLogin').onclick = async ()=>{
    const u = root.querySelector('#username').value;
    const p = root.querySelector('#password').value;
    try{
      const r = await API.post('/auth/login', {username:u,password:p});
      if(r.token){
        localStorage.setItem('token', r.token);
        document.getElementById('msg').innerText = 'Logged in';
        onSuccess();
      } else {
        document.getElementById('msg').innerText = JSON.stringify(r);
      }
    }catch(e){ document.getElementById('msg').innerText = e.message; }
  };
  return root;
}
