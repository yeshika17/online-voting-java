import Login from './Login.js';
import Elections from './Elections.js';

export default function App(){
  const root = document.createElement('div');
  const nav = document.createElement('div');
  nav.innerHTML = '<button id="loginBtn">Login</button> <button id="listBtn">Elections</button>';
  root.appendChild(nav);
  const content = document.createElement('div');
  content.id='content';
  root.appendChild(content);

  document.addEventListener('click', (e)=>{
    if(e.target && e.target.id==='loginBtn') renderLogin();
    if(e.target && e.target.id==='listBtn') renderList();
  });

  function renderLogin(){ content.innerHTML=''; content.appendChild(Login(renderList)); }
  function renderList(){ content.innerHTML=''; content.appendChild(Elections()); }

  // initial
  renderList();
  return root;
}
