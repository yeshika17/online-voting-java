const API = {
  base: 'http://localhost:8080/api',
  async post(path, body, token){
    const res = await fetch(this.base+path, {
      method: 'POST',
      headers: {'Content-Type':'application/json', ...(token?{Authorization:'Bearer '+token}:{})},
      body: JSON.stringify(body)
    });
    return res.json();
  },
  async get(path, token){
    const res = await fetch(this.base+path, {headers: {...(token?{Authorization:'Bearer '+token}:{})}});
    return res.json();
  }
};
export default API;
