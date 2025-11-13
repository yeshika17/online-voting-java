import API from './api.js';
import Vote from './Vote.js';

export default function Elections(){
  const root = document.createElement('div');
  root.innerHTML = '<h3>Elections</h3><div id="list"></div>';
  async function load(){
    const list = await API.get('/elections');
    const container = root.querySelector('#list');
    if(Array.isArray(list)){
      list.forEach(e=>{
        const el = document.createElement('div');
        el.innerHTML = `<b>${e.name}</b> - ${e.description || ''} <button data-id='${e.id}' class='view'>View</button>`;
        container.appendChild(el);
      });
      container.querySelectorAll('.view').forEach(btn=>{
        btn.onclick = async ()=> {
          const id = btn.getAttribute('data-id');
          const cand = await API.get('/elections/'+id+'/candidates');
          container.innerHTML = '';
          container.appendChild(Vote(id, cand));
        };
      });
    } else {
      root.querySelector('#list').innerText = JSON.stringify(list);
    }
  }
  load();
  return root;
}
