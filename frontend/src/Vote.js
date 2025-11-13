import API from './api.js';

export default function Vote(electionId, candidates){
  const root = document.createElement('div');
  root.innerHTML = `<h4>Candidates</h4><div id='c'></div><div id='msg'></div>`;
  const cdiv = root.querySelector('#c');
  candidates.forEach(c=>{
    const d = document.createElement('div');
    d.innerHTML = `${c.name} (${c.party || ''}) <button data-id='${c.id}'>Vote</button>`;
    cdiv.appendChild(d);
  });
  cdiv.querySelectorAll('button').forEach(btn=>{
    btn.onclick = async ()=>{
      const id = btn.getAttribute('data-id');
      const token = localStorage.getItem('token');
      const res = await API.post('/elections/'+electionId+'/vote', {candidateId: Number(id)}, token);
      root.querySelector('#msg').innerText = JSON.stringify(res);
    };
  });
  return root;
}
